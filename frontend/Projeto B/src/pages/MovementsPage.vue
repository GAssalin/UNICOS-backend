<template>
  <section class="module-page">
    <form class="movement-form" @submit.prevent="saveMovement">
      <div class="form-header"><div><p class="eyebrow">Estoque</p><h2>Nova movimentação</h2><p>Registre entradas e saídas em uma única tela.</p></div></div>
      <div class="form-grid">
        <label>Tipo<select v-model="form.tipo"><option value="entrada">Entrada</option><option value="saida">Saída</option></select></label>
        <label>Produto<select v-model="form.produtoId" required><option value="" disabled>Selecione...</option><option v-for="p in productsWithStock" :key="p.id" :value="p.id">{{ p.codigo }} - {{ p.nome }} (saldo: {{ p.saldo }})</option></select></label>
        <label>Quantidade<input v-model.number="form.quantidade" type="number" min="0.01" step="0.01" required /></label>
        <label>Data<input v-model="form.data" type="date" required /></label>
        <label>Fornecedor<input v-model="form.fornecedor" type="text" placeholder="Obrigatório só em entrada" /></label>
        <label>Destino<input v-model="form.destino" type="text" placeholder="Obrigatório só em saída" /></label>
        <label class="wide">Observação<input v-model="form.observacao" type="text" placeholder="Ex: compra, venda, ajuste, produção..." /></label>
      </div>
      <div class="form-actions"><button class="secondary-action" type="button" @click="resetForm">Limpar</button><button class="primary-action" type="submit">Salvar movimentação</button></div>
    </form>

    <div class="results-card">
      <div class="results-header"><div><h2>Histórico de movimentações</h2><p>{{ movementsDetailed.length }} registro(s)</p></div></div>
      <div class="table-wrap"><table class="data-table"><thead><tr><th>Data</th><th>Tipo</th><th>Código</th><th>Produto</th><th>Qtd.</th><th>Fornecedor/Destino</th><th>Observação</th><th>Ações</th></tr></thead><tbody><tr v-for="movement in movementsDetailed" :key="movement.id"><td>{{ formatDate(movement.data) }}</td><td><span :class="['tag', movement.tipo === 'entrada' ? 'tag-in' : 'tag-out']">{{ movement.tipo }}</span></td><td>{{ movement.codigo }}</td><td><strong>{{ movement.produto }}</strong></td><td>{{ movement.quantidade }}</td><td>{{ movement.tipo === 'entrada' ? (movement.fornecedor || '-') : (movement.destino || '-') }}</td><td>{{ movement.observacao || '-' }}</td><td><button class="mini-action danger-text" @click="removeMovement(movement.id)">Excluir</button></td></tr></tbody></table></div>
    </div>
  </section>
</template>

<script setup>
import { reactive } from 'vue'
import { useInventory } from '../composables/useInventory'
const { productsWithStock, movementsDetailed, addMovement, deleteMovement } = useInventory()
const blank = () => ({ tipo: 'entrada', produtoId: '', quantidade: 1, data: new Date().toISOString().slice(0,10), fornecedor: '', destino: '', observacao: '' })
const form = reactive(blank())
function resetForm(){ Object.assign(form, blank()) }
function saveMovement(){ addMovement(form); resetForm() }
function removeMovement(id){ if(confirm('Deseja excluir esta movimentação?')) deleteMovement(id) }
function formatDate(value){ return value ? new Date(`${value}T00:00:00`).toLocaleDateString('pt-BR') : '-' }
</script>

<style scoped>
.module-page{display:flex;flex-direction:column;gap:20px}.movement-form,.results-card{background:#fff;border:1px solid #dce5ef;border-radius:20px;padding:24px;box-shadow:var(--panel-shadow)}.eyebrow{margin:0;color:#5c6b7a;text-transform:uppercase;letter-spacing:.08em;font-size:12px}h2{margin:6px 0}.form-header p,.results-header p{margin:0;color:#5c6b7a}.form-grid{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:16px;margin-top:18px}.form-grid label{display:flex;flex-direction:column;gap:8px;font-weight:700}.form-grid .wide{grid-column:1/-1}.form-grid input,.form-grid select{border:1px solid #dce5ef;border-radius:14px;padding:13px 14px;outline:none;background:#fff}.form-actions{display:flex;justify-content:flex-end;gap:12px;margin-top:18px}.primary-action,.secondary-action,.mini-action{border:0;border-radius:14px;padding:13px 18px;font-weight:800;cursor:pointer}.primary-action{background:linear-gradient(135deg,#1d5be3,#0f44bd);color:#fff}.secondary-action{background:#eef4fb;color:#123466}.table-wrap{overflow-x:auto}.data-table{width:100%;border-collapse:collapse;min-width:980px}.data-table th,.data-table td{text-align:left;padding:14px 12px;border-bottom:1px solid #eef2f6}.data-table th{font-size:12px;color:#5c6b7a;text-transform:uppercase}.tag{border-radius:999px;padding:5px 10px;font-weight:800;text-transform:capitalize}.tag-in{background:#e8f7ef;color:#14763d}.tag-out{background:#fff0ef;color:#bd2f24}.mini-action{padding:8px 10px;background:#eef4fb}.danger-text{color:#bd2f24}@media(max-width:780px){.form-grid{grid-template-columns:1fr}.form-actions{flex-direction:column}}
</style>
