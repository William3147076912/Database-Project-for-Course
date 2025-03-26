<template>
  <div>
    <iframe :src="`https://view.officeapps.live.com/op/embed.aspx?src=${encodeURIComponent(resource.filePath)}`" width="100%" height="600px"></iframe>
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
