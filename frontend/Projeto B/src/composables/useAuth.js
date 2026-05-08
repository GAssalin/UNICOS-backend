const AUTH_KEY = 'auth'

export function isAuthenticated() {
  return localStorage.getItem(AUTH_KEY) === 'true'
}

export function login(user, pass) {
  if (!user?.trim() || !pass?.trim()) {
    throw new Error('Preencha usuário e senha.')
  }

  localStorage.setItem(AUTH_KEY, 'true')
}

export function logout() {
  localStorage.removeItem(AUTH_KEY)
}
