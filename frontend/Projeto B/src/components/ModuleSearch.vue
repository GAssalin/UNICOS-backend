<template>
  <div class="search-card">
    <label class="search-label" :for="fieldId">Buscar no módulo</label>
    <div class="search-input-wrapper">
      <input
        :id="fieldId"
        :value="modelValue"
        type="text"
        :placeholder="placeholder"
        @input="$emit('update:modelValue', $event.target.value)"
      />
    </div>
  </div>
</template>

<script setup>
defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: 'Digite para buscar...'
  },
  fieldId: {
    type: String,
    default: 'module-search'
  }
})

defineEmits(['update:modelValue'])
</script>

<style scoped>
.search-card {
  background: #ffffff;
  border: 1px solid #dce5ef;
  border-radius: 16px;
  padding: 20px 24px;
}

.search-label {
  display: block;
  margin-bottom: 8px;
  color: #5c6b7a;
  font-size: 13px;
  font-weight: 700;
}

.search-input-wrapper {
  display: flex;
}

input {
  width: 100%;
  border: 1px solid #ccd7e2;
  border-radius: 10px;
  padding: 12px 14px;
  outline: none;
}

input:focus {
  border-color: #0b2235;
}
</style>
