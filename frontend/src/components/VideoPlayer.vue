<template>
  <div class="release_wrap">
    <div class="release_title">播 放 视 频</div>
    <el-card class="release_card">
      <el-button type="primary" round icon="el-icon-arrow-left" style="margin-bottom: 40px"
                 @click="jump_home">返回首页</el-button>

      <el-table stripe :data="tableData" style="width: 100%" height="600px">
        <el-table-column prop="videoName" label="视频名称" min-width="280">
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="mini" type="primary" @click="playVideo(scope.$index, scope.row)">播放</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :modal="false" title="视频播放" v-model="dialogVisible" width="40%">
      <p class="video_title">{{ videoName }}</p>
      <video :src="`${videoUrl}/${videoId}`" controls="controls" width="100%" @canplay="getVidDur()" id="myvideo"></video>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import IPconfig from "./IPconfig";
const baseUrl = IPconfig.baseUrl;

export default {
  name: 'VideoPlayAll',
  setup() {
    const router = useRouter();
    const store = useStore();

    const title = ref("");
    const videolist = ref("");
    const tableData = ref([]);
    const dialogVisible = ref(false);
    const videoId = ref(0);
    const videoName = ref('');
    const videoUrl = `${baseUrl}/SelectVideo/policemen`;

    const getVidDur = () => {
      const videoTime = document.getElementById("myvideo");
      console.log(videoTime.duration); //获取视频时长
      console.log(videoTime.currentTime); //获取视频当前播放时间
    };

    const jump_home = () => {
      router.replace('/');
    };

    const getVideoInfo = () => {
      fetch(baseUrl + "/SelectVideo/table")
          .then(response => response.json())
          .then(res => {
            console.log('res:', res);
            tableData.value = res;
          });
    };

    const playVideo = (i, val) => {
      dialogVisible.value = true;
      videoName.value = val.videoName;
      videoId.value = val.id;
      console.log(i, val);
    };

    onMounted(() => {
      getVideoInfo();
    });

    return {
      title,
      videolist,
      tableData,
      dialogVisible,
      videoId,
      videoName,
      videoUrl,
      getVidDur,
      jump_home,
      playVideo,
    };
  },
};
</script>

<style></style>