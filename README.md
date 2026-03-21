# 🧩 UniCoS — Modular ERP Platform (Microservices Architecture)

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Architecture](https://img.shields.io/badge/Architecture-Microservices-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)
![License](https://img.shields.io/badge/License-TBD-lightgrey)

## 📖 Overview

**UniCoS (Unique Control System)** é uma plataforma **ERP modular baseada em microserviços**, projetada para empresas de pequeno e médio porte.

O sistema foi desenvolvido seguindo princípios de:

- **Domain Driven Design (DDD)**
- **Arquitetura de Microserviços**
- **Configuração centralizada**
- **Descoberta dinâmica de serviços**
- **Escalabilidade horizontal**

Cada domínio da aplicação é implementado como um **microserviço independente**, permitindo evolução e deploy desacoplado.

---

# 🧠 Arquitetura

A plataforma utiliza o ecossistema **Spring Cloud** para orquestração dos serviços.

### Componentes principais

| Componente | Função |
|-------------|--------|
| **Config Server** | Centraliza configurações dos serviços |
| **Service Registry (Eureka)** | Registro e descoberta dinâmica |
| **API Gateway** | Ponto único de entrada |
| **Microservices** | Domínios independentes |
| **UniCoS Core** | Biblioteca compartilhada entre serviços |

---

## 📊 Arquitetura Simplificada

```
                ┌─────────────────────┐
                │    Config Server    │
                └──────────┬──────────┘
                           │
                ┌──────────▼──────────┐
                │  Service Registry   │
                │      (Eureka)       │
                └──────────┬──────────┘
                           │
                 ┌─────────▼─────────┐
                 │    API Gateway    │
                 │ Spring Cloud GW   │
                 └─────────┬─────────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼

   MS-AUTH           MS-USUARIO          MS-PERMISSAO
   MS-PESSOAS        MS-EMPRESA          MS-FILIAL
   MS-DEPARTAMENTO   MS-PRODUTO          MS-ESTOQUE
   MS-VENDAS         MS-COMPRAS          MS-NOTIFICACAO

                 ┌───────────────────┐
                 │     UniCoS CORE   │
                 │ Shared Libraries  │
                 └───────────────────┘
```

---

# 🧩 Microserviços

| Serviço | Responsabilidade |
|-------|----------------|
| **config-server** | Centralização das configurações via Spring Cloud Config |
| **service-registry** | Descoberta de serviços usando Eureka |
| **gateway** | API Gateway reativo baseado em Spring Cloud Gateway |
| **ms-auth** | Autenticação e emissão de JWT |
| **ms-permissao** | Gerenciamento de permissões e perfis |
| **ms-usuario** | Gerenciamento de usuários |
| **ms-pessoas** | Cadastro de pessoas físicas e jurídicas |
| **ms-empresa** | Gestão de empresas (multi-tenant) |
| **ms-filial** | Gestão de filiais |
| **ms-departamento** | Gestão de departamentos |
| **ms-produto** | Cadastro de produtos |
| **ms-estoque** | Gestão de estoque |
| **ms-vendas** | Gestão de vendas |
| **ms-compras** | Gestão de compras |
| **ms-notificacao** | Sistema de notificações |

---

# 🧱 UniCoS Core

O módulo **UniCoS Core** contém componentes reutilizáveis entre microserviços:

- DTOs compartilhados
- Estruturas de domínio
- Exceptions
- Helpers
- Infraestrutura base

Publicado via **GitHub Packages**.

### Dependência Maven

```xml
<dependency>
    <groupId>br.com.unicos</groupId>
    <artifactId>core-base</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Repositório

```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/GAssalin/UNICOS</url>
</repository>
```

---

# 🛠️ Tecnologias

| Tecnologia | Uso |
|-------------|-----|
| Java 21 | Linguagem |
| Spring Boot 3 | Framework |
| Spring Cloud | Arquitetura de microserviços |
| Spring Cloud Gateway | API Gateway |
| Eureka | Service Discovery |
| MySQL | Banco de dados |
| Flyway | Versionamento de banco |
| Maven | Build |
| Docker | Containerização |

---

# 📂 Estrutura do Projeto

```
backend
│
├── config-server
├── service-registry
├── gateway
│
├── unicos-core
│   ├── core-base
│   ├── core-produto
│   ├── core-request
│   ├── core-tenant
│   ├── core-usuario
│
├── ms-auth
├── ms-permissao
├── ms-usuario
├── ms-pessoas
├── ms-notificacao
├── ms-empresa
├── ms-filial
├── ms-departamento
├── ms-estoque
├── ms-vendas
├── ms-compras
```

---

# 🚀 Executando o Projeto

## 📦 Rodando com Docker (Recomendado)

```bash
cd backend
docker compose --env-file .env.docker -f docker-compose.base.yml up --build -d
```

Isso iniciará:

- MySQL
- Config Server
- Service Registry
- API Gateway

---

```bash
cd backend
docker compose --env-file .env.docker -f docker-compose.login.yml up --build -d
```

Isso iniciará:

- ms-autenticacao
- ms-permissao
- ms-pessoas
- ms-usuario

---

```bash
cd backend
docker compose --env-file .env.docker -f docker-compose.{X} up --build -d
```

Isso iniciará o micro serviço desejado: {X}
Exemplo: docker compose -f docker-compose.ms-estoque up --build -d

---

## 📦 Parando com Docker (Recomendado)
```bash
docker compose --env-file .env.docker -f docker-compose.base.yml down
docker compose --env-file .env.docker -f docker-compose.login.yml down
docker compose --env-file .env.docker -f docker-compose.{X}.yml down
```

# 📘 Documentação das APIs

A documentação é centralizada via **API Gateway**.

### Acesso

```
http://localhost:8082/docs
```

Esse endpoint lista automaticamente os Swagger de todos os microserviços registrados no Eureka.

---

# ❤️ Health Check

Todos os serviços expõem endpoints do **Spring Boot Actuator**.

Exemplo:

```
/actuator/health
```

Utilizado pelo **Docker Compose** para verificar disponibilidade dos serviços.

---

# 🔐 Segurança

Autenticação baseada em:

- **JWT**
- **Gateway como filtro de autenticação**
- **Microserviços de autenticação e permissão dedicados**

---

# 🤝 Contribuição

1️⃣ Crie uma branch

```
git checkout -b feature/nova-feature
```

2️⃣ Commit

```
git commit -m "feat: nova funcionalidade"
```

3️⃣ Push

```
git push origin feature/nova-feature
```

---

# 📄 Licença

A definir.

---

# 👨‍💻 Autor

**Gustavo Soares Assalin**

GitHub  
https://github.com/GAssalin

LinkedIn  
https://www.linkedin.com/in/gustavo-assalin