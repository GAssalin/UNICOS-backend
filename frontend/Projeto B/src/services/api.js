const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8082'

function buildUrl(path) {
  if (/^https?:\/\//i.test(path)) return path
  return `${API_BASE_URL.replace(/\/$/, '')}/${String(path).replace(/^\//, '')}`
}

export function getAccessToken() {
  return localStorage.getItem('unicos_access_token')
}

export async function apiRequest(path, options = {}) {
  const token = getAccessToken()
  const headers = {
    Accept: 'application/json',
    ...(options.body ? { 'Content-Type': 'application/json' } : {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers
  }

  const response = await fetch(buildUrl(path), {
    ...options,
    headers,
    body: options.body && typeof options.body !== 'string'
      ? JSON.stringify(options.body)
      : options.body
  })

  const contentType = response.headers.get('content-type') || ''
  const data = contentType.includes('application/json') ? await response.json() : await response.text()

  if (!response.ok) {
    const message = data?.message || data?.mensagem || data?.error || `Erro HTTP ${response.status}`
    throw new Error(message)
  }

  return data
}
