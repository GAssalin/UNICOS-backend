import { computed, ref, watch } from 'vue'

const PRODUCTS_KEY = 'unicos_products_v1'
const MOVEMENTS_KEY = 'unicos_movements_v1'

const defaultProducts = [
  {
    id: 'prd-001',
    codigo: 'PRD-001',
    codigoBarras: '7890000000011',
    nome: 'Parafuso Sextavado 1/4”',
    categoria: 'Ferragens',
    unidade: 'UN',
    fornecedor: 'Fornecedor Padrão',
    estoqueMinimo: 20,
    precoCusto: 0.35,
    precoVenda: 0.75
  },
  {
    id: 'prd-002',
    codigo: 'PRD-002',
    codigoBarras: '7890000000028',
    nome: 'Arruela Lisa 1/4”',
    categoria: 'Ferragens',
    unidade: 'UN',
    fornecedor: 'Fornecedor Padrão',
    estoqueMinimo: 30,
    precoCusto: 0.1,
    precoVenda: 0.25
  },
  {
    id: 'prd-003',
    codigo: 'PRD-003',
    codigoBarras: '7890000000035',
    nome: 'Bucha 8mm',
    categoria: 'Fixação',
    unidade: 'UN',
    fornecedor: 'Casa dos Insumos',
    estoqueMinimo: 40,
    precoCusto: 0.22,
    precoVenda: 0.5
  }
]

const defaultMovements = [
  { id: 'mov-001', produtoId: 'prd-001', tipo: 'entrada', quantidade: 50, data: '2026-04-20', fornecedor: 'Fornecedor Padrão', destino: '', observacao: 'Estoque inicial' },
  { id: 'mov-002', produtoId: 'prd-002', tipo: 'entrada', quantidade: 35, data: '2026-04-20', fornecedor: 'Fornecedor Padrão', destino: '', observacao: 'Estoque inicial' },
  { id: 'mov-003', produtoId: 'prd-003', tipo: 'entrada', quantidade: 60, data: '2026-04-21', fornecedor: 'Casa dos Insumos', destino: '', observacao: 'Compra' },
  { id: 'mov-004', produtoId: 'prd-001', tipo: 'saida', quantidade: 42, data: '2026-04-22', fornecedor: '', destino: 'Produção', observacao: 'Ordem de produção' },
  { id: 'mov-005', produtoId: 'prd-002', tipo: 'saida', quantidade: 8, data: '2026-04-23', fornecedor: '', destino: 'Manutenção', observacao: 'Uso interno' }
]

function readStorage(key, fallback) {
  try {
    const saved = localStorage.getItem(key)
    return saved ? JSON.parse(saved) : fallback
  } catch {
    return fallback
  }
}

const products = ref(readStorage(PRODUCTS_KEY, defaultProducts))
const movements = ref(readStorage(MOVEMENTS_KEY, defaultMovements))

watch(products, (value) => localStorage.setItem(PRODUCTS_KEY, JSON.stringify(value)), { deep: true })
watch(movements, (value) => localStorage.setItem(MOVEMENTS_KEY, JSON.stringify(value)), { deep: true })

function uid(prefix) {
  return `${prefix}-${Date.now()}-${Math.random().toString(16).slice(2)}`
}

function normalizeNumber(value) {
  return Number(String(value ?? 0).replace(',', '.')) || 0
}

const productsWithStock = computed(() => products.value.map((product) => {
  const saldo = movements.value
    .filter((movement) => movement.produtoId === product.id)
    .reduce((total, movement) => total + (movement.tipo === 'entrada' ? movement.quantidade : -movement.quantidade), 0)

  return {
    ...product,
    saldo,
    valorEstoque: saldo * normalizeNumber(product.precoCusto),
    estoqueBaixo: saldo <= normalizeNumber(product.estoqueMinimo)
  }
}))

function addProduct(payload) {
  const product = {
    id: uid('prd'),
    codigo: payload.codigo?.trim(),
    codigoBarras: payload.codigoBarras?.trim(),
    nome: payload.nome?.trim(),
    categoria: payload.categoria?.trim(),
    unidade: payload.unidade?.trim() || 'UN',
    fornecedor: payload.fornecedor?.trim(),
    estoqueMinimo: normalizeNumber(payload.estoqueMinimo),
    precoCusto: normalizeNumber(payload.precoCusto),
    precoVenda: normalizeNumber(payload.precoVenda)
  }

  products.value.unshift(product)

  const estoqueInicial = normalizeNumber(payload.estoqueInicial)
  if (estoqueInicial > 0) {
    addMovement({
      produtoId: product.id,
      tipo: 'entrada',
      quantidade: estoqueInicial,
      data: new Date().toISOString().slice(0, 10),
      fornecedor: product.fornecedor,
      destino: '',
      observacao: 'Estoque inicial'
    })
  }
}

function updateProduct(id, payload) {
  const index = products.value.findIndex((product) => product.id === id)
  if (index === -1) return

  products.value[index] = {
    ...products.value[index],
    codigo: payload.codigo?.trim(),
    codigoBarras: payload.codigoBarras?.trim(),
    nome: payload.nome?.trim(),
    categoria: payload.categoria?.trim(),
    unidade: payload.unidade?.trim() || 'UN',
    fornecedor: payload.fornecedor?.trim(),
    estoqueMinimo: normalizeNumber(payload.estoqueMinimo),
    precoCusto: normalizeNumber(payload.precoCusto),
    precoVenda: normalizeNumber(payload.precoVenda)
  }
}

function deleteProduct(id) {
  products.value = products.value.filter((product) => product.id !== id)
  movements.value = movements.value.filter((movement) => movement.produtoId !== id)
}

function addMovement(payload) {
  const quantity = normalizeNumber(payload.quantidade)
  if (!payload.produtoId || quantity <= 0) return

  movements.value.unshift({
    id: uid('mov'),
    produtoId: payload.produtoId,
    tipo: payload.tipo || 'entrada',
    quantidade: quantity,
    data: payload.data || new Date().toISOString().slice(0, 10),
    fornecedor: payload.fornecedor?.trim() || '',
    destino: payload.destino?.trim() || '',
    observacao: payload.observacao?.trim() || ''
  })
}

function deleteMovement(id) {
  movements.value = movements.value.filter((movement) => movement.id !== id)
}

function movementWithProduct(movement) {
  const product = products.value.find((item) => item.id === movement.produtoId)
  return {
    ...movement,
    produto: product?.nome || 'Produto removido',
    codigo: product?.codigo || '-'
  }
}

const movementsDetailed = computed(() => movements.value.map(movementWithProduct))

export function useInventory() {
  return {
    products,
    movements,
    productsWithStock,
    movementsDetailed,
    addProduct,
    updateProduct,
    deleteProduct,
    addMovement,
    deleteMovement
  }
}
