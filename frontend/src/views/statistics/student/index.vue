<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学生姓名" prop="name">
        <el-input
            v-model="queryParams.userName"
            placeholder="请输入学生姓名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="课程数量" prop="subject">-->
<!--        <el-input-->
<!--            v-model="queryParams.courseCount"-->
<!--            placeholder="请输入课程数量"-->
<!--            clearable-->
<!--            @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
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
        <el-form-item label="学生数量：" prop="name">{{total}}</el-form-item>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="学生id" align="center" prop="userId"/>
      <el-table-column label="学生姓名" align="center" prop="userName"/>
      <el-table-column
          label="学习时长"
          align="center"
          show-overflow-tooltip
      >
        <template #default="scope">  <!-- 推荐使用解构赋值 -->
          {{ formattedTime(scope.row.learningTime) }}
        </template>
      </el-table-column>
<!--      <el-table-column label="成绩" align="center" prop="score"/>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Search" @click="handleInfo(scope.row)"
                     v-hasPermi="['course:course:list']">查看成绩
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
<!--  <p>{{form2.value}}</p>-->
    <!-- 查看课程管理详情对话框 -->
    <el-dialog title="学生信息"  v-model="infoOpen" width="800px" append-to-body>
      <!--使用el-descriptions组件以卡片形式展示信息，更简洁-->
      <el-descriptions :column="1" border>
        <el-descriptions-item label="学生姓名">{{ form.userName }}</el-descriptions-item>
        <el-descriptions-item label="学习时长">{{ form.learningTime }}秒</el-descriptions-item>
<!--        <el-descriptions-item label="成绩">{{ form.score }}</el-descriptions-item>-->
        <el-descriptions-item v-for="(item, index) in form2.value" :key="index" :label="item.title">
          {{ item.score }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup name="Course">
import {listStudent} from "@/api/statistics/student.js";
import {computed} from "vue";
import {listScore} from "../../../api/statistics/student.js";

const {proxy} = getCurrentInstance();
const {course_status} = proxy.useDict('course_status');

const studentList = ref([]);
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
    learningTime: null,
    score: null
  },
  rules: {
    userName: [
      {required: true, message: "学生名字不能为空", trigger: "blur"}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);
const form2 = reactive([])
async function getList() {
  loading.value = true;
  await listStudent(queryParams.value).then(response => {
    studentList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
function formattedTime(learningTime){
  const hours = Math.floor(learningTime / 3600);
  const minutes = Math.floor((learningTime%3600) / 60);
  const seconds = learningTime % 60;
  return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
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
function reset() {
  form2.value = {
    title:null,
    score:null
  };
}

/* 查看详情操作 */
function handleInfo(row) {
  // reset();
  listScore({studentId:row.userId}).then(
      response=>{
        form2.value=response.rows;
        infoOpen.value = true;
      }
  )
  form.value=row;
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
