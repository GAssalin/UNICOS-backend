# UNICOS Backend

Backend do projeto **UNICOS** organizado como um projeto Maven multi-módulo e executável em containers com Docker Compose.

A infraestrutura disponibilizada na raiz inclui Service Registry (Eureka), API Gateway, microsserviços, bancos MySQL isolados por domínio e RabbitMQ para mensageria.

## Estrutura da raiz

| Arquivo | Responsabilidade |
|---|---|
| `.dockerignore` | Exclui metadados, builds, arquivos de ambiente, logs e outros arquivos do contexto de build Docker |
| `.gitignore` | Ignora arquivos de ambiente reais, artefatos Maven e configurações locais de IDE; permite versionar arquivos `*.example` |
| `.env.example` | Modelo das variáveis de ambiente necessárias ao projeto |
| `compose.yml` | Definição principal da infraestrutura, serviços, rede, volumes, bancos e healthchecks |
| `compose.homolog.yml` | Override para publicar Gateway e Eureka no host |
| `compose.prod.yml` | Override de produção com perfil `prod`, restrições de runtime e publicação apenas do Gateway |
| `Dockerfile.service` | Build multi-stage dos serviços Java com Maven/JDK 21 e runtime JRE 21 |
| `pom.xml` | POM agregador dos módulos Maven |

## Módulos Maven

O `pom.xml` da raiz é um agregador com os seguintes módulos padrão:

```text
unicos-core
service-registry
gateway
ms-autenticacao
ms-pessoas
```

Os módulos abaixo são adicionados pelo perfil Maven `complementares`:

```text
ms-cliente
ms-empresa
ms-estoque
ms-permissao
ms-produto
```

Para compilar todos os módulos complementares:

```bash
mvn -Pcomplementares clean verify
```

Para compilar apenas um módulo e suas dependências do reactor:

```bash
mvn -Pcomplementares -pl ms-permissao -am clean verify
```

> O perfil `complementares` é um **perfil Maven**. O `compose.yml` atual não declara `profiles:` do Docker Compose; portanto os serviços complementares fazem parte da composição normalmente e não dependem de `docker compose --profile complementares`.

## Arquitetura da composição

O `compose.yml` define os seguintes componentes.

### Infraestrutura

| Serviço Compose | Tecnologia | Porta interna | Porta publicada pelo Compose base |
|---|---|---:|---|
| `service-registry` | Eureka / Spring Boot | `8081` | Não |
| `gateway` | Spring Cloud Gateway | `8080` | Não |
| `rabbitmq` | RabbitMQ `4.1-management-alpine` | `5672` / `15672` | Não |

As portas do Gateway e do Eureka são publicadas quando `compose.homolog.yml` é aplicado. Em produção, `compose.prod.yml` publica apenas o Gateway.

### Microsserviços

| Serviço Compose | Módulo usado no build | Porta interna | Banco |
|---|---|---:|---|
| `ms-autenticacao` | `ms-autenticacao` | `8080` | `mysql-autenticacao` |
| `ms-pessoa` | `ms-pessoas` | `8080` | `mysql-pessoa` |
| `ms-empresa` | `ms-empresa` | `8080` | `mysql-empresa` |
| `ms-permissao` | `ms-permissao` | `8080` | `mysql-permissao` |
| `ms-cliente` | `ms-cliente` | `8080` | `mysql-cliente` |
| `ms-estoque` | `ms-estoque` | `8080` | `mysql-estoque` |
| `ms-produto` | `ms-produto` | `8080` | `mysql-produto` |

Todos os serviços de aplicação entram na rede Docker `unicos` e recebem a URL interna do Eureka:

```text
http://service-registry:8081/eureka/
```

O Gateway aguarda o `service-registry` ficar saudável antes de iniciar. Cada microsserviço aguarda o respectivo MySQL e o Service Registry conforme definido em `depends_on`.

Os serviços `ms-pessoa`, `ms-empresa`, `ms-permissao`, `ms-cliente`, `ms-estoque` e `ms-produto` também recebem configuração de conexão com RabbitMQ usando o hostname interno `rabbitmq` e a porta `5672`.

> Embora RabbitMQ possua healthcheck, os microsserviços não possuem `depends_on` explícito para ele no Compose atual.

## Bancos de dados

A composição utiliza **MySQL 8.4**, com um container e um volume persistente para cada domínio:

| Serviço MySQL | Volume |
|---|---|
| `mysql-autenticacao` | `mysql_auth_data` |
| `mysql-empresa` | `mysql_empresa_data` |
| `mysql-pessoa` | `mysql_pessoa_data` |
| `mysql-permissao` | `mysql_permissao_data` |
| `mysql-cliente` | `mysql_cliente_data` |
| `mysql-estoque` | `mysql_estoque_data` |
| `mysql-produto` | `mysql_produto_data` |

Os bancos ficam acessíveis aos microsserviços pela rede Docker e **não possuem portas publicadas no host** nos arquivos fornecidos.

O Compose monta as URLs JDBC diretamente com os nomes dos serviços Docker e a porta `3306`, por exemplo:

```text
jdbc:mysql://mysql-pessoa:3306/${PESSOA_DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

### Usuários dos bancos

As variáveis `*_DB_USER` são usadas para criar o usuário no container MySQL. Entretanto, as configurações `SPRING_DATASOURCE_USERNAME` dos microsserviços estão atualmente fixadas como:

```text
unicos
```

Por isso, com o Compose atual, mantenha `AUTH_DB_USER`, `EMPRESA_DB_USER`, `PESSOA_DB_USER`, `PERMISSAO_DB_USER`, `CLIENTE_DB_USER`, `ESTOQUE_DB_USER` e `PRODUTO_DB_USER` com o valor `unicos`, ou ajuste o `compose.yml` para utilizar as respectivas variáveis também no datasource.

O `MYSQL_ROOT_PASSWORD` de cada banco utiliza atualmente a mesma variável de senha do usuário da aplicação daquele domínio. Não existe uma variável separada para senha de root nos arquivos atuais.

## Variáveis de ambiente

Use `.env.example` como base para criar o arquivo do ambiente desejado.

Exemplo:

```bash
cp .env.example .env.local
```

No PowerShell:

```powershell
Copy-Item .env.example .env.local
```

No mínimo, o Compose principal depende de:

- `ENV_FILE`;
- `SPRING_PROFILES_ACTIVE`;
- credenciais do RabbitMQ (`RABBITMQ_USER` e `RABBITMQ_PASSWORD`);
- nome, usuário e senha de cada banco (`*_DB_NAME`, `*_DB_USER`, `*_DB_PASSWORD`);
- `UNICOS_JWT_SECRET`, usado explicitamente pelo `ms-autenticacao`.

Como os serviços de aplicação usam:

```yaml
env_file: ${ENV_FILE}
```

as demais variáveis presentes no arquivo indicado por `ENV_FILE` também são carregadas no ambiente dos containers de aplicação.

### Variáveis de rede presentes no `.env`

O `.env.example` também define `*_DB_HOST`, `*_DB_PORT`, `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_MANAGEMENT_PORT` e `EUREKA_URL`. O `compose.yml` atual, entretanto, monta as conexões internas diretamente com os nomes dos serviços Docker (`mysql-*`, `rabbitmq` e `service-registry`). Portanto essas variáveis não são usadas pelo Compose para construir essas conexões, embora continuem disponíveis aos containers via `env_file`.

### Bind do Gateway e Eureka

Os overrides aceitam duas variáveis opcionais que não aparecem atualmente no `.env.example`:

```text
GATEWAY_BIND_ADDRESS
EUREKA_BIND_ADDRESS
```

Quando não informadas, ambas usam `127.0.0.1`.

## Executando localmente

Pré-requisitos:

- Docker com Docker Compose v2;
- acesso aos repositórios Maven e registries das imagens Docker.

O build dos containers utiliza Maven e Java 21 dentro do próprio Docker, portanto Maven/JDK no host não são necessários para `docker compose ... --build`.

O `.env.local` fornecido aponta `ENV_FILE=.env.local` e utiliza as portas de host:

```text
Gateway: 8082
Eureka:  8081
```

No arquivo atual, `SPRING_PROFILES_ACTIVE` está vazio. Portanto a execução local não ativa explicitamente um profile Spring por meio do Compose. Se a aplicação exigir o profile `local`, configure:

```dotenv
SPRING_PROFILES_ACTIVE=local
```

### Validar a composição

```bash
docker compose \
  --env-file .env.local \
  -f compose.yml \
  -f compose.homolog.yml \
  -p unicos-local \
  config --quiet
```

### Subir o ambiente

```bash
docker compose \
  --env-file .env.local \
  -f compose.yml \
  -f compose.homolog.yml \
  -p unicos-local \
  up -d --build
```

Com o override de homologação aplicado:

| Componente | Acesso pelo host |
|---|---|
| Gateway | `http://localhost:8082` |
| Eureka | `http://localhost:8081` |
| Microsserviços | apenas pela rede Docker / Gateway |
| MySQL | apenas pela rede Docker |
| RabbitMQ | apenas pela rede Docker |

O `compose.homolog.yml` publica Gateway e Eureka em `127.0.0.1` por padrão.

### Status e logs

```bash
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local ps
```

```bash
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local logs -f
```

Para acompanhar serviços específicos:

```bash
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local logs -f ms-autenticacao ms-pessoa
```

### Parar o ambiente

```bash
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local down
```

O comando acima preserva os volumes. Para remover também os dados persistidos:

```bash
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local down -v
```

> `down -v` remove os volumes nomeados da composição e deve ser usado somente quando a perda dos dados for intencional.

## Homologação

O `compose.homolog.yml` é um override pequeno: ele apenas publica no host as portas do Gateway e do Service Registry.

Crie um arquivo `.env.homolog` baseado no `.env.example` e configure, entre outras variáveis:

```dotenv
ENV_FILE=.env.homolog
SPRING_PROFILES_ACTIVE=homolog
```

Validação:

```bash
docker compose \
  --env-file .env.homolog \
  -f compose.yml \
  -f compose.homolog.yml \
  -p unicos-homolog \
  config --quiet
```

Inicialização:

```bash
docker compose \
  --env-file .env.homolog \
  -f compose.yml \
  -f compose.homolog.yml \
  -p unicos-homolog \
  up -d --build
```

Para aceitar conexões de outras máquinas, configure conscientemente os binds, por exemplo `GATEWAY_BIND_ADDRESS` e `EUREKA_BIND_ADDRESS`, de acordo com a política de rede do ambiente.

## Produção

Use um arquivo `.env.prod` próprio e nunca reutilize segredos do ambiente local.

O `compose.prod.yml`:

- força `SPRING_PROFILES_ACTIVE=prod` nos serviços de aplicação definidos no override;
- deixa o filesystem desses containers como somente leitura;
- cria `/tmp` como `tmpfs`;
- habilita `no-new-privileges`;
- limita cada aplicação a `768M` e `1.0` CPU, com reserva de `256M` de memória;
- configura rotação de logs `json-file` (`10m`, 3 arquivos);
- publica somente o Gateway no host;
- força `SPRING_JPA_HIBERNATE_DDL_AUTO=validate` e `SPRING_FLYWAY_ENABLED=false` especificamente em `ms-pessoa`.

Valide a composição antes de subir:

```bash
docker compose \
  --env-file .env.prod \
  -f compose.yml \
  -f compose.prod.yml \
  -p unicos-prod \
  config --quiet
```

Depois:

```bash
docker compose \
  --env-file .env.prod \
  -f compose.yml \
  -f compose.prod.yml \
  -p unicos-prod \
  up -d --build
```

Não combine `compose.homolog.yml` e `compose.prod.yml` sem intenção explícita, pois o override de homologação publica também o Service Registry.

> Os arquivos fornecidos não configuram proxy reverso, TLS, backups, observabilidade externa ou política de exposição pública. Esses itens devem ser tratados pela infraestrutura do ambiente.

## Build Docker

`Dockerfile.service` utiliza build multi-stage:

1. `maven:3.9.9-eclipse-temurin-21` para compilar;
2. `eclipse-temurin:21-jre-alpine` para executar a aplicação.

O build recebe:

```text
SERVICE_NAME
MAVEN_PROFILES
```

e executa, conceitualmente:

```bash
mvn -B -ntp [-Pperfil] -pl "$SERVICE_NAME" -am clean package -DskipTests
```

A imagem final instala `curl` para os healthchecks e executa a aplicação com um usuário não-root chamado `spring`.

O `SERVICE_NAME` é validado pelo Dockerfile e deve ser um dos módulos de aplicação suportados.

## Healthchecks

As aplicações expõem healthcheck via Actuator:

```text
/actuator/health
```

- `service-registry`: `http://localhost:8081/actuator/health` dentro do container;
- demais aplicações: `http://localhost:8080/actuator/health` dentro do container;
- MySQL: `mysqladmin ping`;
- RabbitMQ: `rabbitmq-diagnostics -q ping`.

O Compose também configura a exposição dos endpoints Actuator:

```text
health,info,prometheus
```

## Persistência

Os dados de MySQL e RabbitMQ ficam em volumes Docker nomeados. O nome final dos volumes no host normalmente recebe o prefixo do projeto informado por `-p`, por exemplo:

```text
unicos-local_mysql_pessoa_data
unicos-local_rabbitmq_data
```

Trocar o nome do projeto (`-p`) cria outro conjunto lógico de recursos e volumes.

## Segurança e arquivos locais

`.gitignore` ignora `.env` e `.env.*`, mantendo versionáveis apenas `.env.example` e arquivos no formato `.env.*.example`.

`.dockerignore` também exclui arquivos `.env*` do contexto de build, além de `target`, metadados de IDE, logs, ZIPs e READMEs.

Não versione credenciais ou tokens reais. O `.env.local` fornecido contém valores de desenvolvimento e deve permanecer restrito ao ambiente local.

### Atenções sobre os arquivos atuais

1. `SPRING_PROFILES_ACTIVE` está vazio no `.env.local`; nenhum profile Spring local é ativado explicitamente.
2. Não há `profiles:` de Docker Compose. O perfil `complementares` existente é Maven, não Compose.
3. RabbitMQ existe na composição, porém suas portas não são publicadas no host.
4. Os bancos são separados por domínio e todos são definidos no `compose.yml`.
5. `SPRING_DATASOURCE_USERNAME` está fixado como `unicos`; mantenha os `*_DB_USER` consistentes ou ajuste o Compose.
6. O usuário root de cada MySQL reutiliza a senha do usuário da aplicação.
7. `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_MANAGEMENT_PORT`, `*_DB_HOST`, `*_DB_PORT` e `EUREKA_URL` existem nos arquivos de ambiente, mas não são usados pelo Compose para montar as conexões internas atuais.
8. As variáveis Windows (`JAVA_HOME`, `MAVEN_HOME`, `OS`, `TEMP`, etc.) presentes no `.env.local` são carregadas nos containers de aplicação por causa de `env_file`; elas não são necessárias para o build Docker e podem ser removidas do arquivo de runtime se não forem utilizadas pela aplicação.

## Validação dos arquivos

Os arquivos YAML e o `pom.xml` podem ser validados estaticamente antes da execução. A validação efetiva do merge dos arquivos Compose deve ser feita no ambiente com Docker Compose instalado:

```bash
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml config --quiet
```

Para validar o build Java fora do Docker, com JDK 21 e Maven instalados:

```bash
mvn clean verify
```

ou, incluindo os módulos complementares:

```bash
mvn -Pcomplementares clean verify
```
