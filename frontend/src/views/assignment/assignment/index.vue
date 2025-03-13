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
      <el-form-item label="评估类型" prop="assignmentType">
        <el-select v-model="queryParams.assignmentType" placeholder="请选择评估类型" clearable>
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
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['assignment:assignment:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['assignment:assignment:remove']"
        >删除
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
      <el-table-column type="selection" width="55" align="center" v-if="useUserStore().roles.includes('teacher')"/>
      <!--<el-table-column label="任务ID" align="center" prop="assignmentId" />-->
      <el-table-column label="任务标题" align="center" prop="title"/>
      <el-table-column label="详细说明" align="center" prop="description"/>
      <el-table-column label="评估类型" align="center" prop="assignmentType">
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
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['assignment:assignment:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['assignment:assignment:remove']">删除
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

    <!-- 添加或修改任务对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="assignmentRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="任务标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入任务标题"/>
        </el-form-item>
        <el-form-item label="详细说明" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="评估类型" prop="assignmentType">
          <el-select v-model="form.assignmentType" placeholder="请选择评估类型">
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
        <!--<el-form-item label="创建时间" prop="creationTime">-->
        <!--  <el-date-picker clearable-->
        <!--                  v-model="form.creationTime"-->
        <!--                  type="date"-->
        <!--                  value-format="YYYY-MM-DD"-->
        <!--                  placeholder="请选择任务创建时间">-->
        <!--  </el-date-picker>-->
        <!--</el-form-item>-->
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

<script setup name="Assignment">
import {
  listAssignment,
  getAssignment,
  delAssignment,
  addAssignment,
  updateAssignment
} from "@/api/assignment/assignment";
import useUserStore from "@/store/modules/user.js";
import {listCourse} from "@/api/course/course.js";
import {listUser} from "@/api/system/user.js";

const {proxy} = getCurrentInstance();
const {assignment_type} = proxy.useDict('assignment_type');

const assignmentList = ref([]);
const courseList = ref([]);
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
    creatorId: null,
    assignmentType: null,
    title: null,
  },
  rules: {
    courseId: [
      {required: true, message: "关联的课程不能为空", trigger: "blur"}
    ],
    creatorId: [
      {required: true, message: "创建任务的教师不能为空", trigger: "blur"}
    ],
    assignmentType: [
      {required: true, message: "评估类型不能为空", trigger: "change"}
    ],
    title: [
      {required: true, message: "任务标题不能为空", trigger: "blur"}
    ],
    fullScore: [
      {required: true, message: "任务的满分分值不能为空", trigger: "blur"}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);
const teacherMap = ref([])

/** 查询任务列表 */
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
    courseName: null,
    creatorId: null,
    creatorName: null,
    assignmentType: null,
    title: null,
    description: null,
    deadline: null,
    fullScore: null,
    creationTime: null
  };
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
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加任务";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _assignmentId = row.assignmentId || ids.value
  getAssignment(_assignmentId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改任务";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["assignmentRef"].validate(valid => {
    if (valid) {
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
  proxy.$modal.confirm('是否确认删除任务编号为"' + _assignmentIds + '"的数据项？').then(function () {
    return delAssignment(_assignmentIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('assignment/assignment/export', {
    ...queryParams.value
  }, `assignment_${new Date().getTime()}.xlsx`)
}

getList();
</script>
