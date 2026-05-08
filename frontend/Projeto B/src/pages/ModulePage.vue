<template>
  <section class="module-page">
    <div class="module-toolbar">
      <ModuleSearch
        v-model="search"
        :field-id="`${normalizedTitle}-search`"
        :placeholder="`Buscar em ${title}`"
      />

      <button v-if="isProdutos" class="primary-action" type="button" @click="showProductForm = true">
        + Cadastrar produto
      </button>
    </div>

    <div class="panel">
      <p class="eyebrow">Módulo</p>
      <h2>{{ title }}</h2>
      <p>
        Área preparada para receber cadastros, relatórios, consultas e ações específicas de {{ title.toLowerCase() }}.
      </p>
    </div>

    <form v-if="isProdutos && showProductForm" class="product-form" @submit.prevent="addProduct">
      <div class="form-header">
        <div>
          <p class="eyebrow">Novo cadastro</p>
          <h3>Cadastrar produto</h3>
        </div>
        <button class="ghost-action" type="button" @click="showProductForm = false">Fechar</button>
      </div>

      <div class="form-grid">
        <label>
          Nome do produto
          <input v-model="newProduct.nome" type="text" placeholder="Ex: Parafuso sextavado" required />
        </label>

        <label>
          Código
          <input v-model="newProduct.codigo" type="text" placeholder="Ex: PRD-001" required />
        </label>

        <label>
          Categoria
          <input v-model="newProduct.categoria" type="text" placeholder="Ex: Ferragens" />
        </label>

        <label>
          Quantidade inicial
          <input v-model.number="newProduct.quantidade" type="number" min="0" placeholder="0" />
        </label>
      </div>

      <div class="form-actions">
        <button class="secondary-action" type="button" @click="clearProductForm">Limpar</button>
        <button class="primary-action" type="submit">Salvar produto</button>
      </div>
    </form>

    <div v-if="isProdutos" class="results-card">
      <div class="results-header">
        <h3>Produtos cadastrados</h3>
        <span>{{ filteredProducts.length }} produto(s)</span>
      </div>

      <div v-if="filteredProducts.length" class="product-table-wrap">
        <table class="product-table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Produto</th>
              <th>Categoria</th>
              <th>Quantidade</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in filteredProducts" :key="product.codigo">
              <td>{{ product.codigo }}</td>
              <td>{{ product.nome }}</td>
              <td>{{ product.categoria || '-' }}</td>
              <td>{{ product.quantidade }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <p v-else class="empty-state">Nenhum produto encontrado para a busca "{{ search }}".</p>
    </div>

    <div v-else class="results-card">
      <div class="results-header">
        <h3>Itens do módulo</h3>
        <span>{{ filteredItems.length }} resultado(s)</span>
      </div>

      <ul v-if="filteredItems.length" class="results-list">
        <li v-for="item in filteredItems" :key="item">{{ item }}</li>
      </ul>

      <p v-else class="empty-state">Nenhum item encontrado para a busca "{{ search }}".</p>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import ModuleSearch from '../components/ModuleSearch.vue'

const props = defineProps({
  title: {
    type: String,
    required: true
  }
})

const search = ref('')
const showProductForm = ref(false)
const products = ref([
  { codigo: 'PRD-001', nome: 'Parafuso Sextavado 1/4”', categoria: 'Ferragens', quantidade: 12 },
  { codigo: 'PRD-002', nome: 'Arruela Lisa 1/4”', categoria: 'Ferragens', quantidade: 15 },
  { codigo: 'PRD-003', nome: 'Porca Sextavada 1/4”', categoria: 'Ferragens', quantidade: 18 }
])

const newProduct = reactive({
  nome: '',
  codigo: '',
  categoria: '',
  quantidade: 0
})

const moduleItems = {
  Clientes: ['Clientes ativos', 'Clientes inativos', 'Contratos vinculados', 'Histórico de atendimento'],
  Configurações: ['Usuários e permissões', 'Parâmetros do sistema', 'Integrações', 'Preferências gerais'],
  Compras: ['Pedidos de compra', 'Fornecedores homologados', 'Cotações pendentes', 'Contratos ativos'],
  Estoque: ['Saldo por produto', 'Movimentações de entrada', 'Movimentações de saída', 'Inventários em aberto'],
  Fornecedores: ['Fornecedores ativos', 'Cotações abertas', 'Histórico de compras', 'Documentos pendentes'],
  Logística: ['Romaneios', 'Entregas em rota', 'Conferência de recebimento', 'Controle de frota'],
  Produção: ['Ordens de produção', 'Apontamentos do turno', 'Consumo de matéria-prima', 'Paradas de máquina'],
  Qualidade: ['Não conformidades', 'Auditorias internas', 'Planos de ação', 'Indicadores de qualidade'],
  Relatórios: ['Relatório de estoque', 'Relatório de vendas', 'Relatório financeiro', 'Relatório de movimentações'],
  RH: ['Colaboradores ativos', 'Folha de pagamento', 'Solicitações de férias', 'Treinamentos obrigatórios'],
  Saídas: ['Saídas do mês', 'Baixas manuais', 'Vendas vinculadas', 'Histórico de expedição'],
  TI: ['Chamados abertos', 'Inventário de equipamentos', 'Acessos pendentes', 'Backups monitorados'],
  Usuários: ['Usuários ativos', 'Perfis de acesso', 'Convites pendentes', 'Logs de acesso'],
  Vendas: ['Pedidos faturados', 'Propostas em aberto', 'Metas mensais', 'Carteira de clientes']
}

const isProdutos = computed(() => props.title === 'Produtos')

const normalizedTitle = computed(() =>
  props.title
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
)

const filteredItems = computed(() => {
  const items = moduleItems[props.title] || []
  const term = search.value.trim().toLowerCase()

  if (!term) {
    return items
  }

  return items.filter((item) => item.toLowerCase().includes(term))
})

const filteredProducts = computed(() => {
  const term = search.value.trim().toLowerCase()

  if (!term) {
    return products.value
  }

  return products.value.filter((product) =>
    [product.codigo, product.nome, product.categoria]
      .join(' ')
      .toLowerCase()
      .includes(term)
  )
})

function clearProductForm() {
  newProduct.nome = ''
  newProduct.codigo = ''
  newProduct.categoria = ''
  newProduct.quantidade = 0
}

function addProduct() {
  products.value.unshift({
    codigo: newProduct.codigo.trim(),
    nome: newProduct.nome.trim(),
    categoria: newProduct.categoria.trim(),
    quantidade: Number(newProduct.quantidade || 0)
  })

  clearProductForm()
  showProductForm.value = false
}
</script>

<style scoped>
.module-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.module-toolbar {
  display: flex;
  gap: 14px;
  align-items: center;
}

.module-toolbar :deep(.module-search) {
  flex: 1;
}

.panel,
.results-card,
.product-form {
  background: var(--panel-bg, #ffffff);
  border: 1px solid var(--panel-border, #dce5ef);
  border-radius: 20px;
  padding: 24px;
  box-shadow: var(--panel-shadow, 0 10px 28px rgba(11, 34, 53, 0.06));
}

.eyebrow {
  margin: 0;
  color: var(--muted-text, #5c6b7a);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  font-size: 12px;
}

h2,
h3 {
  margin: 8px 0 12px;
}

p {
  margin: 0;
  color: var(--body-text, #435465);
}

.primary-action,
.secondary-action,
.ghost-action {
  border: 0;
  border-radius: 14px;
  padding: 13px 18px;
  font-weight: 800;
  cursor: pointer;
  transition: 0.2s ease;
  white-space: nowrap;
}

.primary-action {
  background: linear-gradient(135deg, #1d5be3, #0f44bd);
  color: #fff;
  box-shadow: 0 10px 22px rgba(29, 91, 227, 0.22);
}

.primary-action:hover {
  transform: translateY(-1px);
}

.secondary-action {
  background: #eef4fb;
  color: #123466;
}

.ghost-action {
  background: transparent;
  color: #1d5be3;
}

.form-header,
.results-header,
.form-actions {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
}

.form-header {
  margin-bottom: 18px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.form-grid label {
  display: flex;
  flex-direction: column;
  gap: 8px;
  color: var(--heading-text, #1c2a39);
  font-weight: 700;
}

.form-grid input {
  border: 1px solid #dce5ef;
  border-radius: 14px;
  padding: 13px 14px;
  font: inherit;
  outline: none;
}

.form-grid input:focus {
  border-color: #1d5be3;
  box-shadow: 0 0 0 3px rgba(29, 91, 227, 0.12);
}

.form-actions {
  justify-content: flex-end;
  margin-top: 18px;
}

.results-header {
  margin-bottom: 16px;
}

.results-header span {
  color: var(--muted-text, #5c6b7a);
  font-size: 13px;
}

.results-list {
  margin: 0;
  padding-left: 20px;
  color: var(--heading-text, #1c2a39);
}

.results-list li + li {
  margin-top: 10px;
}

.product-table-wrap {
  overflow-x: auto;
}

.product-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 640px;
}

.product-table th,
.product-table td {
  text-align: left;
  padding: 14px 12px;
  border-bottom: 1px solid #eef2f6;
}

.product-table th {
  color: #5c6b7a;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.product-table td {
  color: #1c2a39;
  font-weight: 600;
}

.empty-state {
  color: #8a1f1f;
}

@media (max-width: 720px) {
  .module-toolbar,
  .form-header,
  .results-header,
  .form-actions {
    align-items: stretch;
    flex-direction: column;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
