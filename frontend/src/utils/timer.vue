<template>
  <div>
    <span>{{ formattedTime }}</span>
    <el-button @click="startTimer" :disabled="isRunning">开始</el-button>
  <el-button @click="stopTimer" :disabled="!isRunning">停止</el-button>
</div>
</template>

<script setup>
  import {ref, computed} from 'vue';

  const isRunning = ref(false);
  const startTime = ref(null);
  const elapsedTime = ref(0);

  const formattedTime = computed(() => {
  const totalSeconds = Math.floor(elapsedTime.value / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;
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
</script>
