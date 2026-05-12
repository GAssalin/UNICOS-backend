import { apiRequest } from './api'

const LOGIN_PATH = import.meta.env.VITE_AUTH_LOGIN_PATH || '/ms-autenticacao/v1/autenticacao/login'

function findToken(response) {
  return response?.token
    || response?.accessToken
    || response?.access_token
    || response?.jwt
    || response?.data?.token
    || response?.data?.accessToken
    || response?.dados?.token
    || response?.dados?.accessToken
}

export async function authenticate({ email, senha }) {
  const response = await apiRequest(LOGIN_PATH, {
    method: 'POST',
    body: { email, senha }
  })

  const token = findToken(response)

  return {
    token,
    user: response?.user || response?.usuario || response?.data?.user || response?.data?.usuario || { email },
    raw: response
  }
}
