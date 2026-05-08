# UniCoS ERP Frontend

Projeto corrigido para rodar com Vue 3 e Vite.

## Como executar

```bash
npm install
npm run dev
```

## Ajustes realizados

- criação do `package.json` completo com scripts
- adição de `vite`, `vue` e `@vitejs/plugin-vue`
- criação do `vite.config.js`
- criação do `index.html`
- troca de `window.location.href` por `router.push`
- layout com `router-view` interno
- módulos separados em rotas reais
- centralização simples da autenticação mock em `useAuth.js`
