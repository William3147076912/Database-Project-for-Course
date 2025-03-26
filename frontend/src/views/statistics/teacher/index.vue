<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="教师名称" prop="name">
        <el-input
            v-model="queryParams.userName"
            placeholder="请输入教师名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程数量" prop="subject">
        <el-input
            v-model="queryParams.courseCount"
            placeholder="请输入课程数量"
            clearable
            @keyup.enter="handleQuery"
        />
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
        <el-form-item label="教师数量：" prop="name">{{total}}</el-form-item>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="teacherList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="教师id" align="center" prop="userId"/>
      <el-table-column label="教师名称" align="center" prop="userName"/>
      <el-table-column label="课程数量" align="center" prop="courseCount" show-overflow-tooltip/>
      <el-table-column label="教学质量" align="center" prop="teachingQuality"/>
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
    <el-dialog title="教师信息"  v-model="infoOpen" width="800px" append-to-body>
      <!--使用el-descriptions组件以卡片形式展示信息，更简洁-->
      <el-descriptions :column="1" border>
        <el-descriptions-item label="教师姓名">{{ form.userName }}</el-descriptions-item>
        <el-descriptions-item label="课程数量">{{ form.courseCount }}</el-descriptions-item>
        <el-descriptions-item label="教学质量">{{ form.teachingQuality }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup name="Course">
import {listTeacher}  from "@/api/statistics/teacher.js";
const {proxy} = getCurrentInstance();
const {course_status} = proxy.useDict('course_status');

const teacherList = ref([]);
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
    userName: null,
    courses:null,
    courseCount:null,
    teachingQuality:null
  },
  rules: {
    userName: [
      {required: true, message: "教师名字不能为空", trigger: "blur"}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);

async function getList() {
  loading.value = true;
  await listTeacher(queryParams.value).then(response => {
    teacherList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
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
  form.value=row;
  infoOpen.value = true;
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download('course/course/export', {
    ...queryParams.value
  }, `course_${new Date().getTime()}.xlsx`)
}

getList()
// onBeforeRouteUpdate(to=>getList())
</script>
