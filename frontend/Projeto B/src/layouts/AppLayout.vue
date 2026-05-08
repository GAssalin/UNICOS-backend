<template>
  <div class="layout-shell">
    <div class="layout">
      <aside class="sidebar">
        <div class="brand-block">
          <img
            v-if="showLogo"
            :src="logoUrl"
            alt="Logo UniCoS"
            class="brand-logo"
            @error="showLogo = false"
          />
        </div>

        <nav class="menu">
          <RouterLink to="/" class="menu-item dashboard-item" active-class="menu-item-active">
            Dashboard
          </RouterLink>

          <hr class="menu-divider" />

          <RouterLink
            v-for="item in menuItems"
            :key="item.to"
            :to="item.to"
            class="menu-item"
            active-class="menu-item-active"
          >
            {{ item.label }}
          </RouterLink>
        </nav>

        <button class="sidebar-logout" @click="handleLogout">Sair do sistema</button>
      </aside>

      <div class="main">
        <header class="topbar">
          <div>
            <p class="topbar-label">Painel UniCoS</p>
            <strong>{{ pageTitle }}</strong>
          </div>

          <div class="topbar-user">
            <div class="user-avatar">A</div>
            <div>
              <strong>Administrador</strong>
              <p>admin</p>
            </div>
          </div>
        </header>

        <main class="content">
          <router-view />
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { logout } from '../composables/useAuth'

const route = useRoute()
const router = useRouter()
const logoUrl = '/assets/logo-outline.png'
const showLogo = ref(true)

const menuItems = [
  { label: 'Compras', to: '/compras' },
  { label: 'Estoque', to: '/estoque' },
  { label: 'Logística', to: '/logistica' },
  { label: 'Movimentações', to: '/movimentacoes' },
  { label: 'Produção', to: '/producao' },
  { label: 'Produtos', to: '/produtos' },
  { label: 'Qualidade', to: '/qualidade' },
  { label: 'RH', to: '/rh' },
  { label: 'TI', to: '/ti' },
  { label: 'Vendas', to: '/vendas' }
].sort((a, b) => a.label.localeCompare(b.label, 'pt-BR'))

const pageTitle = computed(() => route.meta.title || route.name || 'Dashboard')

function handleLogout() {
  logout()
  router.push({ name: 'login' })
}
</script>

<style scoped>
.layout-shell {
  min-height: 100vh;
  background:
    radial-gradient(circle at top left, rgba(12, 77, 139, 0.16), transparent 32%),
    linear-gradient(135deg, #edf3f9 0%, #f6f8fc 100%);
  padding: 16px;
}

.layout {
  min-height: calc(100vh - 32px);
  display: flex;
  gap: 18px;
}

.sidebar {
  width: 292px;
  background: linear-gradient(180deg, #062b73 0%, #082557 100%);
  color: #ffffff;
  padding: 24px 18px;
  display: flex;
  flex-direction: column;
  gap: 24px;
  border-radius: 28px;
  box-shadow: 0 22px 45px rgba(11, 34, 53, 0.22);
}

.brand-block {
  padding: 8px 8px 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand-logo {
  width: 100%;
  max-width: 220px;
  height: auto;
  display: block;
  filter: brightness(0) invert(1) drop-shadow(0 0 8px rgba(255,255,255,.12));
}

.menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.menu-divider {
  width: 100%;
  border: none;
  border-top: 1px solid rgba(255,255,255,0.18);
  margin: 2px 0 4px;
}

.menu-item {
  text-decoration: none;
  color: inherit;
  padding: 13px 16px;
  border-radius: 14px;
  transition: 0.2s ease;
  font-weight: 600;
  border: 1px solid transparent;
}

.menu-item:hover,
.menu-item-active {
  background: rgba(54, 116, 255, 0.42);
  border-color: rgba(255, 255, 255, 0.14);
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.04);
}

.dashboard-item {
  background: rgba(54, 116, 255, 0.22);
}

.sidebar-logout {
  margin-top: auto;
  border: 1px solid rgba(255,255,255,0.16);
  background: rgba(255,255,255,0.08);
  color: #fff;
  padding: 13px 16px;
  border-radius: 14px;
  cursor: pointer;
  font-weight: 700;
}

.main {
  flex: 1;
  min-width: 0;
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid rgba(220, 229, 239, 0.95);
  border-radius: 30px;
  backdrop-filter: blur(10px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 22px 28px;
  background: rgba(255, 255, 255, 0.82);
  border-bottom: 1px solid #dce5ef;
  backdrop-filter: blur(8px);
}

.topbar-label {
  margin: 0 0 6px;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--muted-text);
}

.topbar-user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.topbar-user p,
.topbar-user strong {
  margin: 0;
}

.topbar-user p {
  color: var(--muted-text);
  font-size: 13px;
}

.user-avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #e5edf8, #c8d6ea);
  color: #1c2a39;
  font-weight: 800;
}

.content {
  padding: 28px;
}

@media (max-width: 980px) {
  .layout {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
  }
}
</style>
