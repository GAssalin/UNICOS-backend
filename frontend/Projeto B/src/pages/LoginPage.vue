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
        <span>Usuário</span>
        <input v-model="user" type="text" placeholder="Digite seu usuário" />
      </label>

      <label class="field">
        <span>Senha</span>
        <input v-model="pass" type="password" placeholder="Digite sua senha" />
      </label>

      <label class="remember-row">
        <input type="checkbox" />
        <span>Lembrar-me</span>
      </label>

      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

      <button type="submit">Entrar</button>
      <p class="copyright">© 2024 UniCoS. Todos os direitos reservados.</p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../composables/useAuth'

const router = useRouter()
const user = ref('')
const pass = ref('')
const errorMessage = ref('')

function handleLogin() {
  try {
    login(user.value, pass.value)
    errorMessage.value = ''
    router.push({ name: 'dashboard' })
  } catch (error) {
    errorMessage.value = error.message
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

input[type='text'],
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
