<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户登录名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户登录名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="加密后的密码" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="请输入加密后的密码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户所属的教育机构" prop="institutionId">
        <el-input
          v-model="queryParams.institutionId"
          placeholder="请输入用户所属的教育机构"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户账号创建时间" prop="registrationTime">
        <el-date-picker clearable
          v-model="queryParams.registrationTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择用户账号创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['user:user:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['user:user:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['user:user:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['user:user:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="用户登录名" align="center" prop="username" />
      <el-table-column label="加密后的密码" align="center" prop="password" />
      <el-table-column label="文件地址" align="center" prop="email" />
      <el-table-column label="用户角色：Admin" align="center" prop="role" />
      <el-table-column label="用户所属的教育机构" align="center" prop="institutionId" />
      <el-table-column label="用户账号创建时间" align="center" prop="registrationTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.registrationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['user:user:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['user:user:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户登录名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户登录名" />
        </el-form-item>
        <el-form-item label="加密后的密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入加密后的密码" />
        </el-form-item>
        <el-form-item label="文件地址" prop="email">
          <file-upload v-model="form.email" />
        </el-form-item>
        <el-form-item label="用户所属的教育机构" prop="institutionId">
          <el-input v-model="form.institutionId" placeholder="请输入用户所属的教育机构" />
        </el-form-item>
        <el-form-item label="用户账号创建时间" prop="registrationTime">
          <el-date-picker clearable
            v-model="form.registrationTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择用户账号创建时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="User">
import { listUser, getUser, delUser, addUser, updateUser } from "@/api/user/user";

const { proxy } = getCurrentInstance();

const userList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    username: null,
    password: null,
    email: null,
    role: null,
    institutionId: null,
    registrationTime: null
  },
  rules: {
    username: [
      { required: true, message: "用户登录名不能为空", trigger: "blur" }
    ],
    password: [
      { required: true, message: "加密后的密码不能为空", trigger: "blur" }
    ],
    email: [
      { required: true, message: "文件地址不能为空", trigger: "blur" }
    ],
    role: [
      { required: true, message: "用户角色：Admin不能为空", trigger: "blur" }
    ],
    institutionId: [
      { required: true, message: "用户所属的教育机构不能为空", trigger: "blur" }
    ],
    registrationTime: [
      { required: true, message: "用户账号创建时间不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询用户信息列表 */
function getList() {
  loading.value = true;
  listUser(queryParams.value).then(response => {
    userList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    userId: null,
    username: null,
    password: null,
    email: null,
    role: null,
    institutionId: null,
    registrationTime: null
  };
  proxy.resetForm("userRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.userId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _userId = row.userId || ids.value
  getUser(_userId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userRef"].validate(valid => {
    if (valid) {
      if (form.value.userId != null) {
        updateUser(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUser(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _userIds = row.userId || ids.value;
  proxy.$modal.confirm('是否确认删除用户信息编号为"' + _userIds + '"的数据项？').then(function() {
    return delUser(_userIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/user/export', {
    ...queryParams.value
  }, `user_${new Date().getTime()}.xlsx`)
}

getList();
</script>
