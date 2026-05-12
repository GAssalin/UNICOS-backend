# UniCoS ERP Frontend

Frontend Vue 3 + Vite integrado ao backend pelo endpoint de autenticação exibido no Postman.

## Backend configurado

Login:

```http
POST http://localhost:8082/ms-autenticacao/v1/autenticacao/login
Content-Type: application/json

{
  "email": "admin@unicos.com",
  "senha": "123456"
}
```

O frontend envia exatamente esse payload e salva o token retornado no `localStorage` para usar nas próximas chamadas com:

```http
Authorization: Bearer <token>
```

## Como rodar

```bash
npm install
npm run dev
```

Acesse a URL exibida pelo Vite, normalmente:

```txt
http://localhost:5173
```

## Configuração

Crie um arquivo `.env` na raiz, ou copie o `.env.example`:

```env
VITE_API_BASE_URL=http://localhost:8082
VITE_AUTH_LOGIN_PATH=/ms-autenticacao/v1/autenticacao/login
```

## Arquivos principais alterados

- `src/services/api.js`: client HTTP centralizado com base URL, JSON e Bearer Token.
- `src/services/authService.js`: chamada real para o login do backend.
- `src/composables/useAuth.js`: controle de autenticação, token e usuário logado.
- `src/pages/LoginPage.vue`: login assíncrono usando `email` e `senha`.
- `src/layouts/AppLayout.vue`: exibe dados do usuário salvo após o login.

## Observação importante

Se o navegador bloquear a chamada com erro de CORS, libere o frontend no backend, por exemplo para `http://localhost:5173`.
