<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="选修的课程" prop="courseId">
        <el-input
          v-model="queryParams.courseId"
          placeholder="请输入选修的课程"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="选课状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择选课状态" clearable>
          <el-option
            v-for="dict in enrollment_status"
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

<!--    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['enrollment:enrollment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['enrollment:enrollment:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['enrollment:enrollment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['enrollment:enrollment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>-->

    <el-table v-loading="loading" :data="enrollmentList" @selection-change="handleSelectionChange">
      <!--<el-table-column type="selection" width="55" align="center" />-->
      <!--<el-table-column label="选课记录ID" align="center" prop="enrollmentId" />-->
      <!--<el-table-column label="选课学生" align="center" prop="studentId" />-->
      <el-table-column label="选修的课程" align="center" prop="courseId" />
      <el-table-column label="学生选课时间" align="center" prop="enrollmentTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.enrollmentTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="选课状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="enrollment_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="课程完成时间" align="center" prop="completionTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.completionTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['enrollment:enrollment:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['enrollment:enrollment:remove']">删除</el-button>
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

    <!-- 添加或修改选课记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="enrollmentRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="选课学生" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入选课学生" />
        </el-form-item>
        <el-form-item label="选修的课程ID" prop="courseId">
          <el-input v-model="form.courseId" placeholder="请输入选修的课程ID" />
        </el-form-item>
        <el-form-item label="学生选课时间" prop="enrollmentTime">
          <el-date-picker clearable
            v-model="form.enrollmentTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择学生选课时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="选课状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择选课状态">
            <el-option
              v-for="dict in enrollment_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程完成时间" prop="completionTime">
          <el-date-picker clearable
            v-model="form.completionTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择课程完成时间">
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

<script setup name="Enrollment">
import { listEnrollment, getEnrollment, delEnrollment, addEnrollment, updateEnrollment } from "@/api/enrollment/enrollment";

const { proxy } = getCurrentInstance();
const { enrollment_status } = proxy.useDict('enrollment_status');

const enrollmentList = ref([]);
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
    studentId: null,
    courseId: null,
    enrollmentTime: null,
    status: null,
    completionTime: null
  },
  rules: {
    studentId: [
      { required: true, message: "选课学生不能为空", trigger: "blur" }
    ],
    courseId: [
      { required: true, message: "选修的课程ID不能为空", trigger: "blur" }
    ],
    enrollmentTime: [
      { required: true, message: "学生选课时间不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "选课状态不能为空", trigger: "change" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询选课记录列表 */
function getList() {
  loading.value = true;
  listEnrollment(queryParams.value).then(response => {
    enrollmentList.value = response.rows;
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
    enrollmentId: null,
    studentId: null,
    courseId: null,
    enrollmentTime: null,
    status: null,
    completionTime: null
  };
  proxy.resetForm("enrollmentRef");
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
  ids.value = selection.map(item => item.enrollmentId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加选课记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _enrollmentId = row.enrollmentId || ids.value
  getEnrollment(_enrollmentId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改选课记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["enrollmentRef"].validate(valid => {
    if (valid) {
      if (form.value.enrollmentId != null) {
        updateEnrollment(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addEnrollment(form.value).then(response => {
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
  const _enrollmentIds = row.enrollmentId || ids.value;
  proxy.$modal.confirm('是否确认删除选课记录编号为"' + _enrollmentIds + '"的数据项？').then(function() {
    return delEnrollment(_enrollmentIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('enrollment/enrollment/export', {
    ...queryParams.value
  }, `enrollment_${new Date().getTime()}.xlsx`)
}

getList();
</script>
