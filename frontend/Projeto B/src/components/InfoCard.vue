<template>
  <article class="card">
    <p class="label">{{ label }}</p>
    <strong class="value">{{ value }}</strong>
    <p v-if="description" class="description">{{ description }}</p>
  </article>
</template>

<script setup>
defineProps({
  label: {
    type: String,
    required: true
  },
  value: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  }
})
</script>

<style scoped>
.card {
  background: var(--panel-bg, #ffffff);
  border: 1px solid var(--panel-border, #dce5ef);
  border-radius: 18px;
  padding: 18px;
  box-shadow: var(--panel-shadow, 0 10px 28px rgba(11, 34, 53, 0.06));
}

.label,
.description {
  margin: 0;
  color: var(--muted-text, #5c6b7a);
}

.value {
  display: block;
  margin: 10px 0 6px;
  font-size: 28px;
  color: var(--heading-text, #1c2a39);
}
</style>
