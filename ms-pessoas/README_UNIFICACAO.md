# MS-Pessoas e Usuários — versão unificada

Esta versão utiliza o `ms-pessoas` como aplicação base e incorpora as funcionalidades do antigo `ms-usuario` no pacote:

`br.com.unicos.ms_pessoas.usuario`

## O que foi unificado

- CRUD de pessoas físicas e jurídicas;
- endereços, contatos, documentos e relações entre pessoas;
- CRUD de usuários;
- vínculo opcional de `usuario.pessoa_id` com `pessoa.id`;
- verificação de e-mail;
- consulta interna de usuário para autenticação;
- validação de permissões das rotas de pessoas e usuários em um único filtro;
- um único cliente Feign para o `ms-permissao`;
- migrations Flyway em sequência única (`V1` até `V13`).

## Serviços que continuam externos

- Config Server;
- Eureka/Service Registry;
- `ms-permissao`;
- serviço de autenticação que consome `/internal/auth/by-email`, caso exista separadamente.

## Banco de dados

O serviço unificado deve apontar para um único schema. A migration `V11__create_usuario_schema.sql` cria as tabelas de usuário e adiciona a chave estrangeira de `usuario.pessoa_id` para `pessoa.id`.

Em banco já existente, não execute as migrations como se fosse um banco vazio sem antes alinhar o histórico da tabela `flyway_schema_history`. Faça backup e prepare uma migration específica para consolidação dos dados dos dois schemas.

## Configuração

As propriedades permanecem centralizadas no Config Server sob o nome:

`ms-pessoas`

As configurações antes vinculadas a `ms-usuario` devem ser incorporadas ao perfil/configuração de `ms-pessoas`.

## Validação local

Execute:

```bash
./mvnw clean test
```

O build exige acesso ao Maven Central, ao GitHub Packages do projeto e credenciais válidas para baixar os módulos `core-*`.
