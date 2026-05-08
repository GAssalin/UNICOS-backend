<template>
  <section class="page">
    <div class="hero">
      <div class="hero-copy">
        <p class="hero-chip">Visão Geral</p>
        <h2>Bem-vindo de volta, <span>Administrador!</span> 👋</h2>
        <p class="description">Dashboard conectado aos produtos, estoque e movimentações salvas no navegador.</p>
      </div>
      <div class="hero-summary"><strong>{{ currency(totalStockValue) }}</strong><span>valor total em estoque</span></div>
    </div>

    <div class="stats">
      <InfoCard label="Produtos" :value="String(productsWithStock.length)" description="cadastrados" />
      <InfoCard label="Itens em estoque" :value="String(totalStock)" description="saldo total" />
      <InfoCard label="Entradas" :value="String(totalEntries)" description="quantidade movimentada" />
      <InfoCard label="Saídas" :value="String(totalOutputs)" description="quantidade movimentada" />
    </div>

    <div class="grid-main">
      <div class="panel panel-chart">
        <div class="panel-header"><h3>Entradas x Saídas</h3><span>últimas movimentações</span></div>
        <div class="bar-chart">
          <div class="bar-group"><span>Entradas</span><div class="bar-track"><div class="bar bar-in" :style="{ width: entryPercent + '%' }"></div></div><strong>{{ totalEntries }}</strong></div>
          <div class="bar-group"><span>Saídas</span><div class="bar-track"><div class="bar bar-out" :style="{ width: outputPercent + '%' }"></div></div><strong>{{ totalOutputs }}</strong></div>
        </div>
      </div>

      <div class="panel panel-chart">
        <div class="panel-header"><h3>Produtos por categoria</h3><span>{{ categoryChart.length }} categorias</span></div>
        <div class="category-chart">
          <div v-for="item in categoryChart" :key="item.label" class="category-row"><span>{{ item.label }}</span><div class="bar-track"><div class="bar bar-category" :style="{ width: item.percent + '%' }"></div></div><strong>{{ item.value }}</strong></div>
        </div>
      </div>
    </div>

    <div class="grid-main lower-grid">
      <div class="panel panel-list">
        <div class="panel-header"><h3>Produtos com estoque baixo</h3><RouterLink to="/estoque">Ver estoque</RouterLink></div>
        <ul v-if="lowStockProducts.length" class="stock-list">
          <li v-for="product in lowStockProducts" :key="product.id"><span>{{ product.nome }}</span><strong>{{ product.saldo }} {{ product.unidade }}</strong></li>
        </ul>
        <p v-else class="empty-state">Nenhum produto abaixo do estoque mínimo.</p>
      </div>

      <div class="panel panel-table">
        <div class="panel-header"><h3>Últimas movimentações</h3><RouterLink to="/movimentacoes">Ver todas</RouterLink></div>
        <table>
          <thead><tr><th>Data</th><th>Tipo</th><th>Produto</th><th>Qtd.</th></tr></thead>
          <tbody><tr v-for="movement in recentMovements" :key="movement.id"><td>{{ formatDate(movement.data) }}</td><td><span :class="['tag', movement.tipo === 'entrada' ? 'tag-in' : 'tag-out']">{{ movement.tipo }}</span></td><td>{{ movement.produto }}</td><td>{{ movement.quantidade }}</td></tr></tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import InfoCard from '../components/InfoCard.vue'
import { useInventory } from '../composables/useInventory'

const { productsWithStock, movementsDetailed } = useInventory()
const totalStock = computed(() => productsWithStock.value.reduce((sum, product) => sum + product.saldo, 0))
const totalStockValue = computed(() => productsWithStock.value.reduce((sum, product) => sum + product.valorEstoque, 0))
const totalEntries = computed(() => movementsDetailed.value.filter((m) => m.tipo === 'entrada').reduce((sum, m) => sum + m.quantidade, 0))
const totalOutputs = computed(() => movementsDetailed.value.filter((m) => m.tipo === 'saida').reduce((sum, m) => sum + m.quantidade, 0))
const maxMovement = computed(() => Math.max(totalEntries.value, totalOutputs.value, 1))
const entryPercent = computed(() => Math.round((totalEntries.value / maxMovement.value) * 100))
const outputPercent = computed(() => Math.round((totalOutputs.value / maxMovement.value) * 100))
const lowStockProducts = computed(() => productsWithStock.value.filter((product) => product.estoqueBaixo).slice(0, 6))
const recentMovements = computed(() => movementsDetailed.value.slice(0, 6))
const categoryChart = computed(() => {
  const map = new Map()
  productsWithStock.value.forEach((product) => map.set(product.categoria || 'Sem categoria', (map.get(product.categoria || 'Sem categoria') || 0) + 1))
  const max = Math.max(...map.values(), 1)
  return [...map.entries()].map(([label, value]) => ({ label, value, percent: Math.round((value / max) * 100) }))
})
function currency(value) { return Number(value || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) }
function formatDate(value) { return value ? new Date(`${value}T00:00:00`).toLocaleDateString('pt-BR') : '-' }
</script>

<style scoped>
.page{display:flex;flex-direction:column;gap:22px}.hero{display:flex;justify-content:space-between;gap:20px;align-items:center;background:linear-gradient(135deg,#062b73 0%,#0b4ea2 55%,#2d75ff 100%);border-radius:28px;padding:30px;color:#fff;box-shadow:0 20px 45px rgba(6,43,115,.22);overflow:hidden;position:relative}.hero:after{content:'';position:absolute;right:-60px;top:-80px;width:260px;height:260px;border-radius:50%;background:rgba(255,255,255,.12)}.hero-copy{position:relative;z-index:1}.hero-chip{display:inline-flex;background:rgba(255,255,255,.16);border:1px solid rgba(255,255,255,.2);border-radius:999px;padding:6px 12px;margin:0 0 12px;font-weight:800}.hero h2{margin:0;font-size:2rem}.hero h2 span{color:#dceaff}.description{max-width:660px;color:rgba(255,255,255,.82);margin:10px 0 0}.hero-summary{position:relative;z-index:1;background:rgba(255,255,255,.12);border:1px solid rgba(255,255,255,.18);border-radius:22px;padding:18px;min-width:220px}.hero-summary strong{display:block;font-size:1.6rem}.hero-summary span{color:rgba(255,255,255,.78)}.stats{display:grid;grid-template-columns:repeat(4,minmax(0,1fr));gap:18px}.grid-main{display:grid;grid-template-columns:1.35fr 1fr;gap:18px}.lower-grid{align-items:start}.panel{background:#fff;border:1px solid #dce5ef;border-radius:22px;padding:22px;box-shadow:var(--panel-shadow)}.panel-header{display:flex;justify-content:space-between;gap:12px;align-items:center;margin-bottom:18px}.panel-header h3{margin:0}.panel-header span,.panel-header a{color:#5c6b7a;text-decoration:none;font-weight:700}.bar-chart,.category-chart{display:flex;flex-direction:column;gap:16px}.bar-group,.category-row{display:grid;grid-template-columns:90px 1fr 50px;gap:12px;align-items:center}.bar-track{height:14px;background:#eef4fb;border-radius:999px;overflow:hidden}.bar{height:100%;border-radius:999px}.bar-in{background:linear-gradient(90deg,#23b26d,#68d391)}.bar-out{background:linear-gradient(90deg,#ef5a4f,#ff9d8f)}.bar-category{background:linear-gradient(90deg,#1d5be3,#6ba2ff)}.stock-list{list-style:none;padding:0;margin:0;display:flex;flex-direction:column;gap:12px}.stock-list li{display:flex;justify-content:space-between;gap:12px;border-bottom:1px solid #eef2f6;padding-bottom:12px}.stock-list strong{color:#bd2f24}.empty-state{color:#5c6b7a;margin:0}table{width:100%;border-collapse:collapse}th,td{text-align:left;padding:12px 10px;border-bottom:1px solid #eef2f6}th{font-size:12px;color:#5c6b7a;text-transform:uppercase}.tag{border-radius:999px;padding:5px 10px;font-weight:800;text-transform:capitalize}.tag-in{background:#e8f7ef;color:#14763d}.tag-out{background:#fff0ef;color:#bd2f24}@media(max-width:1100px){.stats,.grid-main{grid-template-columns:1fr 1fr}}@media(max-width:760px){.hero,.stats,.grid-main{grid-template-columns:1fr;flex-direction:column;align-items:stretch}.bar-group,.category-row{grid-template-columns:1fr}}
</style>
