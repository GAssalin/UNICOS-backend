-- ================================
--  SCHEMA AUTH - UniCoS
--  Flyway Migration V1
-- ================================

-- ============================================================
-- 1) TABELA: permissao
-- ============================================================
CREATE TABLE permissao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ============================================================
-- 2) TABELA: role
-- ============================================================
CREATE TABLE role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ============================================================
-- 3) TABELA ASSOCIATIVA: role_permissao (N:N)
-- ============================================================
CREATE TABLE role_permissao (
    role_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permissao_id),

    CONSTRAINT fk_role_permissao_role
        FOREIGN KEY (role_id) REFERENCES role(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_role_permissao_permissao
        FOREIGN KEY (permissao_id) REFERENCES permissao(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 4) TABELA: role_hierarchy_relation
-- ============================================================
CREATE TABLE role_hierarchy_relation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    parent_role VARCHAR(100) NOT NULL,
    child_role VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),

    CONSTRAINT uk_parent_child_role UNIQUE (parent_role, child_role)
) ENGINE=InnoDB;

CREATE INDEX idx_parent_role ON role_hierarchy_relation(parent_role);
CREATE INDEX idx_child_role ON role_hierarchy_relation(child_role);

-- ============================================================
-- 5) TABELA: usuario
-- ============================================================
CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(100) NOT NULL UNIQUE,
    pessoa_id BIGINT,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    refresh_token VARCHAR(200),
    expiracao_refresh_token DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ============================================================
-- 6) TABELA ASSOCIATIVA: usuario_role (N:N)
-- ============================================================
CREATE TABLE usuario_role (
    usuario_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, role_id),

    CONSTRAINT fk_usuario_role_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_role_role
        FOREIGN KEY (role_id) REFERENCES role(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 7) TABELA: usuario_email_verificacao
-- ============================================================
CREATE TABLE usuario_email_verificacao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expiracao DATETIME NOT NULL,
    utilizado BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_usuario_email_verificacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 8) TABELA: auditoria_acesso
-- ============================================================
CREATE TABLE auditoria_acesso (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100),
    acao VARCHAR(50) NOT NULL,
    detalhes VARCHAR(255),
    data_evento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip VARCHAR(50),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
