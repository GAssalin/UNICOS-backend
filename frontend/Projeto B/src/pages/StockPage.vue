<template>
  <section class="module-page">
    <ModuleSearch v-model="search" field-id="stock-search" placeholder="Buscar produto no estoque" />
    <div class="results-card">
      <div class="results-header"><div><p class="eyebrow">Estoque atual</p><h2>Saldo por produto</h2><p>Os saldos são calculados automaticamente pelas movimentações.</p></div></div>
      <div class="table-wrap"><table class="data-table"><thead><tr><th>Código</th><th>Produto</th><th>Categoria</th><th>Fornecedor</th><th>Saldo</th><th>Mínimo</th><th>Valor em estoque</th><th>Status</th></tr></thead><tbody><tr v-for="product in filteredProducts" :key="product.id"><td>{{ product.codigo }}</td><td><strong>{{ product.nome }}</strong></td><td>{{ product.categoria || '-' }}</td><td>{{ product.fornecedor || '-' }}</td><td>{{ product.saldo }} {{ product.unidade }}</td><td>{{ product.estoqueMinimo }} {{ product.unidade }}</td><td>{{ currency(product.valorEstoque) }}</td><td><span :class="['stock-pill', product.estoqueBaixo ? 'danger' : 'ok']">{{ product.estoqueBaixo ? 'Baixo' : 'OK' }}</span></td></tr></tbody></table></div>
    </div>
  </section>
</template>
<script setup>
import { computed, ref } from 'vue'
import ModuleSearch from '../components/ModuleSearch.vue'
import { useInventory } from '../composables/useInventory'
const { productsWithStock } = useInventory()
const search = ref('')
const filteredProducts = computed(()=>{ const term=search.value.trim().toLowerCase(); if(!term)return productsWithStock.value; return productsWithStock.value.filter(p=>[p.codigo,p.nome,p.categoria,p.fornecedor].join(' ').toLowerCase().includes(term)) })
function currency(value){return Number(value||0).toLocaleString('pt-BR',{style:'currency',currency:'BRL'})}
</script>
<style scoped>.module-page{display:flex;flex-direction:column;gap:20px}.results-card{background:#fff;border:1px solid #dce5ef;border-radius:20px;padding:24px;box-shadow:var(--panel-shadow)}.eyebrow{margin:0;color:#5c6b7a;text-transform:uppercase;letter-spacing:.08em;font-size:12px}h2{margin:6px 0}.results-header p{margin:0;color:#5c6b7a}.table-wrap{overflow-x:auto}.data-table{width:100%;border-collapse:collapse;min-width:920px}.data-table th,.data-table td{text-align:left;padding:14px 12px;border-bottom:1px solid #eef2f6}.data-table th{font-size:12px;color:#5c6b7a;text-transform:uppercase}.stock-pill{display:inline-flex;border-radius:999px;padding:5px 10px;font-weight:800;font-size:12px}.stock-pill.ok{background:#e8f7ef;color:#14763d}.stock-pill.danger{background:#fff0ef;color:#bd2f24}</style>
