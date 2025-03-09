<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件名称" prop="resourceName">
        <el-input
          v-model="queryParams.resourceName"
          placeholder="请输入文件名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源类型" prop="resourceType">
        <el-select v-model="queryParams.resourceType" placeholder="请选择资源类型" clearable>
          <el-option
            v-for="dict in resource_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['resource:resource:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['resource:resource:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['resource:resource:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['resource:resource:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="resourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资源ID" align="center" prop="resourceId" />
      <el-table-column label="资源所属课程" align="center" prop="courseId" />
      <el-table-column label="上传资源的教师" align="center" prop="uploaderId" />
      <el-table-column label="文件名称" align="center" prop="resourceName" />
      <el-table-column label="资源类型" align="center" prop="resourceType">
        <template #default="scope">
          <dict-tag :options="resource_type" :value="scope.row.resourceType"/>
        </template>
      </el-table-column>
      <el-table-column label="上传时间" align="center" prop="uploadTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.uploadTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['resource:resource:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['resource:resource:remove']">删除</el-button>
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

    <!-- 添加或修改教学资源对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="resourceRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件名称" prop="resourceName">
          <el-input v-model="form.resourceName" placeholder="请输入文件名称" />
        </el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-select v-model="form.resourceType" placeholder="请选择资源类型">
            <el-option
              v-for="dict in resource_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上传时间" prop="uploadTime">
          <el-date-picker clearable
            v-model="form.uploadTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择上传时间">
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

<script setup name="Resource">
import { listResource, getResource, delResource, addResource, updateResource } from "@/api/resource/resource";

const { proxy } = getCurrentInstance();
const { resource_type } = proxy.useDict('resource_type');

const resourceList = ref([]);
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
    courseId: null,
    uploaderId: null,
    resourceName: null,
    resourceType: null,
  },
  rules: {
    courseId: [
      { required: true, message: "资源所属课程不能为空", trigger: "change" }
    ],
    resourceName: [
      { required: true, message: "文件名称不能为空", trigger: "blur" }
    ],
    resourceType: [
      { required: true, message: "资源类型不能为空", trigger: "change" }
    ],
    uploadTime: [
      { required: true, message: "上传时间不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询教学资源列表 */
function getList() {
  loading.value = true;
  listResource(queryParams.value).then(response => {
    resourceList.value = response.rows;
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
    resourceId: null,
    courseId: null,
    uploaderId: null,
    resourceName: null,
    filePath: null,
    resourceType: null,
    uploadTime: null
  };
  proxy.resetForm("resourceRef");
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
  ids.value = selection.map(item => item.resourceId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加教学资源";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _resourceId = row.resourceId || ids.value
  getResource(_resourceId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改教学资源";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["resourceRef"].validate(valid => {
    if (valid) {
      if (form.value.resourceId != null) {
        updateResource(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addResource(form.value).then(response => {
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
  const _resourceIds = row.resourceId || ids.value;
  proxy.$modal.confirm('是否确认删除教学资源编号为"' + _resourceIds + '"的数据项？').then(function() {
    return delResource(_resourceIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('resource/resource/export', {
    ...queryParams.value
  }, `resource_${new Date().getTime()}.xlsx`)
}

getList();
</script>
