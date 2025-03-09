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
      <!--<el-form-item label="审核课程的管理员ID" prop="reviewerId">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.reviewerId"-->
      <!--    placeholder="请输入审核课程的管理员ID"-->
      <!--    clearable-->
      <!--    @keyup.enter="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="课程创建者ID" prop="creatorId">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.creatorId"-->
      <!--    placeholder="请输入课程创建者ID"-->
      <!--    clearable-->
      <!--    @keyup.enter="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="课程创建时间" prop="creationTime">-->
      <!--  <el-date-picker clearable-->
      <!--    v-model="queryParams.creationTime"-->
      <!--    type="date"-->
      <!--    value-format="YYYY-MM-DD"-->
      <!--    placeholder="请选择课程创建时间">-->
      <!--  </el-date-picker>-->
      <!--</el-form-item>-->
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
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['course:course:add']"
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
            v-hasPermi="['course:course:edit']"
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
            v-hasPermi="['course:course:remove']"
        >删除
        </el-button>
      </el-col>
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
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" v-if="!isStudent"/>
      <el-table-column label="课程id" align="center" prop="courseId"/>
      <el-table-column label="课程名称" align="center" prop="name"/>
      <el-table-column label="课程简介" align="center" prop="description" show-overflow-tooltip/>
      <el-table-column label="课程目标" align="center" prop="objectives" show-overflow-tooltip/>
      <el-table-column label="课程内容" align="center" show-overflow-tooltip>
        <template #default="scope">
          <div v-html="scope.row.content"></div>
        </template>
      </el-table-column>
      <el-table-column label="课程大纲" align="center" prop="syllabus" show-overflow-tooltip/>
      <el-table-column label="课程状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="course_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="所属学科" align="center" prop="subject"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Search" @click="handleInfo(scope.row)"
                     v-hasPermi="['course:course:list']">查看
          </el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['course:course:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['course:course:remove']">删除
          </el-button>
          <!-- 判断用户身份和选课状态 -->
          <el-button v-if="isStudent && !isCourseSelected(scope.row.courseId)"
                     link type="success" icon="Check" @click="selectCourse(scope.row)">选课
          </el-button>
          <el-button v-else-if="isStudent && isCourseSelected(scope.row.courseId)"
                     link type="warning" icon="Close" @click="unselectCourse(scope.row)">退选
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="课程id" align="center" prop="courseId" />
          <el-table-column label="课程标题" align="center" prop="name" />
          <el-table-column label="课程简要描述" align="center" prop="description" />
          <el-table-column label="课程学习目标" align="center" prop="objectives" />
          <el-table-column label="详细教学内容" align="center" prop="content" />
          <el-table-column label="课程大纲" align="center" prop="syllabus" />
          <el-table-column label="课程状态：草稿、审核中、已上线、已结束" align="center" prop="status">
            <template #default="scope">
              <dict-tag :options="course_status" :value="scope.row.status"/>
            </template>
          </el-table-column>
          <el-table-column label="审核课程的管理员ID" align="center" prop="reviewerId" />
          <el-table-column label="课程创建者ID" align="center" prop="creatorId" />
          <el-table-column label="课程创建时间" align="center" prop="creationTime" width="180">
            <template #default="scope">
              <span>{{ parseTime(scope.row.creationTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="课程所属学科" align="center" prop="subject" />
          <el-table-column label="课程类型" align="center" prop="courseType" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['course:course:edit']">修改</el-button>
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['course:course:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>-->

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改课程管理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="courseRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入课程名称"/>
        </el-form-item>
        <el-form-item label="课程简介" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="课程目标" prop="objectives">
          <el-input v-model="form.objectives" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="课程内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="课程大纲" prop="syllabus">
          <el-input v-model="form.syllabus" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="课程状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择课程状态">
            <el-option
                v-for="dict in course_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属学科" prop="subject">
          <el-input v-model="form.subject" placeholder="请输入所属学科"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看课程管理详情对话框 -->
    <el-dialog title="课程信息" :modal="form" v-model="infoOpen" width="800px" append-to-body>
      <!--使用el-descriptions组件以卡片形式展示信息，更简洁-->
      <el-descriptions :column="1" border>
        <el-descriptions-item label="课程名称">{{ form.name }}</el-descriptions-item>
        <el-descriptions-item label="课程简介">{{ form.description }}</el-descriptions-item>
        <el-descriptions-item label="课程目标">{{ form.objectives }}</el-descriptions-item>
        <el-descriptions-item label="课程内容">
          <div v-html="form.content"></div>
        </el-descriptions-item>
        <el-descriptions-item label="课程大纲">{{ form.syllabus }}</el-descriptions-item>
        <el-descriptions-item label="所属学科">{{ form.subject }}</el-descriptions-item>
        <el-descriptions-item label="课程状态">
          <dict-tag :options="course_status" :value="form.status"/>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

  </div>
</template>

<script setup name="Course">
import {listCourse, getCourse, delCourse, addCourse, updateCourse} from "@/api/course/course";
import {getUser, getUserProfile} from "@/api/system/user.js";

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
    courseType: null
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

/** 查询课程管理列表 */
function getList() {
  loading.value = true;
  listCourse(queryParams.value).then(response => {
    courseList.value = response.rows;
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
    courseType: null
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
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加课程管理";
}

/* 查看详情操作 */
function handleInfo(row) {
  reset();
  const _courseId = row.courseId || ids.value
  getCourse(_courseId).then(response => {
    form.value = response.data;
    infoOpen.value = true;
  });
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _courseId = row.courseId || ids.value
  getCourse(_courseId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改课程管理";
  });
}

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

/** 删除按钮操作 */
function handleDelete(row) {
  const _courseIds = row.courseId || ids.value;
  proxy.$modal.confirm('是否确认删除课程管理编号为"' + _courseIds + '"的数据项？').then(function () {
    return delCourse(_courseIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('course/course/export', {
    ...queryParams.value
  }, `course_${new Date().getTime()}.xlsx`)
}
const userCourses = ref([]); // 存储用户已选课程的ID列表
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
  return userCourses.value.includes(courseId);
};

// 选课
const selectCourse =  (course) => {
  if (isCourseSelected(course.courseId)) {
    proxy.$modal.msgError("您已选过该课程");
    return;
  }
  proxy.$modal.confirm('是否确认要选"' + course.name + '？').then(function () {
    return delCourse(_courseIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("选课成功");
    userCourses.value.push(course.courseId);
  })
};

// 退选
const unselectCourse =  (course) => {
  if (!isCourseSelected(course.courseId)) {
    proxy.$modal.msgError("您未选过该课程");
    return;
  }
  proxy.$modal.confirm('是否确认退选"' + course.name + '？').then(function () {
    return delCourse(_courseIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("退选成功");
    userCourses.value = userCourses.value.filter(id => id !== course.courseId);
  })
};

getList();
</script>
