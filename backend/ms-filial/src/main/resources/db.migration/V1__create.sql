-- ================================
--  SCHEMA MS-FILIAL - UniCoS
--  Flyway Migration V1
-- ================================

-- ============================================================
-- 1) TABELA: filial
-- ============================================================
CREATE TABLE filial (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    -- Identificação
    codigo VARCHAR(30) NOT NULL,
    nome VARCHAR(200) NOT NULL,
    cnpj VARCHAR(14) NOT NULL,

    -- Status
    status_filial VARCHAR(20) NOT NULL,

    -- Integrações lógicas (sem FK física)
    empresa_id_proprietaria BIGINT NOT NULL,

    -- Referências internas (MVP)
    endereco_filial_id BIGINT,
    contato_filial_id BIGINT,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_filial_codigo UNIQUE (codigo),
    CONSTRAINT uk_filial_cnpj UNIQUE (cnpj),

    INDEX idx_filial_empresa (empresa_id),
    INDEX idx_filial_empresa_proprietaria (empresa_id_proprietaria),
    INDEX idx_filial_status (status_filial)
) ENGINE=InnoDB;

-- ============================================================
-- 2) TABELA: endereco_filial
-- ============================================================
CREATE TABLE endereco_filial (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    -- Relacionamento lógico com filial (sem FK física no MVP)
    filial_id BIGINT NOT NULL,

    -- Endereço
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(120) NOT NULL,
    cidade VARCHAR(120) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    cep VARCHAR(8) NOT NULL,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    INDEX idx_endereco_filial_empresa (empresa_id),
    INDEX idx_endereco_filial_filial (filial_id),
    INDEX idx_endereco_filial_cep (cep)
) ENGINE=InnoDB;

-- ============================================================
-- 3) TABELA: contato_filial
-- ============================================================
CREATE TABLE contato_filial (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    -- Relacionamento lógico com filial (sem FK física no MVP)
    filial_id BIGINT NOT NULL,

    -- Contatos
    telefone_principal VARCHAR(20) NOT NULL,
    telefone_secundario VARCHAR(20),
    email_principal VARCHAR(120) NOT NULL,
    email_secundario VARCHAR(120),
    nome_responsavel VARCHAR(120),

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    INDEX idx_contato_filial_empresa (empresa_id),
    INDEX idx_contato_filial_filial (filial_id),
    INDEX idx_contato_filial_email (email_principal)
) ENGINE=InnoDB;

-- ============================================================
-- 4) TABELA: horario_funcionamento_filial
-- ============================================================
CREATE TABLE horario_funcionamento_filial (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    filial_id BIGINT NOT NULL,

    -- Dia/Situação
    dia_semana VARCHAR(10) NOT NULL,
    hora_abertura TIME,
    hora_fechamento TIME,
    aberto BOOLEAN NOT NULL,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_horario_filial_dia UNIQUE (filial_id, dia_semana),

    INDEX idx_horario_filial_empresa (empresa_id),
    INDEX idx_horario_filial_filial (filial_id),
    INDEX idx_horario_filial_dia (dia_semana)
) ENGINE=InnoDB;

-- ============================================================
-- 5) TABELA: filial_parametro
-- ============================================================
CREATE TABLE filial_parametro (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    filial_id BIGINT NOT NULL,

    -- Parâmetro
    chave VARCHAR(80) NOT NULL,
    valor VARCHAR(500) NOT NULL,
    descricao VARCHAR(300),
    ativo_parametro BOOLEAN NOT NULL,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    CONSTRAINT uk_filial_parametro_chave UNIQUE (filial_id, chave),

    INDEX idx_filial_parametro_empresa (empresa_id),
    INDEX idx_filial_parametro_filial (filial_id),
    INDEX idx_filial_parametro_chave (chave)
) ENGINE=InnoDB;

-- ============================================================
-- 6) TABELA: filial_status_historico
-- ============================================================
CREATE TABLE filial_status_historico (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Multi-tenant
    empresa_id BIGINT NOT NULL,

    filial_id BIGINT NOT NULL,

    status_anterior VARCHAR(20),
    status_novo VARCHAR(20) NOT NULL,
    data_alteracao DATETIME NOT NULL,
    motivo VARCHAR(300) NOT NULL,

    -- Integração lógica com usuário (ms-auth/ms-pessoas)
    usuario_id BIGINT,

    -- Auditoria
    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),

    INDEX idx_filial_status_hist_empresa (empresa_id),
    INDEX idx_filial_status_hist_filial (filial_id),
    INDEX idx_filial_status_hist_data (data_alteracao),
    INDEX idx_filial_status_hist_usuario (usuario_id)
) ENGINE=InnoDB;
