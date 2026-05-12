import { authenticate } from '../services/authService'

const AUTH_KEY = 'unicos_auth'
const TOKEN_KEY = 'unicos_access_token'
const USER_KEY = 'unicos_user'

export function isAuthenticated() {
  return localStorage.getItem(AUTH_KEY) === 'true' && !!localStorage.getItem(TOKEN_KEY)
}

export function getCurrentUser() {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || 'null')
  } catch {
    return null
  }
}

export async function login(email, senha) {
  if (!email?.trim() || !senha?.trim()) {
    throw new Error('Preencha e-mail e senha.')
  }

  const auth = await authenticate({ email: email.trim(), senha: senha.trim() })

  if (!auth.raw.tokenAccess) {
    throw new Error('Login realizado, mas o backend não retornou o token. Verifique o nome do campo no response.')
  }

  localStorage.setItem(AUTH_KEY, 'true')
  localStorage.setItem(TOKEN_KEY, auth.raw.tokenAccess)
  localStorage.setItem(USER_KEY, JSON.stringify(auth.user || { email: email.trim() }))

  return auth
}

export function logout() {
  localStorage.removeItem(AUTH_KEY)
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}
