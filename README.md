# UNICOS Backend

Backend do projeto **UNICOS**, organizado como um reactor Maven multi-módulo e executável em containers com Docker Compose.

A plataforma é composta por um Service Registry (Eureka), um API Gateway (Spring Cloud Gateway), microsserviços Spring Boot e um banco MySQL isolado por domínio.

## Estrutura

| Caminho | Responsabilidade |
|---|---|
| `pom.xml` | Agregador (reactor) de todos os módulos |
| `unicos-parent/` | POM pai: versões de Spring Boot, Spring Cloud, SpringDoc, JWT e configuração de compilação |
| `unicos-core/` | Bibliotecas compartilhadas (`core-base`, `core-auth`, `core-tenant`, `core-usuario`, `core-pessoas`, `core-web`) |
| `service-registry/` | Eureka Server |
| `gateway/` | API Gateway: roteamento, validação de JWT na borda, CORS e portal `/docs` |
| `ms-*/` | Microsserviços de domínio |
| `Dockerfile.service` | Build multi-stage único para qualquer serviço (`SERVICE_NAME`) |
| `compose.yml` | Infraestrutura, bancos e serviços |
| `compose.homolog.yml` | Override de homologação (publica também o Eureka) |
| `compose.prod.yml` | Override de produção (profile `prod`, filesystem somente leitura, limites de recursos) |
| `.env.example` | Modelo das variáveis de ambiente |

### Versões

| Componente | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.7 |
| Spring Cloud | 2025.0.0 |
| SpringDoc OpenAPI | 2.8.13 |

Todas as versões são definidas apenas em `unicos-parent/pom.xml`.

### Microsserviços

| Serviço | Responsabilidade | Banco |
|---|---|---|
| `ms-autenticacao` | Login e renovação de tokens JWT | — |
| `ms-pessoas` | Pessoas físicas/jurídicas, endereços, contatos, documentos, usuários e verificação de e-mail | `mysql-pessoas` |
| `ms-permissao` | Roles, permissões e vínculos role × permissão | `mysql-permissao` |
| `ms-empresa` | Empresas (tenants), contatos, endereços, configurações, parâmetros e vínculos de usuários | `mysql-empresa` |
| `ms-cliente` | Clientes, categorias e observações | `mysql-cliente` |
| `ms-estoque` | Estoques, saldos por produto, movimentações, responsáveis e vínculos com filiais | `mysql-estoque` |
| `ms-produto` | Catálogo de produtos, categorias, marcas, atributos, preços, imagens e códigos de barras | `mysql-produto` |

### Bibliotecas core

| Módulo | Conteúdo |
|---|---|
| `core-base` | `EntidadeAuditavel` (auditoria JPA) e serialização de erros em Problem Details |
| `core-auth` | Validação de JWT (`TokenCoreService`), claims padronizadas e token interno (`TokenInternoService`) |
| `core-tenant` | `TenantContext`, entidade/repositório/serviço base multi-tenant |
| `core-usuario` | `UserContext` e contratos de usuário trocados entre serviços |
| `core-pessoas` | Contratos do `ms-pessoas` consumidos por outros serviços |
| `core-web` | Filtro de contexto da requisição, segurança padrão, tratamento global de erros e interceptor Feign |

## Segurança

### Fluxo de autenticação

1. O cliente chama `POST /ms-autenticacao/v1/autenticacao/login` (e-mail e senha) e recebe `tokenAccess` e `refreshToken`.
2. As demais chamadas enviam `Authorization: Bearer <tokenAccess>` ao gateway.
3. O gateway valida o token e encaminha a requisição ao serviço.
4. O serviço **valida o JWT novamente** (`ContextoRequisicaoFilter`) e extrai dele o usuário e a empresa. Nenhum header de identidade enviado pelo cliente é considerado. Tokens sem expiração (`exp`) são recusados.
5. A permissão exigida pela rota é verificada no `ms-permissao`. Métodos HTTP sem permissão correspondente (`OPTIONS`, `TRACE`...) são recusados com `403`; `HEAD` exige a mesma permissão do `GET`.
6. Quando o access token expira, o cliente chama `POST /ms-autenticacao/v1/autenticacao/atualizar-token` com o `refreshToken`. Usuários desativados ou removidos não conseguem renovar a sessão.

Regras adicionais:

* **Força bruta no login**: após `UNICOS_LOGIN_MAX_TENTATIVAS` senhas incorretas para o mesmo e-mail (padrão 5), o login desse e-mail responde `429` por `UNICOS_LOGIN_BLOQUEIO` (padrão 15 minutos), mesmo com a senha correta. O controle fica em memória, por instância do `ms-autenticacao`; com várias réplicas, use também um limitador compartilhado (gateway + Redis, por exemplo).
* **Usuário desativado** perde as permissões imediatamente: a role deixa de ser considerada nas verificações, mesmo que o access token ainda não tenha expirado.
* **Cadastro de usuários**: senha com 8 a 72 caracteres; o usuário não pode alterar a própria role, desativar ou excluir a própria conta. Ao trocar o e-mail, os links de verificação pendentes são invalidados.

### Comunicação interna

* Endpoints `/internal/**` dos serviços são usados apenas entre serviços e exigem o header `X-Internal-Token` (`UNICOS_INTERNAL_TOKEN`), enviado automaticamente pelo interceptor Feign do `core-web`.
* O gateway bloqueia `/{servico}/internal/**` e `/{servico}/actuator/**` vindos de fora e remove headers que só a plataforma pode definir (`X-Internal-Token`, `X-Usuario-Id`, `X-Tenant-Id`).
* O JWT do usuário é repassado nas chamadas Feign, preservando usuário e empresa no serviço chamado.

### Multi-tenant

Todo dado de negócio pertence a uma empresa (`empresa_id`). Os serviços filtram leituras e escritas pela empresa do token; registros de outra empresa respondem `404`. Identificadores referenciados no corpo das requisições (estoque de um saldo, estoques de origem/destino de uma movimentação, movimentação de um item, atributo de um produto etc.) também precisam pertencer à empresa do token. Exceções intencionais, por serem dados de referência compartilhados:

* municípios e tipos de relação (`ms-pessoas`) e o catálogo de permissões (`ms-permissao`) podem ser lidos e utilizados por qualquer empresa, mas só são alterados pela empresa que os cadastrou. Uma permissão já vinculada a roles de outras empresas não pode ser renomeada nem excluída;
* login e e-mail de usuário, CPF, CNPJ e número de documento são únicos em toda a base (restrição do banco).

No `ms-empresa`, a própria empresa é o tenant. `/v1/empresas` enxerga apenas a empresa do token e as filiais vinculadas a ela (`matrizId`). Por essa rota é possível cadastrar apenas filiais da empresa do token (somente quando ela é uma matriz); novas matrizes são novos tenants e devem ser cadastradas pela administração da plataforma. O tipo (matriz/filial) não pode ser alterado e a empresa do token não pode ser excluída.

### Formato de erros

Todos os serviços e o gateway respondem erros no formato [Problem Details (RFC 9457)](https://www.rfc-editor.org/rfc/rfc9457), `Content-Type: application/problem+json`:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Pessoa não encontrada.",
  "instance": "/v1/pessoas/99",
  "timestamp": "2026-10-03T17:45:00-03:00",
  "path": "/v1/pessoas/99"
}
```

Erros de validação de corpo trazem também o mapa `errors` (campo → mensagem).

O `detail` traz as mensagens de regra de negócio da plataforma. Mensagens de exceções de bibliotecas e frameworks (que podem conter nomes de classes, consultas ou valores internos) são substituídas por um texto genérico e registradas apenas no log.

### Rotas públicas (sem token)

| Rota no gateway | Uso |
|---|---|
| `POST /ms-autenticacao/v1/autenticacao/login` | Login |
| `POST /ms-autenticacao/v1/autenticacao/atualizar-token` | Renovação de tokens |
| `PATCH /ms-pessoas/v1/verificacao-email/confirmar?token=...` | Confirmação de e-mail |
| `GET /docs` | Portal com links para o Swagger de cada serviço (desligado no profile `prod`, assim como o Swagger/OpenAPI dos serviços) |

## Variáveis de ambiente

Crie o arquivo do ambiente a partir do modelo:

```bash
cp .env.example .env.docker.local
```

No PowerShell:

```powershell
Copy-Item .env.example .env.docker.local
```

Variáveis obrigatórias:

* `SPRING_PROFILES_ACTIVE`;
* nome, usuário e senha de cada banco (`*_DB_NAME`, `*_DB_USER`, `*_DB_PASSWORD`);
* `UNICOS_JWT_ISSUER` e `UNICOS_JWT_SECRET` (mínimo de 32 caracteres);
* `UNICOS_INTERNAL_TOKEN` (mínimo de 16 caracteres).

Os serviços não sobem quando os segredos estão ausentes ou curtos demais.

Opcionais: `GATEWAY_BIND_ADDRESS`/`GATEWAY_PORT` (padrão `127.0.0.1:8082`), `EUREKA_BIND_ADDRESS`/`EUREKA_PORT` (homologação), `CORS_ALLOWED_ORIGINS` (padrão `*`; **restrinja em homologação e produção**), `UNICOS_DOCS_ENABLED`, `UNICOS_TEMPO_EXP_TOKEN` e `UNICOS_TEMPO_EXP_REFRESH_TOKEN` (minutos), `UNICOS_LOGIN_MAX_TENTATIVAS` e `UNICOS_LOGIN_BLOQUEIO` (ex.: `15m`).

O arquivo é usado pelo Compose apenas para interpolação (`--env-file`): cada container recebe somente as variáveis de que precisa. O gateway recebe apenas o segredo JWT; o Eureka, nenhum segredo; cada microsserviço, apenas a senha do próprio banco. O usuário `root` de cada MySQL recebe uma senha aleatória, exibida no log da primeira inicialização do volume (volumes já existentes mantêm a senha anterior).

## Executando com Docker Compose

Pré-requisitos: Docker com Compose v2. O build usa Maven e JDK 21 dentro do próprio Docker.

### Local

```bash
docker compose --env-file .env.docker.local -f compose.yml -p unicos-local config --quiet
```

```bash
docker compose --env-file .env.docker.local -f compose.yml -p unicos-local up -d --build
```

| Componente | Acesso pelo host |
|---|---|
| Gateway | `http://localhost:8082` |
| Microsserviços, MySQL, Eureka | apenas pela rede Docker |

Status e logs:

```bash
docker compose --env-file .env.docker.local -f compose.yml -p unicos-local ps
```

```bash
docker compose --env-file .env.docker.local -f compose.yml -p unicos-local logs -f ms-autenticacao ms-pessoas
```

Parar preservando os dados:

```bash
docker compose --env-file .env.docker.local -f compose.yml -p unicos-local down
```

`down -v` remove também os volumes (dados dos bancos) e só deve ser usado quando a perda dos dados for intencional.

O RabbitMQ está provisionado no profile `mensageria` do Compose, mas nenhum serviço o utiliza ainda. Para subi-lo, acrescente `--profile mensageria` aos comandos.

### Homologação

Use um `.env.homolog` próprio com `SPRING_PROFILES_ACTIVE=homolog`:

```bash
docker compose --env-file .env.homolog -f compose.yml -f compose.homolog.yml -p unicos-homolog up -d --build
```

O override publica também o painel do Eureka (`EUREKA_BIND_ADDRESS:EUREKA_PORT`).

### Produção

Use um `.env.prod` próprio (nunca reutilize segredos de outros ambientes), com `CORS_ALLOWED_ORIGINS` restrito ao domínio do frontend:

```bash
docker compose --env-file .env.prod -f compose.yml -f compose.prod.yml -p unicos-prod config --quiet
```

```bash
docker compose --env-file .env.prod -f compose.yml -f compose.prod.yml -p unicos-prod up -d --build
```

O override de produção aplica o profile `prod`, filesystem somente leitura com `/tmp` em `tmpfs`, `no-new-privileges`, limites de `768M`/`1.0` CPU e rotação de logs. Somente o Gateway é publicado.

Proxy reverso com TLS, backups, observabilidade e política de exposição pública devem ser tratados pela infraestrutura do ambiente.

## Executando fora do Docker

Com JDK 21 e Maven instalados, compile e teste tudo a partir da raiz:

```bash
mvn clean verify
```

Para um único serviço e suas dependências do reactor:

```bash
mvn -pl ms-pessoas -am clean package
```

Cada serviço lê as variáveis do ambiente (por exemplo, as do `.env.local`) para Eureka, banco e segredos. Fora do Docker as portas são aleatórias (`server.port=0`), salvo quando `*_SERVER_PORT` é definido.

## Banco de dados e migrações

Cada serviço usa Flyway com pastas separadas por finalidade:

| Pasta | Conteúdo | Aplicada em |
|---|---|---|
| `db/migration/common` | Estrutura (tabelas, índices, restrições) | todos os ambientes |
| `db/migration/local` | Dados fictícios de desenvolvimento (inclui usuários de teste) | profile padrão/local |
| `db/migration/homolog` | Dados específicos de homologação | profile `homolog` |
| `db/migration/prod` | Dados específicos de produção | profile `prod` |

Regras:

* alterações de estrutura vão sempre para `common`;
* os números de versão são globais por serviço (as pastas são lidas em conjunto): use sempre um número maior que o último existente em **qualquer** pasta;
* nunca altere uma migração já aplicada.

O Hibernate roda com `ddl-auto=validate`: o serviço não sobe se o schema divergir das entidades.

> Os ambientes `homolog` e `prod` não recebem o catálogo de permissões nem roles (estão em `local`). Antes do primeiro uso, cadastre-os via API ou por migrações nas pastas do ambiente.

## Healthchecks e Actuator

Cada serviço expõe apenas `/actuator/health` e `/actuator/info`, sem detalhes. No Compose, o healthcheck usa `curl` dentro do container; o gateway nunca encaminha `/actuator/**` dos serviços.

## Testes

```bash
mvn test
```

Os testes unitários cobrem a validação de tokens, o token interno, o filtro de contexto/permissões (inclusive métodos HTTP não mapeados), o tratamento seguro de mensagens de erro, o gateway (bloqueio de rotas internas, inclusive com barras duplicadas ou codificadas, remoção de headers, 401), login/renovação de tokens, a proteção contra força bruta e o mapeamento rota → permissão.

Cada serviço possui também um teste de contexto (`ContextoAplicacaoTest`) que sobe a aplicação completa com H2 em memória (profile `test`, sem Flyway e sem Eureka) e valida pela API a cadeia de segurança, o isolamento por empresa, o tratamento de erros e as consultas dos repositórios. Esses testes não substituem a validação das migrações em MySQL, que deve ser feita subindo o ambiente com Docker Compose.
