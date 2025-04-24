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
            v-if="!useUserStore().roles.includes('student')"
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
            v-hasPermi="['resource:resource:edit']"
            v-if="!useUserStore().roles.includes('student')"
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
            v-hasPermi="['resource:resource:remove']"
            v-if="!useUserStore().roles.includes('student')"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['resource:resource:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="resourceList" @selection-change="handleSelectionChange">
      <!--<el-table-column type="selection" width="55" align="center"/>-->
      <!--<el-table-column label="资源ID" align="center" prop="resourceId" />-->
      <el-table-column label="文件名称" align="center" prop="resourceName" />
      <el-table-column label="所属课程" align="center" prop="name"/>
      <el-table-column label="上传资源的教师" align="center" prop="createdBy"/>
      <el-table-column label="资源类型" align="center" prop="resourceType">
        <template #default="scope">
          <dict-tag :options="resource_type" :value="scope.row.resourceType"/>
        </template>
      </el-table-column>
      <el-table-column label="上传时间" align="center" prop="uploadTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.uploadTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-if="!useUserStore().roles.includes('student')"
                     v-hasPermi="['resource:resource:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-if="!useUserStore().roles.includes('student')"
                     v-hasPermi="['resource:resource:remove']">删除
          </el-button>
          <el-button link type="primary" icon="download"
                     @click="()=>{handleDownload(scope.row);startTimer();}">下载
          </el-button>
          <!--<el-button link type="primary" icon="download"  v-if="scope.row.resourceType!=='Video'" @click="()=>{showResource(scope.row);}">在线学习-->
          <!--</el-button>-->
          <el-button link type="primary" icon="Search" v-if="scope.row.resourceType==='Video'"
                     @click="handleVideoPlay(scope.row)">在线播放
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

    <!--<p>{{learnTime.userId}}</p>-->
    <!--<p>{{learnTime.resourceId}}</p>-->
<!--    <p>{{learnTime.learnTime}}</p>-->
    <el-dialog
        :modal="false"
        title="视频播放"
        @close="handleDialogClose()"
        v-model="videoDialog"
        width="40%">
      <p>{{ videoName }}</p>
      <video :src="videoUrl"
             controls="controls"
             width="100%"
             @play="startTimer()"
             @pause="stopTimer()"
             @timeupdate="updateVideoCurrentTime"
             @loadedmetadata="onVideoLoadedMetadata"
             @ended="onVideoEnded()"
             ></video>
      <!--<p>{{formattedTime}}</p>-->
      <!--<p>{{totalSeconds}}</p>-->
    </el-dialog>
    <!-- 添加或修改教学资源对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="resourceRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件名称" prop="resourceName">
          <el-input v-model="form.resourceName" placeholder="请输入文件名称"/>
        </el-form-item>
        <el-form-item label="所属课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择所属课程">
            <el-option
                v-for="item in courseList"
                :key="item.courseId"
                :label="item.name"
                :value="item.courseId"
            ></el-option>
          </el-select>
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
        <!--<el-form-item label="上传时间" prop="uploadTime">-->
        <!--  <el-date-picker clearable-->
        <!--                  v-model="form.uploadTime"-->
        <!--                  type="date"-->
        <!--                  value-format="YYYY-MM-DD"-->
        <!--                  placeholder="请选择上传时间">-->
        <!--  </el-date-picker>-->
        <!--</el-form-item>-->
        <el-form-item label="上传文件">
          <file-upload v-model="form.filePath"/>
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
import {listCourse} from "@/api/course/course.js";
import {listResource, getResource, delResource, addResource, updateResource} from "@/api/resource/resource";
import useUserStore from "@/store/modules/user.js";
import FileUpload from "@/components/FileUpload/index.vue";
import download from "@/plugins/download.js";
import {ref, computed} from 'vue';
import {getVideo} from "@/api/resource/resource.js";
import {updateLearningTime} from "../../../api/resource/resource.js";

const {proxy} = getCurrentInstance();
const {resource_type} = proxy.useDict('resource_type');
const resourceList = ref([]);
const open = ref(false);
const videoDialog = ref(false);
const videoName = ref('');
const videoUrl = ref( '/resource/resource/video');
const videoId = ref('');
const currentResourceId=ref('');
const currentVideoDuration = ref(0);
const videoCurrentTime = ref(0);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const isRunning = ref(false);
const startTime = ref(null);
const elapsedTime = ref(0);
const totalSeconds = ref(0);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    courseId: null,
    name: null,
    uploaderId: null,
    createdBy: null,
    resourceName: null,
    resourceType: null,
    filePath: null
  },
  rules: {
    courseId: [
      {required: true, message: "资源所属课程不能为空", trigger: "change"}
    ],
    resourceName: [
      {required: true, message: "文件名称不能为空", trigger: "blur"}
    ],
    resourceType: [
      {required: true, message: "资源类型不能为空", trigger: "change"}
    ],
    uploadTime: [
      {required: true, message: "上传时间不能为空", trigger: "blur"}
    ]
  }
});
const {queryParams, form, rules} = toRefs(data);
const learnTime=reactive({
  studentId:useUserStore().userId,
  resourceId:null,
  learnTime:0,
  Finished:false
})
const courseList = ref([]);

function getCourseList() {
  listCourse().then(response => {
    courseList.value = response.rows.map(item => {
      return {
        courseId: item.courseId,
        name: item.name
      }
    });
    // console.log(JSON.stringify(courseList.value))
  });
}

getCourseList()

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

function handleVideoPlay(row) {
  videoDialog.value = true
  videoName.value = row.resourceName;
  videoId.value = row.resourceId;
  currentResourceId.value=row.resourceId;
  videoUrl.value = '/resource/resource/video/'+row.resourceId
  // 请求视频数据
  getVideo(videoUrl.value).then(response => {
    const videoBlob = new Blob([response], {type: 'video/mp4'}); // 假设视频类型为mp4
    videoUrl.value = URL.createObjectURL(videoBlob);
  }).catch(error => {
    console.error('获取视频失败:', error);
  });
}
function onVideoLoadedMetadata(event) {
  const video = event.target;
  console.log('视频时长:', video.duration);
  // 输出视频时长到控制台
  // 你可以在这里将视频时长赋值给某个响应式变量，以便在模板中使用
  currentVideoDuration.value=video.duration;
}
// const videoPlay=ref(false);
// function handleVideoClick() {
//   if(videoPlay===false)
//   {
//     startTimer();
//     videoPlay.value=true;
//   }else
//   {
//     stopTimer();
//     videoPlay.value=false;
//   }
//   console.log('视频被点击了');
//   // 在这里添加你想要执行的逻辑，比如播放视频
// }
function updateVideoCurrentTime(event) {
  videoCurrentTime.value = event.target.currentTime;

}
function onVideoEnded() {
  // 在这里添加你想要执行的逻辑，比如关闭对话框、显示提示信息等
  learnTime.Finished=true;
  alert("完成");
}
function handleDialogClose() {
  console.log('视频播放对话框已关闭');
  // 在这里添加你想要执行的逻辑，比如重置某些状态
  stopTimer(); // 停止计时器
  if(totalSeconds!==0)
  {
    learnTime.studentId=useUserStore().id;
    learnTime.learnTime=totalSeconds;
    learnTime.resourceId=currentResourceId.value;
    updateLearningTime(learnTime);
    learnTime.Finished=false;
    resetTimer();
  }
}


/** 提交按钮 */
function submitForm() {
  proxy.$refs["resourceRef"].validate(async valid => {
    if (valid) {
      if (form.value.resourceId != null) {
        // 删除旧文件

        await updateResource(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        form.value.uploaderId = useUserStore().id
        await addResource(form.value).then(response => {
          proxy.$modal.msgSuccess("上传成功");
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
  proxy.$modal.confirm('是否确认删除教学资源编号为"' + _resourceIds + '"的数据项？').then(function () {
    return delResource(_resourceIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 下载按钮操作 */
function handleDownload(row) {
  download.resource(row.filePath)
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('resource/resource/export', {
    ...queryParams.value
  }, `resource_${new Date().getTime()}.xlsx`)
}

getList();



const formattedTime = computed(() => {
  totalSeconds.value = Math.floor(elapsedTime.value / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds.value % 60;
  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
});

const startTimer = () => {
  isRunning.value = true;
  startTime.value = Date.now() - elapsedTime.value;
  requestAnimationFrame(updateTimer);
};

const stopTimer = () => {
  isRunning.value = false;
};

const updateTimer = () => {
  if (isRunning.value) {
    elapsedTime.value = Date.now() - startTime.value;
    requestAnimationFrame(updateTimer);
  }
};
const resetTimer = () => {
  totalSeconds.value= 0;
  elapsedTime.value = 0;
  startTime.value = Date.now();
};
</script>
