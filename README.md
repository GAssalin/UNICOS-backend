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
                        │   API Gateway     │
                        │ (Spring Gateway)  │
                        └─────────┬─────────┘
      ┌──────────────┬────────────┼───────────────┬──────────────┐
      │              │            │               │              │
┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐  ┌────────────┐
│ MS-Auth  │   │ MS-Pessoa │   │ MS-Estoque│   │ MS-Compras│  │ MS-Financeiro │
└──────────┘   └──────────┘   └──────────┘   └──────────┘  └────────────┘
        │              │            │               │              │
        └──────────────┴────────────┴───────────────┴──────────────┘
                         ┌────────────────────────┐
                         │        CORE (DDD)      │
                         │  Publicado no GitHub   │
                         └────────────────────────┘
```

---

## 🚀 Funcionalidades Principais (MVP)
✔️ Gestão de produtos  
✔️ Gestão de empresas  
✔️ Catálogo de estoque  
✔️ Compras e fornecedores  
✔️ Módulo financeiro básico  
✔️ Módulo de pagamentos  
✔️ Módulo de notificações  
✔️ Autenticação e permissões  
✔️ Núcleo CORE com entidades, enums e contratos reutilizáveis  

---

## 🧩 Microserviços do UniCoS

| Serviço | Responsabilidade |
|--------|------------------|
| **service-registry** | Registro e descoberta via Eureka |
| **gateway** | Roteamento global e autenticação |
| **ms-auth** | Login, usuários e permissões |
| **unicos-core** | Enums, modelos e contratos padrão |
| **ms-produtos** | Produtos, categorias e atributos |
| **ms-empresa** | Empresas, filiais e dados corporativos |
| **ms-estoque** | Estoque, lotes e movimentações |
| **ms-compras** | Pedidos de compra e fornecedores |
| **ms-financeiro** | Movimentos, contas e lançamentos |
| **ms-pagamento** | Processamento e integração de pagamentos |
| **ms-notificacao** | Eventos e envio de mensagens |

---

## 🔧 Tecnologias Utilizadas
- **Backend:** Java 21 + Spring Boot 3  
- **Comunicação:** REST + Spring Cloud  
- **Orquestração:** Eureka + Gateway  
- **Banco de Dados:** MySQL  
- **Build:** Maven  
- **Deploy:** Docker / Docker Compose  
- **Infra futura:** Kubernetes (opcional)  

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
│   ├── core-produto/
│   ├── core-financeiro/
│   └── ...
├── ms-produtos/
├── ms-estoque/
├── ms-empresa/
├── ms-compras/
├── ms-financeiro/
└── ms-pagamento/
```

---

# ▶️ Como Executar o Projeto

## Backend

### ✅ Rodando com Docker
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
