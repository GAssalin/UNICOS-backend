# UNICOS Backend

Arquivos de raiz adaptados da referência ATCARE para os módulos UNICOS revisados nesta conversa. Use este pacote junto com as versões revisadas de `unicos-core`, `gateway`, `service-registry`, `ms-autenticacao` e `ms-pessoas`.

## Aplicação

Extraia os dez arquivos deste pacote **na raiz do UNICOS-backend**, ao lado das pastas dos serviços. Eles substituem os equivalentes da raiz; faça um commit ou backup antes. Não contém os fontes dos serviços.

O novo `pom.xml` é um agregador: não altere os parents dos POMs filhos para apontarem para ele. Mantém os parents Spring Boot de cada aplicação. A pasta `backend` da imagem não é agregada porque sua função/conteúdo não foi fornecido.

| Arquivo | Função |
|---|---|
| `.dockerignore` | Exclui ambientes, artefatos de build, ZIPs e metadados do contexto Docker |
| `.gitignore` | Ignora ambientes reais e `target`; permite versionar `.env.example` |
| `.env.example` | Modelo sem segredos, com campos obrigatórios vazios |
| `.env.local` | Ambiente Docker local com credenciais novas geradas para desenvolvimento |
| `compose.yml` | Serviços, rede, build, dependências e healthchecks |
| `compose.homolog.yml` | Publicação do gateway e Eureka, em loopback por padrão; serve também para local |
| `compose.prod.yml` | Perfil Spring prod, restrições de runtime e publicação somente do gateway |
| `Dockerfile.service` | Build Java 21 com reactor Maven e runtime JRE sem root, com curl para healthchecks |
| `pom.xml` | Agregador dos módulos revisados e perfil opcional dos demais |
| `README.md` | Instruções e limites desta adaptação |

As credenciais do ATCARE não foram copiadas. Foram retirados Config Server, credenciais Git, caminhos Java/Maven do Windows e variáveis dos serviços exclusivos do ATCARE. RabbitMQ não foi incluído: não há dependência dele nos fontes UNICOS revisados. Se outros módulos o utilizarem, sua infraestrutura deverá ser adicionada após conferir esses fontes.

## Módulos e alcance

| Grupo | Módulos |
|---|---|
| Padrão, com fontes já revisados | unicos-core, service-registry, gateway, ms-autenticacao, ms-pessoas |
| Perfil opcional `complementares` | ms-cliente, ms-empresa, ms-estoque, ms-permissao, ms-produto |

O core é compilado como biblioteca e não tem container. A composição padrão inclui somente um banco: `mysql-pessoas`. A autenticação obtém o usuário via Feign em ms-pessoas e não possui banco próprio.

A composição padrão é uma **base para os módulos revisados**, não a operação completa do backend: ms-permissao é necessário para várias operações de pessoas. Não há geração automática de usuários/roles iniciais. Em um banco novo, Hibernate cria o schema no perfil local, mas não carrega os seeds SQL nem cria automaticamente um usuário para login.

## Executar localmente

Requer Docker Desktop com Compose v2 e acesso aos repositórios de dependências/imagens. Os builds Docker fornecem JDK 21 e Maven, sem exigir instalação deles no host.

O `.env.local` entregue já tem credenciais locais próprias. Ele usa issuer `unicos-local`, access token de 15 minutos e refresh de 10080 minutos (7 dias): são valores propostos para um ambiente novo, não os valores recuperados do Config Server. Todos os emissores/validadores precisam usar os mesmos valores JWT. Para integrar com tokens existentes, use o segredo e issuer reais atuais; trocar esses valores invalida a validação dos tokens antigos.

Na raiz do backend, PowerShell:

```powershell
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local config --quiet
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local up -d --build
```

O comando `config --quiet` valida sem imprimir segredos. `--env-file` fornece os valores usados para interpolar o Compose. Os serviços revisados recebem somente as variáveis explicitamente declaradas em `environment`; não recebem todo o arquivo.

| Serviço | Porta interna | Acesso pelo host |
|---|---|---|
| gateway | 8082 | http://localhost:8082 |
| service-registry | 8081 | http://localhost:8081 |
| ms-autenticacao | 8083 | Via gateway |
| ms-pessoas | 8085 | Via gateway |
| mysql-pessoas | 3306 | Rede Docker, sem porta publicada |

Gateway e Eureka são publicados em `127.0.0.1` por padrão. Para acesso de outra máquina em homologação, ajuste `GATEWAY_BIND_ADDRESS`/`EUREKA_BIND_ADDRESS` conforme o ambiente e sua política de acesso.

Login pelo gateway: `POST /ms-autenticacao/v1/autenticacao/login`, com email/senha de usuário existente. Operações protegidas de pessoas usam `/ms-pessoas/...` e JWT válido. A URL do Eureka dentro dos containers usa `service-registry`, não `localhost`.

```powershell
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local ps
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local logs -f ms-autenticacao ms-pessoas
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml -p unicos-local down
```

`down` preserva o volume. Evite acrescentar `-v` se quiser manter os dados. O novo volume `unicos-local_mysql_pessoas_data` não migra ou reutiliza automaticamente bancos dos Compose antigos. Para conservar dados existentes, faça backup/migração ou adapte o volume explicitamente antes do primeiro uso. Alterar as credenciais no arquivo não altera usuários de um MySQL já inicializado.

## Banco e perfis

O Compose constrói `PESSOA_DB_URL` a partir de `PESSOA_DB_NAME` e usa o mesmo nome ao inicializar o MySQL. Usuário da aplicação e senha root são separados. O healthcheck realiza uma consulta autenticada no banco da aplicação.

- `local`: o ms-pessoas revisado usa Hibernate `update` e Flyway desligado.
- `homolog`: o ms-pessoas revisado usa o padrão `validate` e Flyway desligado; exige schema existente.
- `prod`: o override força Hibernate `validate` e Flyway desligado; exige schema existente.

As migrations UNICOS existentes estão diretamente em `db/migration`; não foi copiada a organização `common/local/homolog/prod` do ATCARE. Planeje a evolução e inicialização do schema antes de subir homolog/prod. Esse Compose não aplica seeds nem transforma automaticamente o banco de desenvolvimento em produção.

Para homologação, preencha um novo `.env.homolog` baseado no exemplo, com `SPRING_PROFILES_ACTIVE=homolog`, e utilize `-p unicos-homolog`. Isso cria uma composição e volume separados.

## Perfil complementares — templates a conferir

Os cinco serviços extras existem na estrutura mostrada, mas seus fontes e configurações ainda não foram enviados. O Compose fornece templates de build/rede/Eureka/JWT com porta interna 8080; **não presume banco, mensageria ou variáveis de domínio desses módulos**.

Antes de ativar:

1. Disponibilize as cinco pastas e POMs na raiz. O perfil Maven agrega todas elas, mesmo quando o build seleciona um serviço.
2. Migre cada módulo para configuração local, removendo efetivamente Config Client/imports. Este pacote não altera esses fontes nem desativa a dependência por atalhos de ambiente.
3. Configure `CLIENTE_ENV_FILE`, `EMPRESA_ENV_FILE`, `ESTOQUE_ENV_FILE`, `PERMISSAO_ENV_FILE` e `PRODUTO_ENV_FILE` para arquivos específicos de cada serviço. O valor `.env.local` é apenas um ponto de partida local, sem as configurações desconhecidas.
4. Informe nesses arquivos as variáveis consumidas pelo módulo, conexões de banco e mensageria. Bancos/filas adicionais precisam existir em destinos alcançáveis pela rede Docker ou ser adicionados ao Compose.
5. Confira Actuator `/actuator/health` acessível na porta interna 8080, dependências entre serviços e contratos de segurança. O template atualmente aguarda somente Eureka.

Após esses ajustes:

```powershell
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml --profile complementares -p unicos-local config --quiet
docker compose --env-file .env.local -f compose.yml -f compose.homolog.yml --profile complementares -p unicos-local up -d --build
```

O Compose passa `MAVEN_PROFILES=complementares` ao build desses serviços. No Maven do host, o equivalente é `mvn -Pcomplementares -pl ms-permissao -am clean verify`.

## Produção

Use `.env.prod` próprio, com credenciais reais, issuer correto, `SPRING_PROFILES_ACTIVE=prod` e schema preparado. Não reutilize os segredos de desenvolvimento. Se utilizar complementares, configure arquivos de ambiente específicos desse ambiente.

```powershell
docker compose --env-file .env.prod -f compose.yml -f compose.prod.yml -p unicos-prod config --quiet
```

Após validação no seu ambiente, a inicialização segue com `up -d --build` nos mesmos arquivos. Acrescente `--profile complementares` somente após revisar os módulos extras. Não combine `compose.homolog.yml` com `compose.prod.yml`: a publicação do Eureka do primeiro poderia permanecer no resultado.

O override usa filesystem somente leitura, `/tmp` temporário, limites de recursos e logs rotacionados para as aplicações. MySQL preserva seu volume gravável. Somente gateway publica porta e o bind padrão é loopback, para acesso por proxy no host. Proxy/TLS, backup e monitoramento não são provisionados por estes arquivos.

Este override é uma configuração de infraestrutura, **não uma certificação de prontidão para produção**. Os fontes revisados de ms-pessoas ainda confiam em headers de identidade e liberam endpoints internos; essas pendências já foram identificadas no guia anterior e exigem correção coordenada. Healthchecks saudáveis também não garantem que todos os fluxos e serviços dependentes estejam disponíveis.

## Build e validação

O Dockerfile usa o POM agregador, `-pl SERVICO -am`, para compilar o serviço e seus cores no mesmo reactor. O POM não redefine versões dos filhos. As versões Spring Boot dos módulos ainda não foram unificadas. O empacotamento Docker pula testes; execute `clean verify` separadamente com JDK 21 antes de implantar.

Exemplo usando o wrapper existente, na raiz:

```powershell
.\unicos-core\mvnw.cmd -f pom.xml -pl ms-autenticacao -am clean verify
```

Se os fontes dependem de outros artefatos privados fora do reactor, configure o acesso Maven apropriadamente. Credenciais dos arquivos de ambiente não são copiadas para a imagem, e este Dockerfile não incorpora tokens Git/GitHub.

Validação feita nesta entrega: parsing XML/YAML, módulos padrão contra os diretórios revisados, interpolação estática das variáveis com `.env.local`, referências de rede/volume/dependências, portas dos healthchecks e conteúdo do ZIP. **Não foi executado `docker compose config`, build Docker nem inicialização**, pois Docker/Compose não está instalado neste ambiente. Os builds Java dos serviços também continuam pendentes, conforme as entregas anteriores.

Referências: [variáveis e interpolação do Compose](https://docs.docker.com/compose/how-tos/environment-variables/variable-interpolation/) e [perfis opcionais](https://docs.docker.com/compose/how-tos/profiles/).
