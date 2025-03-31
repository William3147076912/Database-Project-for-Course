<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input
            v-model="queryParams.courseName"
            placeholder="请输入课程名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="learningList" @selection-change="handleSelectionChange">
      <el-table-column label="课程名称" align="center" prop="courseName"/>
      <el-table-column label="总共作业数" align="center" prop="totalAssignments"/>
      <el-table-column label="已完成作业数" align="center" prop="submittedAssignments"/>
      <el-table-column label="学习计划" align="center">
        <template #default="scope">
          <el-button @click="openPlanForm(scope.row)">查看/修改</el-button>
        </template>
      </el-table-column>
      <el-table-column label="学习进度" align="center">
        <template #default="scope">
          <span>{{ calculateProgress(scope.row) }}%</span>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <el-dialog :visible.sync="planFormVisible" title="学习计划">
      <template #content>
        <el-form :model="planFormData" ref="planFormRef">
          <el-form-item label="学习计划" prop="plan">
            <el-input v-model="planFormData.plan"/>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button @click="planFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPlanForm">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Progress">
import { ref, reactive, onMounted } from 'vue';
import { ElForm, ElFormItem, ElInput, ElButton, ElTable, ElTableColumn, ElPagination, ElDialog } from 'element-plus';
import axios from 'axios';
import useUserStore from "@/store/modules/user.js";
import request from "@/utils/request.js";

// 数据列表
const learningList = ref([]);
// 加载状态
const loading = ref(true);
// 搜索栏显示状态
const showSearch = ref(true);
// 选中项的ID数组
const ids = ref([]);
// 是否单选
const single = ref(true);
// 是否多选
const multiple = ref(true);
// 总数据量
const total = ref(0);
// 学习计划表单是否可见
const planFormVisible = ref(false);
// 学习计划表单数据
const planFormData = ref({});
// 学习计划表单引用
const planFormRef = ref(null);

// 假设这里的学生ID是固定的，实际应用中你可能需要从登录信息等获取
const studentId = useUserStore().id;

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  courseName: null
});

// 计算学习进度的方法
const calculateProgress = (row) => {
  const { totalAssignments, submittedAssignments } = row;
  if (totalAssignments === 0) {
    return 0;
  }
  return ((submittedAssignments / totalAssignments) * 100).toFixed(2);
};

// 获取数据列表
async function getList() {
  loading.value = true;
  try {
    const response = await request({
      url: `/progress/listProgress/${studentId}`,
      method: 'get',
      params: queryParams
    });
    learningList.value = response.rows;
    total.value = response.total; // 假设接口返回了 total 字段表示总数据量
  } catch (error) {
    console.error('获取数据失败', error);
  } finally {
    loading.value = false;
  }
}

// 搜索按钮点击事件
function handleQuery() {
  queryParams.pageNum = 1;
  getList();
}

// 重置按钮点击事件
function resetQuery() {
  Object.keys(queryParams).forEach(key => {
    queryParams[key] = null;
  });
  queryParams.pageNum = 1;
  queryParams.pageSize = 10;
  getList();
}

// 表格选中项变化事件
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.courseId);
  single.value = selection.length!== 1;
  multiple.value =!selection.length;
};

// 分页大小变化事件
function handleSizeChange(newSize) {
  queryParams.pageSize = newSize;
  getList();
}

// 分页当前页变化事件
function handleCurrentChange(newPage) {
  queryParams.pageNum = newPage;
  getList();
}

// 打开学习计划表单
function openPlanForm(row) {
  planFormData.value = { ...row };
  planFormVisible.value = true;
}

// 提交学习计划表单
async function submitPlanForm() {
  try {
    const response = await axios.post('/progress/updatePlan', {
      studentId: planFormData.value.studentId,
      courseId: planFormData.value.courseId,
      plan: planFormData.value.plan
    });
    if (response.data) {
      console.log('学习计划更新成功');
      alert('学习计划更新成功');
      planFormVisible.value = false;
      getList();
    } else {
      console.log('学习计划更新失败');
      alert('学习计划更新失败');
    }
  } catch (error) {
    console.error('更新学习计划失败', error);
  }
}

// 页面挂载时获取数据
onMounted(() => {
  getList();
});
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style>