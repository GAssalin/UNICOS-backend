-- =========================================================
-- MS-PRODUTO | DDL UNIFICADO (MySQL/MariaDB)
-- =========================================================
-- Padrão de auditoria + tenant (copiado em todas tabelas)
-- criado_por BIGINT
-- criado_em DATETIME NOT NULL
-- atualizado_por BIGINT
-- atualizado_em DATETIME
-- ativo BOOLEAN NOT NULL
-- empresa_id BIGINT NOT NULL
-- =========================================================


-- =========================================================
-- CATEGORIA PRODUTO
-- =========================================================
CREATE TABLE categoria_produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(400),
    categoria_pai_id BIGINT,

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_categoria_produto_empresa_nome UNIQUE (empresa_id, nome)
);

-- muito útil para montar hierarquia por tenant
CREATE INDEX idx_categoria_produto_empresa_pai
    ON categoria_produto (empresa_id, categoria_pai_id);



-- =========================================================
-- MARCA PRODUTO
-- =========================================================
CREATE TABLE marca_produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(400),

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_marca_produto_empresa_nome UNIQUE (empresa_id, nome)
);



-- =========================================================
-- UNIDADE MEDIDA
-- =========================================================
CREATE TABLE unidade_medida (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    codigo VARCHAR(10) NOT NULL,
    descricao VARCHAR(80) NOT NULL,
    fracionavel BOOLEAN NOT NULL,

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_unidade_medida_empresa_codigo UNIQUE (empresa_id, codigo)
);

-- útil se existir filtro por fracionável
CREATE INDEX idx_unidade_medida_empresa_fracionavel
    ON unidade_medida (empresa_id, fracionavel);



-- =========================================================
-- TIPO PRODUTO (dinâmico)
-- =========================================================
CREATE TABLE produto_tipo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(400),

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_produto_tipo_empresa_nome UNIQUE (empresa_id, nome)
);



-- =========================================================
-- PRODUTO
-- =========================================================
CREATE TABLE produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    codigo VARCHAR(50) NOT NULL,
    nome VARCHAR(200) NOT NULL,
    descricao VARCHAR(1000),

    tipo_produto VARCHAR(20) NOT NULL,
    unidade_medida VARCHAR(10) NOT NULL,

    categoria_id BIGINT,
    marca_id BIGINT,

    codigo_barras VARCHAR(20),
    preco_base DECIMAL(15,4),
    peso DECIMAL(15,4),
    volume DECIMAL(15,4),

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_produto_empresa_codigo UNIQUE (empresa_id, codigo),

    -- listagem / busca
    INDEX idx_produto_empresa_nome (empresa_id, nome),

    -- filtros comuns
    INDEX idx_produto_categoria (empresa_id, categoria_id),
    INDEX idx_produto_marca (empresa_id, marca_id),

    -- filtro por tipo (PRODUTO/SERVICO/DIGITAL/ASSINATURA)
    INDEX idx_produto_empresa_tipo (empresa_id, tipo_produto),

    -- leitura por código de barras (PDV/importação)
    INDEX idx_produto_empresa_codigo_barras (empresa_id, codigo_barras)
);



-- =========================================================
-- ATRIBUTOS
-- =========================================================
CREATE TABLE produto_atributo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(400),

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_produto_atributo_empresa_nome UNIQUE (empresa_id, nome)
);



-- =========================================================
-- ATRIBUTO VALOR
-- =========================================================
CREATE TABLE produto_atributo_valor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    atributo_id BIGINT NOT NULL,
    valor VARCHAR(250) NOT NULL,

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_produto_atributo_valor_empresa_produto_atributo
        UNIQUE (empresa_id, produto_id, atributo_id),

    INDEX idx_produto_atributo_valor_empresa_produto (empresa_id, produto_id),
    INDEX idx_produto_atributo_valor_empresa_atributo (empresa_id, atributo_id)
);



-- =========================================================
-- CODIGOS DE BARRAS
-- =========================================================
CREATE TABLE produto_codigo_barras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    codigo_barras VARCHAR(20) NOT NULL,
    principal BOOLEAN NOT NULL,

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_produto_cod_barras_empresa_codigo
        UNIQUE (empresa_id, codigo_barras),

    INDEX idx_produto_cod_barras_empresa_produto (empresa_id, produto_id),
    INDEX idx_produto_cod_barras_empresa_produto_principal (empresa_id, produto_id, principal)
);



-- =========================================================
-- IMAGENS
-- =========================================================
CREATE TABLE produto_imagem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    alt_texto VARCHAR(200),
    principal BOOLEAN NOT NULL,
    ordem INT NOT NULL,

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    INDEX idx_produto_imagem_empresa_produto (empresa_id, produto_id),
    INDEX idx_produto_imagem_empresa_produto_ordem (empresa_id, produto_id, ordem),
    INDEX idx_produto_imagem_empresa_produto_principal (empresa_id, produto_id, principal)
);



-- =========================================================
-- PREÇO BASE
-- =========================================================
CREATE TABLE produto_preco_base (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    custo_base DECIMAL(15,4),
    preco_venda_base DECIMAL(15,4),
    margem_base DECIMAL(7,2),

    criado_por BIGINT,
    criado_em DATETIME NOT NULL,
    atualizado_por BIGINT,
    atualizado_em DATETIME,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT uk_produto_preco_base_empresa_produto
        UNIQUE (empresa_id, produto_id),

    INDEX idx_produto_preco_base_empresa_produto (empresa_id, produto_id)
);
