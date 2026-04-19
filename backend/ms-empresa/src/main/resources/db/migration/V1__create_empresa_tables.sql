-- =========================================================
-- MS-EMPRESA | DDL UNIFICADO (MySQL 8+)
-- =========================================================

CREATE TABLE empresa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matriz_id BIGINT NULL,

    razao_social VARCHAR(200) NOT NULL,
    nome_fantasia VARCHAR(200) NULL,
    cnpj VARCHAR(14) NOT NULL,
    tipo_empresa VARCHAR(20) NOT NULL,
    status_empresa VARCHAR(20) NOT NULL,
    regime_tributario VARCHAR(30) NOT NULL,
    data_abertura DATE NULL,
    pessoa_juridica_id BIGINT NULL,

    criado_por BIGINT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_empresa_cnpj UNIQUE (cnpj),
    CONSTRAINT fk_empresa_matriz
        FOREIGN KEY (matriz_id) REFERENCES empresa(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

    INDEX idx_empresa_razao_social (razao_social),
    INDEX idx_empresa_nome_fantasia (nome_fantasia),
    INDEX idx_empresa_tipo_empresa (tipo_empresa),
    INDEX idx_empresa_status_empresa (status_empresa),
    INDEX idx_empresa_regime_tributario (regime_tributario),
    INDEX idx_empresa_pessoa_juridica_id (pessoa_juridica_id),
    INDEX idx_empresa_matriz_id (matriz_id)
);

CREATE TABLE empresa_configuracao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    chave VARCHAR(100) NOT NULL,
    valor VARCHAR(255) NOT NULL,

    criado_por BIGINT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_empresa_configuracao_empresa_chave UNIQUE (empresa_id, chave),
    CONSTRAINT fk_empresa_configuracao_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    INDEX idx_empresa_configuracao_empresa_id (empresa_id)
);

CREATE TABLE empresa_contato (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    tipo_contato VARCHAR(20) NOT NULL,
    valor VARCHAR(150) NOT NULL,
    principal BOOLEAN NOT NULL DEFAULT FALSE,

    criado_por BIGINT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_empresa_contato_empresa_valor UNIQUE (empresa_id, valor),
    CONSTRAINT fk_empresa_contato_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    INDEX idx_empresa_contato_empresa_id (empresa_id),
    INDEX idx_empresa_contato_tipo_contato (tipo_contato),
    INDEX idx_empresa_contato_principal (principal)
);

CREATE TABLE empresa_endereco (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    tipo_endereco VARCHAR(20) NOT NULL,
    logradouro VARCHAR(150) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100) NULL,
    bairro VARCHAR(100) NOT NULL,
    municipio VARCHAR(120) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    principal BOOLEAN NOT NULL DEFAULT FALSE,

    criado_por BIGINT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_empresa_endereco_empresa_logradouro_numero_cep
        UNIQUE (empresa_id, logradouro, numero, cep),
    CONSTRAINT fk_empresa_endereco_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    INDEX idx_empresa_endereco_empresa_id (empresa_id),
    INDEX idx_empresa_endereco_tipo_endereco (tipo_endereco),
    INDEX idx_empresa_endereco_principal (principal),
    INDEX idx_empresa_endereco_cep (cep),
    INDEX idx_empresa_endereco_municipio_uf (municipio, uf)
);

CREATE TABLE empresa_parametro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    chave VARCHAR(100) NOT NULL,
    valor VARCHAR(255) NOT NULL,

    criado_por BIGINT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_empresa_parametro_empresa_chave UNIQUE (empresa_id, chave),
    CONSTRAINT fk_empresa_parametro_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    INDEX idx_empresa_parametro_empresa_id (empresa_id)
);

CREATE TABLE empresa_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    usuario_id BIGINT NOT NULL,
    perfil VARCHAR(20) NOT NULL,

    criado_por BIGINT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_empresa_usuario_empresa_usuario UNIQUE (empresa_id, usuario_id),
    CONSTRAINT fk_empresa_usuario_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    INDEX idx_empresa_usuario_empresa_id (empresa_id),
    INDEX idx_empresa_usuario_usuario_id (usuario_id),
    INDEX idx_empresa_usuario_perfil (perfil)
);