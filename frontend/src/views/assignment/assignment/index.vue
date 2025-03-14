<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="任务标题" prop="title">
        <el-input
            v-model="queryParams.title"
            placeholder="请输入任务标题"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联的课程" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择关联的课程" clearable>
          <el-option
              v-for="item in courseList"
              :key="item.courseId"
              :label="item.name"
              :value="item.courseId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建的教师" prop="creatorId">
        <el-select v-model="queryParams.creatorId" placeholder="请选择创建的教师" clearable>
          <el-option
              v-for="item in teacherMap"
              :key="item.creatorId"
              :label="item.creatorName"
              :value="item.creatorId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="任务类型" prop="assignmentType">
        <el-select v-model="queryParams.assignmentType" placeholder="请选择任务类型" clearable>
          <el-option
              v-for="dict in assignment_type"
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
            v-hasPermi="['assignment:assignment:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['assignment:assignment:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="assignmentList" @selection-change="handleSelectionChange">
      <!--<el-table-column label="任务ID" align="center" prop="assignmentId" />-->
      <el-table-column label="任务标题" align="center" prop="title"/>
      <el-table-column label="详细说明" align="center" prop="description"/>
      <el-table-column label="任务类型" align="center" prop="assignmentType">
        <template #default="scope">
          <dict-tag :options="assignment_type" :value="scope.row.assignmentType"/>
        </template>
      </el-table-column>
      <el-table-column label="关联的课程" align="center" prop="courseName"/>
      <el-table-column label="创建的教师" align="center" prop="creatorName"/>
      <el-table-column label="满分分值" align="center" prop="fullScore"/>
      <el-table-column label="截止时间" align="center" prop="deadline" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.deadline, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="creationTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.creationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdateOrSearch(scope.row)"
                     v-hasPermi="['assignment:assignment:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['assignment:assignment:remove']">删除
          </el-button>
          <el-button link type="primary" icon="search" @click="handleUpdateOrSearch(scope.row)"
          >查看
          </el-button>
          <el-button link type="primary" icon="Edit" @click="handleSubmit(scope.row)"
          >提交
          </el-button>
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

    <!-- 添加或修改作业/考试对话框 -->
    <el-dialog :title="title" v-model="open" width="1200px" append-to-body>
      <el-form ref="assignmentRef" :model="form" :rules="rules" label-width="100px">
        <div v-if="!useUserStore().roles.includes('student')">
          <el-form-item label="任务标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入任务标题"/>
          </el-form-item>
          <el-form-item label="详细说明" prop="description">
            <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
          </el-form-item>
          <el-form-item label="任务类型" prop="assignmentType">
            <el-select v-model="form.assignmentType" placeholder="请选择任务类型">
              <el-option
                  v-for="dict in assignment_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关联的课程" prop="courseId">
            <el-select v-model="form.courseId" placeholder="请选择关联的课程">
              <el-option
                  v-for="dict in courseList"
                  :key="dict.courseId"
                  :label="dict.name"
                  :value="dict.courseId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="满分分值" prop="fullScore">
            <el-input v-model="form.fullScore" placeholder="请输入任务的满分分值"/>
          </el-form-item>
          <el-form-item label="截止时间" prop="deadline">
            <el-date-picker clearable
                            v-model="form.deadline"
                            type="date"
                            value-format="YYYY-MM-DD"
                            placeholder="请选择作业提交截止时间">
            </el-date-picker>
          </el-form-item>
        </div>
        <div v-else>
          <!--使用el-descriptions组件以卡片形式展示信息，更简洁-->
          <el-descriptions :column="1" border>
            <el-descriptions-item label="任务标题">{{ form.title }}</el-descriptions-item>
            <el-descriptions-item label="详细说明">{{ form.description }}</el-descriptions-item>
            <el-descriptions-item label="任务类型">
              <dict-tag :options="assignment_type" :value="form.assignmentType"/>
            </el-descriptions-item>
            <el-descriptions-item label="关联的课程">
              {{ courseList.filter(item => item.courseId === form.courseId)[0]?.name }}
            </el-descriptions-item>
            <el-descriptions-item label="满分分值">{{ form.fullScore }}</el-descriptions-item>
            <el-descriptions-item label="截止时间">{{ parseTime(form.deadline, '{y}-{m}-{d}') }}</el-descriptions-item>
          </el-descriptions>

        </div>
        <div v-if="form.courseId!==null">
          <el-divider content-position="center">作业提交记录信息</el-divider>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="danger" icon="Delete" @click="handleDeleteSubmission"
                         v-hasPermi="['assignment:assignment:remove']">删除
              </el-button>
            </el-col>
          </el-row>
          <el-table :data="submissionList" :row-class-name="rowSubmissionIndex"
                    @selection-change="handleSubmissionSelectionChange" ref="submission">
            <el-table-column type="selection" align="center"/>
            <!--<el-table-column label="序号" align="center" prop="index" width="50"/>-->
            <el-table-column label="学生" prop="studentName"/>
            <el-table-column label="作业提交时间" prop="submissionTime"/>
            <el-table-column label="成绩" prop="score"/>
            <el-table-column label="评价" prop="comments"/>
            <el-table-column label="提交状态" prop="status">
              <template #default="scope">
                <dict-tag :options="submitted_status" :value="scope.row.status"/>
              </template>
            </el-table-column>
            <el-table-column label="上传的作业" align="center" class-name="small-padding fixed-width">
              <template #default="scope">
                <el-button link type="primary" @click="handleDownload(scope.row)"
                >{{ extractFileName(scope.row.contentOrFilePath) }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width"
                             v-if="useUserStore().roles.includes('teacher')|| useUserStore().roles.includes('admin')">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleEvaluation(scope.row)"
                >评价
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog title="作业提交" v-model="openSubmission" width="600px" append-to-body>
      <file-upload v-model="submissionParams.contentOrFilePath" :file-type="['doc','txt','pdf']"
                   :file-size="50" :limit="1"></file-upload>
      <template #footer>
        <div class="dialog-footer centered-footer">
          <el-button :disabled="submissionParams.contentOrFilePath === null" type="primary"
                     @click="confirmSubmission">确 定
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog title="评价作业" v-model="openEvaluation" width="600px" append-to-body>
      <el-form ref="gradeRef" :model="gradeParams" :rules="gradeRules" label-width="100px">
        <el-form-item label="成绩" prop="score">
          <el-input-number v-model="gradeParams.score" :min="0" :max="form.fullScore" placeholder="成绩"/>
        </el-form-item>
        <el-form-item label="评价" prop="comments">
          <el-input v-model="gradeParams.comments" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer centered-footer">
          <el-button :disabled="gradeParams.score === null" type="primary"
                     @click="confirmGrade">确 定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup >
import {
  listAssignment,
  getAssignment,
  delAssignment,
  addAssignment,
  updateAssignment
} from "@/api/assignment/assignment";
import useUserStore from "@/store/modules/user.js";
import {parseTime} from "@/utils/ruoyi.js";
import {listCourse} from "@/api/course/course.js";
import FileUpload from "@/components/FileUpload/index.vue";
import {addSubmission, listSubmission, updateSubmission} from "@/api/submission/submission.js";
import download from "@/plugins/download.js";

const {proxy} = getCurrentInstance();
const {assignment_type} = proxy.useDict('assignment_type');
const {submitted_status} = proxy.useDict('submitted_status');
const assignmentList = ref([]);
const courseList = ref([]);
const submissionList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedSubmission = ref([]);
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
    creatorId: null,
    assignmentType: null,
    title: null,
  },
  rules: {
    courseId: [
      {required: true, message: "关联的课程不能为空", trigger: "blur"}
    ],
    creatorId: [
      {required: true, message: "创建作业/考试的教师不能为空", trigger: "blur"}
    ],
    assignmentType: [
      {required: true, message: "任务类型不能为空", trigger: "change"}
    ],
    title: [
      {required: true, message: "作业/考试标题不能为空", trigger: "blur"}
    ],
    fullScore: [
      {required: true, message: "作业/考试的满分分值不能为空", trigger: "blur"}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);
const teacherMap = ref([])

/** 查询作业/考试列表 */
function getList() {
  loading.value = true;
  listAssignment(queryParams.value).then(response => {
    assignmentList.value = response.rows;
    total.value = response.total;
    loading.value = false;
    teacherMap.value = response.rows.map(item => {
      return {
        creatorId: item.creatorId,
        creatorName: item.creatorName
      }
    })
    // 根据教师id去重
    teacherMap.value = teacherMap.value.filter((item, index, arr) =>
        index === arr.findIndex(t => t.creatorId === item.creatorId))
  });
  listCourse({pageNum: 1, pageSize: 100}).then(response => {
    courseList.value = response.rows;
  });
}

/* 控制学生提交页面的开启 */
const openSubmission = ref(false);
const submissionParams = ref({
  assignmentId: null,
  studentId: null,
  contentOrFilePath: null
})

/* 学生提交作业的按钮操作 */
function handleSubmit(row) {
  submissionParams.value.assignmentId = row.assignmentId;
  submissionParams.value.studentId = useUserStore().id;
  // 打开提交页面
  openSubmission.value = true;
}

function confirmSubmission() {
  proxy.$confirm('确认提交吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    addSubmission(submissionParams.value);
    proxy.$message({
      type: 'success',
      message: '提交成功'
    });
    submissionParams.value.contentOrFilePath = null;
    openSubmission.value = false;
  }).catch({})
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    assignmentId: null,
    courseId: null,
    creatorId: null,
    assignmentType: null,
    title: null,
    description: null,
    deadline: null,
    fullScore: null,
    creationTime: null
  };
  submissionList.value = [];
  proxy.resetForm("assignmentRef");
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
  ids.value = selection.map(item => item.assignmentId);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加任务";
}

/** （老师方）修改或（学生方）查看按钮操作 */
function handleUpdateOrSearch(row) {
  reset();
  const _assignmentId = row.assignmentId || ids.value
  getAssignment(_assignmentId).then(response => {
    form.value = response.data;
    submissionList.value = response.data.submissionList;
    open.value = true;
    title.value = "修改任务";
    // console.log(JSON.stringify(form.value))
  });
}

const openEvaluation = ref(false)
const gradeParams = ref({
  submissionId: null,
  score: null,
  graderId: null,
  comments: null
})
const gradeRules = reactive({
  score: [
    {required: true, message: "评分不能为空", trigger: "blur"}
  ],
})

/* 评价按钮操作 */
function handleEvaluation(row) {
  openEvaluation.value = true
  gradeParams.value.submissionId = row.submissionId;
  gradeParams.value.score = row.score;
  gradeParams.value.graderId = useUserStore().id;
  gradeParams.value.comments = row.comments;
}

/* 确认提交评价 */
function confirmGrade() {
  proxy.$refs["gradeRef"].validate(valid => {
    if (valid) {
      updateSubmission(gradeParams.value).then(response => {
        proxy.$modal.msgSuccess("评价完成！");
        openEvaluation.value = false;
        getList();
      });
    }
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["assignmentRef"].validate(valid => {
    if (valid) {
      form.value.submissionList = submissionList.value;
      if (form.value.assignmentId != null) {
        updateAssignment(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        // 添加创建任务的老师id
        form.value.creatorId = useUserStore().id;
        addAssignment(form.value).then(response => {
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
  const _assignmentIds = row.assignmentId || ids.value;
  proxy.$modal.confirm('是否确认删除作业/考试编号为"' + _assignmentIds + '"的数据项？').then(function () {
    return delAssignment(_assignmentIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 作业提交记录序号 */
function rowSubmissionIndex({row, rowIndex}) {
  row.index = rowIndex + 1;
}

/** 作业提交记录添加按钮操作 */
function handleAddSubmission() {
  let obj = {};
  obj.studentId = "";
  obj.contentOrFilePath = "";
  obj.submissionTime = "";
  obj.status = "";
  submissionList.value.push(obj);
}

/** 作业提交记录删除按钮操作 */
function handleDeleteSubmission() {
  if (checkedSubmission.value.length == 0) {
    proxy.$modal.msgError("请先选择要删除的作业提交记录数据");
  } else {
    const submissions = submissionList.value;
    const checkedSubmissions = checkedSubmission.value;
    submissionList.value = submissions.filter(function (item) {
      return checkedSubmissions.indexOf(item.index) == -1
    });
  }
}

/** 复选框选中数据 */
function handleSubmissionSelectionChange(selection) {
  checkedSubmission.value = selection.map(item => item.index)
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('assignment/assignment/export', {
    ...queryParams.value
  }, `assignment_${new Date().getTime()}.xlsx`)
}

/** 下载按钮操作 */
function handleDownload(row) {
  download.resource(row.contentOrFilePath)
}

/* 从文件路径中提取文件名 */
function extractFileName(filePath) {
  const parts = filePath.split('/');
  const fileNameWithTimestamp = parts[parts.length - 1];
  const baseName = fileNameWithTimestamp.split('_')[0];
  const extension = fileNameWithTimestamp.split('.').pop();
  return `${baseName}.${extension}`;
}

getList();
</script>
<style scoped>
.centered-footer {
  display: flex;
  justify-content: center;
}
</style>