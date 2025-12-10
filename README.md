# 🧩 UniCoS — ERP Modular em Microserviços

## 📝 Descrição  
O **UniCoS (Unique Control System)** é um ERP moderno, modular e escalável, desenvolvido para atender empresas de pequeno e médio porte.  
Sua arquitetura é baseada em **microserviços independentes**, que se comunicam via REST e são integrados por:

- **Service Registry (Eureka)**  
- **API Gateway (Spring Cloud Gateway)**  
- **Módulo CORE** publicado como pacote Maven (GitHub Packages)  

O UniCoS oferece flexibilidade para evolução contínua de cada domínio, garantindo desacoplamento, alta disponibilidade e facilidade de manutenção.

---

## 🧠 Arquitetura

A estrutura geral do UniCoS segue este modelo:

```
                     ┌────────────────────────┐
                     │     Service Registry    │
                     │        (Eureka)         │
                     └────────────┬────────────┘
                                  │
                        ┌─────────┴─────────┐
                        │     API Gateway    │
                        │  (Spring Gateway)  │
                        └─────────┬─────────┘
                ┌────────────────┼──────────────────┐
                │                │                  │
        ┌────────────┐   ┌──────────────┐   ┌──────────────┐
        │  MS-Auth    │   │  MS-Pessoas   │   │ MS-Produtos   │
        └────────────┘   └──────────────┘   └──────────────┘
                │                │                  │
                └────────────────┴──────────────────┘
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
| **service-registry** | Registro e descoberta via Eureka |
| **gateway** | Roteamento global e autenticação |
| **ms-auth** | Gerencia usuários, perfis e permissões |
| **unicos-core** | Enums, modelos e contratos padrão |
| **ms-produtos** | Gerencia os produtos da empresa |
| **ms-pessoas** | Gerencia dados de pessoas físicas e jurídicas |

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
├── service-registry/
├── gateway/
├── unicos-core/
│   ├── core-base/
│   ├── core-categoria/
│   ├── core-contas/
│   ├── core-financeiro/
│   ├── core-pedido/
│   ├── core-produto/
│   ├── core-tesouraria/
├── ms-produtos/
├── ms-pessoas/
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
```bash
cd service-registry
mvn spring-boot:run
```
```bash
cd gateway
mvn spring-boot:run
```
```bash
cd ms-auth
mvn spring-boot:run
```
```bash
cd ms-produtos
mvn spring-boot:run
```
```bash
cd ms-pessoas
mvn spring-boot:run
```

---

# 📘 Swagger

A documentação é gerada por cada microserviço.

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
