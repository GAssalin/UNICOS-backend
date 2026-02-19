# 🧩 UniCoS — ERP Modular em Microserviços

## 📝 Descrição  
O **UniCoS (Unique Control System)** é um ERP moderno, modular e escalável, desenvolvido para atender empresas de pequeno e médio porte.  
Sua arquitetura é baseada em **microserviços independentes**, que se comunicam via REST e são integrados por:

- **Config Server**
- **Service Registry (Eureka)**  
- **API Gateway (Spring Cloud Gateway)**
- **Micro Serviços**
- **Módulo CORE** publicado como pacote Maven (GitHub Packages)  

O UniCoS oferece flexibilidade para evolução contínua de cada domínio, garantindo desacoplamento, alta disponibilidade e facilidade de manutenção.

---

## 🧠 Arquitetura

A estrutura geral do UniCoS segue este modelo:

```
    ┌────────────────────────┐
    │      Config Server     │
    └────────────┬───────────┘
                 │
    ┌────────────┴───────────┐
    │     Service Registry   │
    │        (Eureka)        │
    └────────────┬───────────┘
                 │
       ┌─────────┴─────────┐
       │     API Gateway   │
       │  (Spring Gateway) │
       └─────────┬─────────┘
                 |                ┌────────────┐
                 └────────────────│  MS-Auth   │
                 |                └────────────┘
                 |                ┌────────────────┐
                 └────────────────│  MS-Permissao  │
                 |                └────────────────┘
                 |                ┌───────────────┐
                 └────────────────│  MS-Usuario   │
                 |                └───────────────┘
                 |                ┌───────────────┐
                 └────────────────│  MS-Pessoas   │
                 |                └───────────────┘
                 |                ┌──────────────────┐
                 └────────────────│  MS-Notificacao  │
                 |                └──────────────────┘
                 |                ┌───────────────┐
                 └────────────────│  MS-Empresa   │
                 |                └───────────────┘
                 |                ┌─────────────┐
                 └────────────────│  MS-Filial  │
                 |                └─────────────┘
                 |                ┌───────────────────┐
                 └────────────────│  MS-Departamento  │
                 |                └───────────────────┘
    ┌────────────────────────┐
    │        CORE (DDD)      │
    │  Publicado no GitHub   │
    └────────────────────────┘
```

---

## 🚀 Funcionalidades Principais (MVP)
✔️ Autenticação e permissões
✔️ Gerenciamento de dados pessoais
✔️ Gestão de produtos

---

## 🧩 Microserviços do UniCoS

| Serviço | Responsabilidade |
|--------|------------------|
| **Config Server** | Spring Cloud Config Server do UniCoS para centralização e distribuição de configurações dos microserviços. |
| **service-registry** | Eureka Server para registro e descoberta de microserviços. |
| **gateway** | Gateway reativo do UNICOS com roteamento dinâmico via Spring Cloud Gateway e integração ao Eureka Server. |
| **ms-auth** | Microserviço responsável pela autenticação, autorização e emissão de tokens JWT. |
| **ms-permissao** | Microserviço responsável pelo gerenciamento de perfis e permissões. |
| **ms-usuario** | Microserviço responsável pela gerenciamento de usuários. |
| **ms-pessoas** | Microserviço responsável pela gerenciamento de dados de pessoas físicas e jurídicas. |
| **ms-notificacao** | Microserviço responsável pelo envio e gerenciamento de notificações da plataforma. |
| **ms-empresa** | Microserviço de gerenciamento de empresas (tenants) do UniCoS. |
| **ms-filial** | Microserviço responsável pela gestão de filiais das empresas, representando unidades organizacionais e operacionais. |
| **ms-departamento** | Microserviço responsável pela gestão de departamentos organizacionais das filiais. |
| **ms-vendas** | Microserviço responsável pela gestão de vendas, pedidos e operações comerciais da plataforma. |
| **unicos-core** | Módulo principal de domínios e componentes centrais do ecossistema UniCoS (Unique Control System). |

---

## 🔧 Tecnologias Utilizadas
- **Backend:** Java 21 + Spring Boot 3
- **Comunicação:** REST + Spring Cloud
- **Orquestração:** Eureka + Gateway
- **Banco de Dados:** Flyway + MySQL
- **Build:** Maven
- **Deploy:** Docker / Docker Compose (Em Construção)
- **Infra futura:** Kubernetes (Em Construção)

---

## 📦 Módulo CORE no GitHub Packages

O `unicos-core` é publicado como dependência Maven:

```xml
<dependency>
    <groupId>br.com.unicos</groupId>
    <artifactId>core-base</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Repositório usado pelos microserviços:

```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/GAssalin/UNICOS</url>
</repository>
```

---

## 🏗️ Estrutura do Projeto

```
backend/
├── config-server/
├── service-registry/
├── gateway/
├── unicos-core/
│   ├── core-base/
│   ├── core-produto/
│   ├── core-request/
│   ├── core-tenant/
│   ├── core-usuario/
├── ms-auth/
├── ms-permissao/
├── ms-usuario/
├── ms-pessoas/
├── ms-notificacao/
├── ms-empresa/
├── ms-filial/
├── ms-departamento/
├── ms-vendas/
```

---

# ▶️ Como Executar o Projeto

## Backend

### ✅ Rodando com Docker (Em Construção)
```bash
git clone https://github.com/GAssalin/ERP.git
cd backend
make up
```

Ou:

```bash
./run.sh        # Linux/macOS/WSL
run.bat         # Windows
```

Comandos úteis:

```bash
make logs
make down
```

---

### ⚙️ Rodando localmente sem Docker
OBS.: Deve-se respeitar a ordem de execução para os 3 primeiros abaixo
```bash
cd config-server
mvn spring-boot:run
```
```bash
cd service-registry
mvn spring-boot:run
```
```bash
cd gateway
mvn spring-boot:run
```
OBS.: MS-Auth, MS-Permissao e MS-Usuario devem estar rodando em conjunto para que a autenticação e geração do token ocorra.
```bash
cd ms-auth
mvn spring-boot:run
```
```bash
cd ms-permissao
mvn spring-boot:run
```
```bash
cd ms-usuario
mvn spring-boot:run
```

```bash
cd ms-pessoas
mvn spring-boot:run
```
```bash
cd ms-notificacao
mvn spring-boot:run
```
```bash
cd ms-empresa
mvn spring-boot:run
```
```bash
cd ms-filial
mvn spring-boot:run
```
```bash
cd ms-departamento
mvn spring-boot:run
```

---

# 📘 Swagger — Documentação Centralizada

A documentação de todos os microsserviços do UniCoS é unificada pelo API Gateway.

🔗 Acesse a documentação completa aqui:

👉 http://localhost:8082/docs

Este endpoint exibe uma página com links diretos para os Swagger UI de cada microserviço, detectados automaticamente via Eureka.

O endpoint /docs funciona como um hub centralizado, facilitando testes, inspeção e navegação entre APIs sem precisar acessar portas individuais.

---

# 🤝 Contribuição

```bash
git checkout -b feature/nova-feature
git push origin feature/nova-feature
```

---

# 📄 Licença  
A definir

---

# 📬 Contato  
Gustavo Soares Assalin  
GitHub: https://github.com/GAssalin

LinkedIn: https://www.linkedin.com/in/gustavo-assalin/
