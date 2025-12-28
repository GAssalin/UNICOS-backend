-- ============================================================
-- TABELA: pessoa (classe base da herança JOINED)
-- ============================================================
CREATE TABLE pessoa (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(150) NOT NULL,
    tipo_pessoa VARCHAR(20) NOT NULL,

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- TABELA: pessoa_fisica
-- ============================================================
CREATE TABLE pessoa_fisica (
    id BIGINT PRIMARY KEY,

    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    nome_social VARCHAR(150),

    CONSTRAINT fk_pf_pessoa
        FOREIGN KEY (id) REFERENCES pessoa(id)
        ON DELETE CASCADE
);

-- ============================================================
-- TABELA: pessoa_juridica
-- ============================================================
CREATE TABLE pessoa_juridica (
    id BIGINT PRIMARY KEY,

    cnpj VARCHAR(14) NOT NULL UNIQUE,
    razao_social VARCHAR(200) NOT NULL,
    nome_fantasia VARCHAR(200),

    CONSTRAINT fk_pj_pessoa
        FOREIGN KEY (id) REFERENCES pessoa(id)
        ON DELETE CASCADE
);

-- ============================================================
-- TABELA: municipio
-- ============================================================
CREATE TABLE municipio (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(120) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    codigo_ibge VARCHAR(10),

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- TABELA: endereco
-- ============================================================
CREATE TABLE endereco (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    pessoa_id BIGINT NOT NULL,
    tipo_endereco VARCHAR(20) NOT NULL,
    logradouro VARCHAR(150) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    municipio_id BIGINT NOT NULL,
    cep VARCHAR(8) NOT NULL,
    principal BOOLEAN NOT NULL DEFAULT FALSE,

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_endereco_pessoa
        FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_endereco_municipio
        FOREIGN KEY (municipio_id) REFERENCES municipio(id)
);

CREATE INDEX idx_endereco_pessoa ON endereco(pessoa_id);
CREATE INDEX idx_endereco_municipio ON endereco(municipio_id);
CREATE INDEX idx_endereco_cep ON endereco(cep);

-- ============================================================
-- TABELA: documento
-- ============================================================
CREATE TABLE documento (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    pessoa_id BIGINT NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    numero VARCHAR(50) NOT NULL,
    orgao_emissor VARCHAR(50),
    data_emissao DATE,

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_documento_pessoa
        FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX uq_documento_numero ON documento(numero);
CREATE INDEX idx_documento_pessoa ON documento(pessoa_id);
CREATE INDEX idx_documento_tipo ON documento(tipo_documento);

-- ============================================================
-- TABELA: contato
-- ============================================================
CREATE TABLE contato (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    pessoa_id BIGINT NOT NULL,
    tipo_contato VARCHAR(20) NOT NULL,
    valor VARCHAR(150) NOT NULL,
    principal BOOLEAN NOT NULL DEFAULT FALSE,

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_contato_pessoa
        FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_contato_pessoa ON contato(pessoa_id);

-- ============================================================
-- TABELA: tipo_relacao_pessoa
-- ============================================================
CREATE TABLE tipo_relacao_pessoa (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- TABELA: pessoa_relacao
-- ============================================================
CREATE TABLE pessoa_relacao (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    pessoa_id BIGINT NOT NULL,
    relacionado_id BIGINT NOT NULL,
    tipo_relacao_pessoa_id BIGINT NOT NULL,

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_relacao_pessoa
        FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_relacao_relacionado
        FOREIGN KEY (relacionado_id) REFERENCES pessoa(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_relacao_tipo
        FOREIGN KEY (tipo_relacao_pessoa_id)
        REFERENCES tipo_relacao_pessoa(id)
);

CREATE INDEX idx_relacao_pessoa ON pessoa_relacao(pessoa_id);
CREATE INDEX idx_relacao_relacionado ON pessoa_relacao(relacionado_id);
CREATE INDEX idx_relacao_tipo ON pessoa_relacao(tipo_relacao_pessoa_id);
