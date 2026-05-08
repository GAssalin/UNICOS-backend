import { createRouter, createWebHistory } from 'vue-router'
import { isAuthenticated } from '../composables/useAuth'
import LoginPage from '../pages/LoginPage.vue'
import AppLayout from '../layouts/AppLayout.vue'
import DashboardHomePage from '../pages/DashboardHomePage.vue'
import ModulePage from '../pages/ModulePage.vue'
import ProductsPage from '../pages/ProductsPage.vue'
import StockPage from '../pages/StockPage.vue'
import MovementsPage from '../pages/MovementsPage.vue'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
    meta: { guestOnly: true }
  },
  {
    path: '/',
    component: AppLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'dashboard',
        component: DashboardHomePage,
        meta: { title: 'Dashboard' }
      },
      {
        path: 'compras',
        name: 'compras',
        component: ModulePage,
        props: { title: 'Compras' },
        meta: { title: 'Compras' }
      },
      {
        path: 'estoque',
        name: 'estoque',
        component: StockPage,
        meta: { title: 'Estoque' }
      },
      {
        path: 'logistica',
        name: 'logistica',
        component: ModulePage,
        props: { title: 'Logística' },
        meta: { title: 'Logística' }
      },
      {
        path: 'movimentacoes',
        name: 'movimentacoes',
        component: MovementsPage,
        meta: { title: 'Movimentações' }
      },
      {
        path: 'produtos',
        name: 'produtos',
        component: ProductsPage,
        meta: { title: 'Produtos' }
      },
      {
        path: 'producao',
        name: 'producao',
        component: ModulePage,
        props: { title: 'Produção' },
        meta: { title: 'Produção' }
      },
      {
        path: 'qualidade',
        name: 'qualidade',
        component: ModulePage,
        props: { title: 'Qualidade' },
        meta: { title: 'Qualidade' }
      },
      {
        path: 'rh',
        name: 'rh',
        component: ModulePage,
        props: { title: 'RH' },
        meta: { title: 'RH' }
      },
      {
        path: 'ti',
        name: 'ti',
        component: ModulePage,
        props: { title: 'TI' },
        meta: { title: 'TI' }
      },
      {
        path: 'vendas',
        name: 'vendas',
        component: ModulePage,
        props: { title: 'Vendas' },
        meta: { title: 'Vendas' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const authenticated = isAuthenticated()

  if (to.meta.requiresAuth && !authenticated) {
    return { name: 'login' }
  }

  if (to.meta.guestOnly && authenticated) {
    return { name: 'dashboard' }
  }

  return true
})

export default router
