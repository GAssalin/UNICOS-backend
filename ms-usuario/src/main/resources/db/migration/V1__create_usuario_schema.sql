-- ================================
--  SCHEMA USUARIO - UniCoS
--  Flyway Migration V1
-- ================================

-- ============================================================
-- 1) TABELA: usuario
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
    UNIQUE KEY uk_usuario_empresa_login (empresa_id, login),
    UNIQUE KEY uk_usuario_empresa_email (empresa_id, email),
    INDEX idx_usuario_empresa (empresa_id)
) ENGINE=InnoDB;

-- ============================================================
-- 2) TABELA: usuario_email_verificacao
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
