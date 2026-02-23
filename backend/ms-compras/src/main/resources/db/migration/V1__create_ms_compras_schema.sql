-- V1__create_ms_compras_schema.sql
-- Banco alvo: MySQL 8+ (InnoDB / utf8mb4)
-- Observação: todas as tabelas incluem colunas de multi-tenant e auditoria (BaseTenantEntity/EntidadeAuditavel).

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- 1) FORNECEDOR
-- =========================================================
CREATE TABLE fornecedor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(30) NOT NULL,
    razao_social VARCHAR(200) NOT NULL,
    nome_fantasia VARCHAR(200) NULL,
    cnpj VARCHAR(18) NOT NULL,
    inscricao_estadual VARCHAR(30) NULL,
    email VARCHAR(200) NULL,
    telefone VARCHAR(30) NULL,
    observacao VARCHAR(500) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT uk_fornecedor_codigo UNIQUE (codigo),
    CONSTRAINT uk_fornecedor_cnpj UNIQUE (cnpj),
    INDEX ix_fornecedor_empresa_id (empresa_id),
    INDEX ix_fornecedor_nome_fantasia (nome_fantasia),
    INDEX ix_fornecedor_razao_social (razao_social)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 2) ENDERECO_FORNECEDOR
-- =========================================================
CREATE TABLE endereco_fornecedor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fornecedor_id BIGINT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100) NULL,
    bairro VARCHAR(120) NOT NULL,
    cidade VARCHAR(120) NOT NULL,
    uf CHAR(2) NOT NULL,
    observacao VARCHAR(300) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_endereco_fornecedor_fornecedor
        FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id),
    INDEX ix_endereco_fornecedor_empresa_id (empresa_id),
    INDEX ix_endereco_fornecedor_fornecedor_id (fornecedor_id),
    INDEX ix_endereco_fornecedor_cep (cep)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 3) CONTATO_FORNECEDOR
-- =========================================================
CREATE TABLE contato_fornecedor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fornecedor_id BIGINT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    cargo VARCHAR(100) NULL,
    telefone VARCHAR(30) NULL,
    celular VARCHAR(30) NULL,
    email VARCHAR(200) NULL,
    principal TINYINT(1) NOT NULL,
    observacao VARCHAR(400) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_contato_fornecedor_fornecedor
        FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id),
    INDEX ix_contato_fornecedor_empresa_id (empresa_id),
    INDEX ix_contato_fornecedor_fornecedor_id (fornecedor_id),
    INDEX ix_contato_fornecedor_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 4) CONDICAO_PAGAMENTO
-- =========================================================
CREATE TABLE condicao_pagamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(30) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(500) NULL,
    parcelado TINYINT(1) NOT NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT uk_condicao_pagamento_codigo UNIQUE (codigo),
    INDEX ix_condicao_pagamento_empresa_id (empresa_id),
    INDEX ix_condicao_pagamento_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 5) CONDICAO_PAGAMENTO_PARCELA
-- =========================================================
CREATE TABLE condicao_pagamento_parcela (
    id BIGINT NOT NULL AUTO_INCREMENT,
    condicao_pagamento_id BIGINT NOT NULL,
    ordem INT NOT NULL,
    dias_apos_emissao INT NOT NULL,
    percentual DECIMAL(5,2) NOT NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_cond_pag_parcela_condicao_pagamento
        FOREIGN KEY (condicao_pagamento_id) REFERENCES condicao_pagamento(id),
    CONSTRAINT uk_cond_pag_parcela_condicao_ordem UNIQUE (condicao_pagamento_id, ordem),
    INDEX ix_condicao_pagamento_parcela_empresa_id (empresa_id),
    INDEX ix_cond_pag_parcela_condicao_id (condicao_pagamento_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 6) PEDIDO_COMPRA
-- =========================================================
CREATE TABLE pedido_compra (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(30) NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    data_emissao DATE NOT NULL,
    data_prevista_entrega DATE NULL,
    status_pedido_compra VARCHAR(30) NOT NULL,
    condicao_pagamento_id BIGINT NULL,
    observacao VARCHAR(500) NULL,
    subtotal DECIMAL(19,2) NOT NULL,
    desconto DECIMAL(19,2) NOT NULL,
    frete DECIMAL(19,2) NOT NULL,
    total DECIMAL(19,2) NOT NULL,
    aprovado_por BIGINT NULL,
    aprovado_em TIMESTAMP NULL,
    motivo_cancelamento VARCHAR(40) NULL,
    observacao_cancelamento VARCHAR(300) NULL,
    cancelado_por BIGINT NULL,
    cancelado_em TIMESTAMP NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT uk_pedido_compra_codigo UNIQUE (codigo),
    CONSTRAINT fk_pedido_compra_fornecedor
        FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id),
    CONSTRAINT fk_pedido_compra_condicao_pagamento
        FOREIGN KEY (condicao_pagamento_id) REFERENCES condicao_pagamento(id),
    INDEX ix_pedido_compra_empresa_id (empresa_id),
    INDEX ix_pedido_compra_fornecedor_id (fornecedor_id),
    INDEX ix_pedido_compra_status (status_pedido_compra),
    INDEX ix_pedido_compra_data_emissao (data_emissao),
    INDEX ix_pedido_compra_fornecedor_status (fornecedor_id, status_pedido_compra),
    INDEX ix_pedido_compra_empresa_data_emissao (empresa_id, data_emissao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 7) ITEM_PEDIDO_COMPRA
-- =========================================================
CREATE TABLE item_pedido_compra (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pedido_compra_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    produto_descricao_snapshot VARCHAR(250) NULL,
    unidade_snapshot VARCHAR(10) NULL,
    quantidade DECIMAL(19,4) NOT NULL,
    preco_unitario DECIMAL(19,2) NOT NULL,
    desconto_item DECIMAL(19,2) NOT NULL,
    total_item DECIMAL(19,2) NOT NULL,
    observacao VARCHAR(300) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_item_pedido_compra_pedido
        FOREIGN KEY (pedido_compra_id) REFERENCES pedido_compra(id),
    INDEX ix_item_pedido_compra_empresa_id (empresa_id),
    INDEX ix_item_pedido_compra_pedido_id (pedido_compra_id),
    INDEX ix_item_pedido_compra_produto_id (produto_id),
    INDEX ix_item_pedido_compra_pedido_produto (pedido_compra_id, produto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 8) COTACAO_COMPRA
-- =========================================================
CREATE TABLE cotacao_compra (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(30) NOT NULL,
    data_abertura DATE NOT NULL,
    data_validade DATE NOT NULL,
    status VARCHAR(30) NOT NULL,
    observacao VARCHAR(500) NULL,
    pedido_compra_id BIGINT NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT uk_cotacao_compra_codigo UNIQUE (codigo),
    CONSTRAINT fk_cotacao_compra_pedido_compra
        FOREIGN KEY (pedido_compra_id) REFERENCES pedido_compra(id),
    INDEX ix_cotacao_compra_empresa_id (empresa_id),
    INDEX ix_cotacao_compra_status (status),
    INDEX ix_cotacao_compra_data_abertura (data_abertura),
    INDEX ix_cotacao_compra_data_validade (data_validade),
    INDEX ix_cotacao_compra_empresa_status (empresa_id, status),
    INDEX ix_cotacao_compra_pedido_compra_id (pedido_compra_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 9) ITEM_COTACAO
-- =========================================================
CREATE TABLE item_cotacao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cotacao_compra_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    produto_descricao_snapshot VARCHAR(250) NULL,
    unidade_snapshot VARCHAR(10) NULL,
    quantidade DECIMAL(19,4) NOT NULL,
    observacao VARCHAR(300) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_item_cotacao_cotacao
        FOREIGN KEY (cotacao_compra_id) REFERENCES cotacao_compra(id),
    INDEX ix_item_cotacao_empresa_id (empresa_id),
    INDEX ix_item_cotacao_cotacao_id (cotacao_compra_id),
    INDEX ix_item_cotacao_produto_id (produto_id),
    INDEX ix_item_cotacao_cotacao_produto (cotacao_compra_id, produto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 10) RESPOSTA_COTACAO_FORNECEDOR
-- =========================================================
CREATE TABLE resposta_cotacao_fornecedor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cotacao_compra_id BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    respondido_em TIMESTAMP NULL,
    condicao_pagamento_id BIGINT NULL,
    total_proposto DECIMAL(19,2) NOT NULL,
    frete DECIMAL(19,2) NOT NULL,
    observacao VARCHAR(500) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT uk_resposta_cotacao_fornecedor_cotacao_fornecedor UNIQUE (cotacao_compra_id, fornecedor_id),
    CONSTRAINT fk_resposta_cotacao_fornecedor_cotacao
        FOREIGN KEY (cotacao_compra_id) REFERENCES cotacao_compra(id),
    CONSTRAINT fk_resposta_cotacao_fornecedor_fornecedor
        FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id),
    CONSTRAINT fk_resposta_cotacao_fornecedor_condicao_pagamento
        FOREIGN KEY (condicao_pagamento_id) REFERENCES condicao_pagamento(id),
    INDEX ix_resposta_cotacao_fornecedor_empresa_id (empresa_id),
    INDEX ix_resposta_cotacao_fornecedor_cotacao_id (cotacao_compra_id),
    INDEX ix_resposta_cotacao_fornecedor_fornecedor_id (fornecedor_id),
    INDEX ix_resposta_cotacao_fornecedor_status (status),
    INDEX ix_resposta_cotacao_fornecedor_cotacao_status (cotacao_compra_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 11) ITEM_RESPOSTA_COTACAO_FORNECEDOR
-- =========================================================
CREATE TABLE item_resposta_cotacao_fornecedor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    resposta_cotacao_fornecedor_id BIGINT NOT NULL,
    item_cotacao_id BIGINT NOT NULL,
    preco_unitario DECIMAL(19,2) NOT NULL,
    desconto_item DECIMAL(19,2) NOT NULL,
    total_item DECIMAL(19,2) NOT NULL,
    prazo_entrega_dias INT NULL,
    observacao VARCHAR(300) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT uk_item_resposta_cotacao_fornecedor_resposta_itemcotacao UNIQUE (resposta_cotacao_fornecedor_id, item_cotacao_id),
    CONSTRAINT fk_item_resposta_cotacao_fornecedor_resposta
        FOREIGN KEY (resposta_cotacao_fornecedor_id) REFERENCES resposta_cotacao_fornecedor(id),
    CONSTRAINT fk_item_resposta_cotacao_fornecedor_item_cotacao
        FOREIGN KEY (item_cotacao_id) REFERENCES item_cotacao(id),
    INDEX ix_item_resposta_cotacao_fornecedor_empresa_id (empresa_id),
    INDEX ix_item_resposta_cotacao_fornecedor_resposta_id (resposta_cotacao_fornecedor_id),
    INDEX ix_item_resposta_cotacao_fornecedor_item_cotacao_id (item_cotacao_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 12) RECEBIMENTO_COMPRA
-- =========================================================
CREATE TABLE recebimento_compra (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pedido_compra_id BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    data_recebimento DATE NOT NULL,
    status VARCHAR(30) NOT NULL,
    observacao VARCHAR(500) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_recebimento_compra_pedido
        FOREIGN KEY (pedido_compra_id) REFERENCES pedido_compra(id),
    CONSTRAINT fk_recebimento_compra_fornecedor
        FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id),
    INDEX ix_recebimento_compra_empresa_id (empresa_id),
    INDEX ix_recebimento_compra_pedido_id (pedido_compra_id),
    INDEX ix_recebimento_compra_fornecedor_id (fornecedor_id),
    INDEX ix_recebimento_compra_status (status),
    INDEX ix_recebimento_compra_data_recebimento (data_recebimento),
    INDEX ix_recebimento_compra_empresa_data (empresa_id, data_recebimento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 13) ITEM_RECEBIMENTO_COMPRA
-- =========================================================
CREATE TABLE item_recebimento_compra (
    id BIGINT NOT NULL AUTO_INCREMENT,
    recebimento_compra_id BIGINT NOT NULL,
    item_pedido_compra_id BIGINT NOT NULL,
    quantidade_recebida DECIMAL(19,4) NOT NULL,
    quantidade_aprovada DECIMAL(19,4) NOT NULL,
    quantidade_recusada DECIMAL(19,4) NOT NULL,
    observacao VARCHAR(300) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_item_recebimento_compra_recebimento
        FOREIGN KEY (recebimento_compra_id) REFERENCES recebimento_compra(id),
    CONSTRAINT fk_item_recebimento_compra_item_pedido
        FOREIGN KEY (item_pedido_compra_id) REFERENCES item_pedido_compra(id),
    INDEX ix_item_recebimento_compra_empresa_id (empresa_id),
    INDEX ix_item_recebimento_compra_recebimento_id (recebimento_compra_id),
    INDEX ix_item_recebimento_compra_item_pedido_id (item_pedido_compra_id),
    INDEX ix_item_recebimento_compra_recebimento_itempedido (recebimento_compra_id, item_pedido_compra_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 14) DIVERGENCIA_RECEBIMENTO
-- =========================================================
CREATE TABLE divergencia_recebimento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    item_recebimento_compra_id BIGINT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    quantidade_divergente DECIMAL(19,4) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_divergencia_recebimento_item_recebimento
        FOREIGN KEY (item_recebimento_compra_id) REFERENCES item_recebimento_compra(id),
    INDEX ix_divergencia_recebimento_empresa_id (empresa_id),
    INDEX ix_divergencia_recebimento_item_recebimento_id (item_recebimento_compra_id),
    INDEX ix_divergencia_recebimento_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- 15) DOCUMENTO_ENTRADA
-- =========================================================
CREATE TABLE documento_entrada (
    id BIGINT NOT NULL AUTO_INCREMENT,
    recebimento_compra_id BIGINT NOT NULL,
    tipo_documento VARCHAR(30) NOT NULL,
    numero VARCHAR(30) NOT NULL,
    serie VARCHAR(10) NULL,
    chave_acesso VARCHAR(60) NULL,
    data_emissao DATE NULL,
    arquivo_ref VARCHAR(500) NULL,
    observacao VARCHAR(300) NULL,

    -- auditoria + tenant
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_documento_entrada_recebimento
        FOREIGN KEY (recebimento_compra_id) REFERENCES recebimento_compra(id),
    INDEX ix_documento_entrada_empresa_id (empresa_id),
    INDEX ix_documento_entrada_recebimento_id (recebimento_compra_id),
    INDEX ix_documento_entrada_tipo (tipo_documento),
    INDEX ix_documento_entrada_numero (numero),
    INDEX ix_documento_entrada_chave_acesso (chave_acesso)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;