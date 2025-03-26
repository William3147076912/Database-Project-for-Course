<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入课程名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属学科" prop="subject">
        <el-input
            v-model="queryParams.subject"
            placeholder="请输入课程所属学科"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择课程状态" clearable>
          <el-option
              v-for="dict in course_status"
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
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['course:course:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-form-item label="课程总数：" prop="name">{{total}}</el-form-item>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" v-if="!isStudent"/>
      <el-table-column label="课程id" align="center" prop="courseId"/>
      <el-table-column label="课程名称" align="center" prop="name"/>
      <el-table-column label="课程简介" align="center" prop="description" show-overflow-tooltip/>
      <el-table-column label="课程状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="course_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="所属学科" align="center" prop="subject"/>
      <el-table-column label="选课人数" align="center" prop="enrollmentCount"/>
      <el-table-column label="完成率" align="center" prop="completionRate"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Search" @click="handleInfo(scope.row)"
                     v-hasPermi="['course:course:list']">查看
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


    <!-- 查看课程管理详情对话框 -->
    <el-dialog title="课程信息"  v-model="infoOpen" width="800px" append-to-body>
      <!--使用el-descriptions组件以卡片形式展示信息，更简洁-->
      <el-descriptions :column="1" border>
        <el-descriptions-item label="课程名称">{{ form.name }}</el-descriptions-item>
        <el-descriptions-item label="课程简介">{{ form.description }}</el-descriptions-item>
        <el-descriptions-item label="所属学科">{{ form.subject }}</el-descriptions-item>
        <el-descriptions-item label="课程状态">
          <dict-tag :options="course_status" :value="form.status"/>
        </el-descriptions-item>
        <el-descriptions-item label="选课人数">{{ form.enrollmentCount }}</el-descriptions-item>
        <el-descriptions-item label="课程完成率">{{ form.completionRate }}%</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup name="Course">
import {listCourse, getCourse, delCourse, addCourse, updateCourse,listCourseWithStatistics} from "@/api/course/course";
import {getUser, getUserProfile} from "@/api/system/user.js";
import {addEnrollment, delEnrollment, delEnrollmentByCourseId, listEnrollment} from "@/api/enrollment/enrollment.js";
import {onBeforeRouteUpdate} from "vue-router";
import useUserStore from "@/store/modules/user.js";

const {proxy} = getCurrentInstance();
const {course_status} = proxy.useDict('course_status');

const courseList = ref([]);
const open = ref(false);
const infoOpen = ref(false);
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
    name: null,
    description: null,
    objectives: null,
    content: null,
    syllabus: null,
    status: null,
    reviewerId: null,
    creatorId: null,
    creationTime: null,
    subject: null,
    courseType: null,
    enrollmentCount:null,
    completionRate:null
  },
  rules: {
    name: [
      {required: true, message: "课程标题不能为空", trigger: "blur"}
    ],
    description: [
      {required: true, message: "课程简要描述不能为空", trigger: "blur"}
    ],
    status: [
      {required: true, message: "课程状态：草稿、审核中、已上线、已结束不能为空", trigger: "change"}
    ],
    creatorId: [
      {required: true, message: "课程创建者ID不能为空", trigger: "blur"}
    ],
    creationTime: [
      {required: true, message: "课程创建时间不能为空", trigger: "blur"}
    ],
    subject: [
      {required: true, message: "课程所属学科不能为空", trigger: "blur"}
    ],
    courseType: [
      {required: true, message: "课程类型不能为空", trigger: "change"}
    ]
  }
});

const {queryParams, form, rules} = toRefs(data);
// 已选课的列表
const selectedCourseList = ref([]);

/** 查询课程管理列表 */
async function getList() {
  loading.value = true;
  await listCourseWithStatistics(queryParams.value).then(response => {
    courseList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}


// 表单重置
function reset() {
  form.value = {
    courseId: null,
    name: null,
    description: null,
    objectives: null,
    content: null,
    syllabus: null,
    status: null,
    reviewerId: null,
    creatorId: null,
    creationTime: null,
    subject: null,
    courseType: null,
    enrollmentCount:null,
    completionRate:null
  };
  proxy.resetForm("courseRef");
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
  ids.value = selection.map(item => item.courseId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */

/* 查看详情操作 */
function handleInfo(row) {
  reset();
  const _courseId = row.courseId || ids.value
  // getCourse(_courseId).then(response => {
  //   form.value = response.data;
  //   infoOpen.value = true;
  // });
  form.value = row;
  infoOpen.value = true;
}

/** 修改按钮操作 *

/** 提交按钮 */
function submitForm() {
  proxy.$refs["courseRef"].validate(valid => {
    if (valid) {
      if (form.value.courseId != null) {
        updateCourse(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCourse(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('course/course/export', {
    ...queryParams.value
  }, `course_${new Date().getTime()}.xlsx`)
}

const isStudent = ref(false); // 假设从API获取用户角色


// 判断用户是否为学生
const checkUserRole = async () => {
  // 这里假设通过API获取用户角色信息
  const roleResponse = await getUserProfile();
  isStudent.value = roleResponse.roleGroup === '学生';
};
checkUserRole()
// 检查课程是否已被选择
const isCourseSelected = (courseId) => {
  return selectedCourseList.value.includes(courseId);
};

// 选课
getList()
// onBeforeRouteUpdate(to=>getList())
</script>
