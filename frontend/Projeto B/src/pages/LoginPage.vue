<template>
  <div class="login-page">
    <div class="login-wave"></div>
    <form class="login-box" @submit.prevent="handleLogin">
      <div class="login-header">
        <img src="/assets/logo-outline.png" alt="Logo UniCoS" class="login-logo" />
        <h1>Entrar no sistema</h1>
        <p class="subtitle">Acesse sua conta para continuar</p>
      </div>

      <label class="field">
        <span>E-mail</span>
        <input v-model="email" type="email" autocomplete="username" placeholder="admin@unicos.com" />
      </label>

      <label class="field">
        <span>Senha</span>
        <input v-model="senha" type="password" autocomplete="current-password" placeholder="123456" />
      </label>

      <label class="remember-row">
        <input type="checkbox" />
        <span>Lembrar-me</span>
      </label>

      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

      <button type="submit" :disabled="loading">{{ loading ? 'Entrando...' : 'Entrar' }}</button>
      <p class="copyright">© 2024 UniCoS. Todos os direitos reservados.</p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../composables/useAuth'

const router = useRouter()
const email = ref('admin@unicos.com')
const senha = ref('123456')
const errorMessage = ref('')
const loading = ref(false)

async function handleLogin() {
  try {
    loading.value = true
    await login(email.value, senha.value)
    errorMessage.value = ''
    router.push({ name: 'dashboard' })
  } catch (error) {
    errorMessage.value = error.message || 'Não foi possível realizar o login.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, #0a2f7a 0%, #0a2a66 100%);
  padding: 24px;
  position: relative;
  overflow: hidden;
}

.login-wave {
  position: absolute;
  inset: auto 0 0 0;
  height: 180px;
  background: radial-gradient(circle at 10% 40%, rgba(255,255,255,0.08), transparent 38%),
    radial-gradient(circle at 30% 60%, rgba(255,255,255,0.06), transparent 30%),
    radial-gradient(circle at 60% 30%, rgba(255,255,255,0.05), transparent 28%);
  opacity: .9;
}

.login-box {
  position: relative;
  width: 100%;
  max-width: 460px;
  background: transparent;
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 18px;
  color: #fff;
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  text-align: left;
  gap: 8px;
  margin-bottom: 8px;
}

.login-logo {
  width: min(100%, 280px);
  height: auto;
  display: block;
  margin-bottom: 10px;
  filter: brightness(0) invert(1) contrast(1.15) drop-shadow(0 0 4px rgba(255,255,255,.14));
}

h1,
.subtitle,
.field span,
.error,
.copyright {
  margin: 0;
}

h1 {
  color: #ffffff;
  font-size: 2.25rem;
  line-height: 1.1;
}

.subtitle,
.copyright,
.remember-row span {
  color: rgba(255,255,255,0.82);
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.field span {
  font-weight: 600;
}

input[type='email'],
input[type='password'] {
  border: 1px solid rgba(255,255,255,0.38);
  border-radius: 14px;
  padding: 14px 16px;
  font-size: 14px;
  background: rgba(5, 23, 59, 0.22);
  color: #fff;
}

input::placeholder {
  color: rgba(255,255,255,0.56);
}

.remember-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

button {
  border: none;
  background: linear-gradient(135deg, #2f6bf2, #1655db);
  color: #ffffff;
  padding: 14px 16px;
  border-radius: 14px;
  font-size: 14px;
  cursor: pointer;
  font-weight: 700;
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.18);
}

.error {
  color: #ffd7d7;
  font-size: 14px;
}

.copyright {
  margin-top: 6px;
  font-size: 12px;
}
</style>
