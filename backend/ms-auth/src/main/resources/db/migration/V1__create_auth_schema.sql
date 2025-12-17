-- ================================
--  SCHEMA AUTH - UniCoS
--  Flyway Migration V1 (AJUSTADO)
-- ================================

-- ============================================================
-- 1) TABELA: permissao
-- ============================================================
CREATE TABLE permissao (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),
    UNIQUE (nome),
    INDEX idx_permissao_empresa (empresa_id)
) ENGINE=InnoDB;

-- ============================================================
-- 2) TABELA: role
-- ============================================================
CREATE TABLE role (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(50) NOT NULL,
    descricao VARCHAR(255),

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),
    UNIQUE (nome),
    INDEX idx_role_empresa (empresa_id)
) ENGINE=InnoDB;

-- ============================================================
-- 3) TABELA: role_hierarchy_relation
-- ============================================================
CREATE TABLE role_hierarchy_relation (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    parent_role VARCHAR(100) NOT NULL,
    child_role VARCHAR(100) NOT NULL,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_parent_child_role
        UNIQUE (parent_role, child_role),

    INDEX idx_parent_role (parent_role),
    INDEX idx_child_role (child_role),
    INDEX idx_rhr_empresa (empresa_id)
) ENGINE=InnoDB;

-- ============================================================
-- 4) TABELA: usuario
-- ============================================================
CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    -- Identidade
    login VARCHAR(100) NOT NULL,
    pessoa_id BIGINT,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL,

    -- Segurança
    email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    refresh_token VARCHAR(300),
    expiracao_refresh_token DATETIME,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),
    UNIQUE (login),
    UNIQUE (email),
    INDEX idx_usuario_empresa (empresa_id)
) ENGINE=InnoDB;

-- ============================================================
-- 5) TABELA ASSOCIATIVA: usuario_role (AJUSTADA)
-- ============================================================
CREATE TABLE usuario_role (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    usuario_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_usuario_role
        UNIQUE (usuario_id, role_id),

    CONSTRAINT fk_usuario_role_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_role_role
        FOREIGN KEY (role_id)
        REFERENCES role(id)
        ON DELETE CASCADE,

    INDEX idx_ur_empresa (empresa_id),
    INDEX idx_ur_usuario (usuario_id),
    INDEX idx_ur_role (role_id)
) ENGINE=InnoDB;

-- ============================================================
-- 6) TABELA: usuario_email_verificacao
-- ============================================================
CREATE TABLE usuario_email_verificacao (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    usuario_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expiracao DATETIME NOT NULL,
    utilizado BOOLEAN NOT NULL DEFAULT FALSE,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT fk_usuario_email_verificacao_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE,

    INDEX idx_uev_empresa (empresa_id),
    INDEX idx_uev_usuario (usuario_id)
) ENGINE=InnoDB;

-- ============================================================
-- 7) TABELA: auditoria_acesso
-- ============================================================
CREATE TABLE auditoria_acesso (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    -- Dados do evento
    username VARCHAR(100),
    acao VARCHAR(50) NOT NULL,
    detalhes VARCHAR(255),
    data_evento DATETIME NOT NULL,
    ip VARCHAR(50),

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    INDEX idx_auditoria_empresa (empresa_id),
    INDEX idx_auditoria_data (data_evento),
    INDEX idx_auditoria_username (username)
) ENGINE=InnoDB;

-- ============================================================
-- 8) TABELA: role_permissao
-- ============================================================
CREATE TABLE role_permissao (
    id BIGINT NOT NULL AUTO_INCREMENT,

    empresa_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_role_permissao
        UNIQUE (empresa_id, role_id, permissao_id),

    CONSTRAINT fk_erp_role
        FOREIGN KEY (role_id)
        REFERENCES role(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_erp_permissao
        FOREIGN KEY (permissao_id)
        REFERENCES permissao(id)
        ON DELETE CASCADE,

    INDEX idx_erp_empresa (empresa_id),
    INDEX idx_erp_role (role_id),
    INDEX idx_erp_permissao (permissao_id)
) ENGINE=InnoDB;
