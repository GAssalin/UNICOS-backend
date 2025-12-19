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
-- 4) TABELA: auditoria_acesso
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
-- 5) TABELA: role_permissao
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

-- ============================================================
-- 6) TABELA ASSOCIATIVA: role_usuario
-- (ms-auth NÃO referencia fisicamente ms-usuario)
-- ============================================================
CREATE TABLE role_usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    -- Referência lógica ao ms-usuario
    usuario_id BIGINT NOT NULL,

    -- Nome da role (ex: UNICOS_ADMIN)
    role_nome VARCHAR(64) NOT NULL,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_role_usuario
        UNIQUE (empresa_id, usuario_id, role_nome),

    INDEX idx_ur_empresa (empresa_id),
    INDEX idx_ur_usuario (usuario_id),
    INDEX idx_ur_role (role_nome)
) ENGINE=InnoDB;
