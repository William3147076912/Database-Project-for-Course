<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学生得分" prop="score">
        <el-input
          v-model="queryParams.score"
          placeholder="请输入学生得分"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评分的教师ID" prop="graderId">
        <el-input
          v-model="queryParams.graderId"
          placeholder="请输入评分的教师ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成绩评定时间" prop="gradingTime">
        <el-date-picker clearable
          v-model="queryParams.gradingTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择成绩评定时间">
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
          v-hasPermi="['grade:grade:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['grade:grade:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['grade:grade:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['grade:grade:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="gradeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关联的作业提交记录ID" align="center" prop="submissionId" />
      <el-table-column label="学生得分" align="center" prop="score" />
      <el-table-column label="评分的教师ID" align="center" prop="graderId" />
      <el-table-column label="成绩评定时间" align="center" prop="gradingTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.gradingTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="教师对作业的反馈评语" align="center" prop="comments" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['grade:grade:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['grade:grade:remove']">删除</el-button>
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

    <!-- 添加或修改成绩对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="gradeRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学生得分" prop="score">
          <el-input v-model="form.score" placeholder="请输入学生得分" />
        </el-form-item>
        <el-form-item label="评分的教师ID" prop="graderId">
          <el-input v-model="form.graderId" placeholder="请输入评分的教师ID" />
        </el-form-item>
        <el-form-item label="成绩评定时间" prop="gradingTime">
          <el-date-picker clearable
            v-model="form.gradingTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择成绩评定时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="教师对作业的反馈评语" prop="comments">
          <el-input v-model="form.comments" type="textarea" placeholder="请输入内容" />
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

<script setup name="Grade">
import { listGrade, getGrade, delGrade, addGrade, updateGrade } from "@/api/grade/grade";

const { proxy } = getCurrentInstance();

const gradeList = ref([]);
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
    score: null,
    graderId: null,
    gradingTime: null,
    comments: null
  },
  rules: {
    score: [
      { required: true, message: "学生得分不能为空", trigger: "blur" }
    ],
    graderId: [
      { required: true, message: "评分的教师ID不能为空", trigger: "blur" }
    ],
    gradingTime: [
      { required: true, message: "成绩评定时间不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询成绩列表 */
function getList() {
  loading.value = true;
  listGrade(queryParams.value).then(response => {
    gradeList.value = response.rows;
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
    submissionId: null,
    score: null,
    graderId: null,
    gradingTime: null,
    comments: null
  };
  proxy.resetForm("gradeRef");
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
  ids.value = selection.map(item => item.submissionId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加成绩";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _submissionId = row.submissionId || ids.value
  getGrade(_submissionId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改成绩";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["gradeRef"].validate(valid => {
    if (valid) {
      if (form.value.submissionId != null) {
        updateGrade(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addGrade(form.value).then(response => {
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
  const _submissionIds = row.submissionId || ids.value;
  proxy.$modal.confirm('是否确认删除成绩编号为"' + _submissionIds + '"的数据项？').then(function() {
    return delGrade(_submissionIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('grade/grade/export', {
    ...queryParams.value
  }, `grade_${new Date().getTime()}.xlsx`)
}

getList();
</script>
