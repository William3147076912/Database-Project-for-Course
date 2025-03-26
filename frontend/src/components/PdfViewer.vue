<template>
  <div>
    <embed :src="resource.filePath" type="application/pdf" width="100%" height="600px" />
  </div>
</template>

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  resource: {
    type: Object,
    required: true
  }
});
</script>
