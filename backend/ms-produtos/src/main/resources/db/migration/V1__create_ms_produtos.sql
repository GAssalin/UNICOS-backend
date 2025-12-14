-- ============================================================
--  MIGRATION: Criação completa do schema do ms-produtos
--  Arquitetura: MULTI-TENANT (empresa_id)
-- ============================================================

-- ===========================================
-- 1) TABELA: categoria
-- ===========================================
CREATE TABLE categoria (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    categoria_pai_id BIGINT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_categoria_empresa UNIQUE (empresa_id, nome, categoria_pai_id),
    CONSTRAINT fk_categoria_pai FOREIGN KEY (categoria_pai_id)
        REFERENCES categoria(id)
);

CREATE INDEX idx_categoria_empresa
    ON categoria (empresa_id);

-- ===========================================
-- 2) TABELA: marca
-- ===========================================
CREATE TABLE marca (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    pais_origem VARCHAR(100),

    CONSTRAINT uk_marca_empresa UNIQUE (empresa_id, nome)
);

CREATE INDEX idx_marca_empresa
    ON marca (empresa_id);

CREATE INDEX idx_marca_nome
    ON marca (empresa_id, nome);

-- ===========================================
-- 3) TABELA: unidade_medida
-- ===========================================
CREATE TABLE unidade_medida (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(50) NOT NULL,
    sigla VARCHAR(10) NOT NULL,
    descricao VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_unidade_sigla_empresa UNIQUE (empresa_id, sigla)
);

CREATE INDEX idx_unidade_empresa
    ON unidade_medida (empresa_id);

-- ===========================================
-- 4) TABELA: produto
-- ===========================================
CREATE TABLE produto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    -- ProdutoBase
    dados_basicos_nome VARCHAR(150),
    dados_basicos_descricao VARCHAR(500),
    dados_basicos_sku VARCHAR(50),
    dados_basicos_tipo VARCHAR(50),
    dados_basicos_variacao VARCHAR(50),
    dados_basicos_origem VARCHAR(50),
    dados_basicos_controle_estoque VARCHAR(50),
    dados_basicos_armazenamento VARCHAR(50),
    dados_basicos_classificacao VARCHAR(50),
    dados_basicos_status VARCHAR(50),

    -- ProdutoTributacaoBase
    tributacao_ncm VARCHAR(10),
    tributacao_cest VARCHAR(20),
    tributacao_situacao VARCHAR(30),

    -- PrecoBase
    preco_custo DECIMAL(15,2),
    preco_venda DECIMAL(15,2),
    preco_minimo DECIMAL(15,2),
    margem_padrao DECIMAL(15,2),

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    categoria_id BIGINT,
    marca_id BIGINT,

    CONSTRAINT uk_produto_sku_empresa UNIQUE (empresa_id, dados_basicos_sku),

    CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria(id),

    CONSTRAINT fk_produto_marca FOREIGN KEY (marca_id)
        REFERENCES marca(id)
);

CREATE INDEX idx_produto_empresa
    ON produto (empresa_id);

-- ===========================================
-- 5) TABELA: atributo_personalizado
-- ===========================================
CREATE TABLE atributo_personalizado (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(100) NOT NULL,
    categoria_id BIGINT NOT NULL,

    CONSTRAINT uk_atributo_empresa_categoria UNIQUE (empresa_id, nome, categoria_id),
    CONSTRAINT fk_atributo_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
);

CREATE INDEX idx_atributo_empresa
    ON atributo_personalizado (empresa_id);

-- ===========================================
-- 6) TABELA: produto_atributo_valor
-- ===========================================
CREATE TABLE produto_atributo_valor (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    atributo_personalizado_id BIGINT NOT NULL,
    valor VARCHAR(100) NOT NULL,

    CONSTRAINT uk_produto_atributo_empresa UNIQUE (
        empresa_id, produto_id, atributo_personalizado_id
    ),

    CONSTRAINT fk_prod_atrib_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id),

    CONSTRAINT fk_prod_atrib_attr FOREIGN KEY (atributo_personalizado_id)
        REFERENCES atributo_personalizado(id)
);

CREATE INDEX idx_prod_attr_empresa
    ON produto_atributo_valor (empresa_id);

-- ===========================================
-- 7) TABELA: produto_unidade
-- ===========================================
CREATE TABLE produto_unidade (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    unidade_medida_id BIGINT NOT NULL,
    quantidade_padrao DOUBLE NOT NULL,
    fator_conversao DOUBLE NOT NULL DEFAULT 1.0,

    CONSTRAINT uk_produto_unidade_empresa UNIQUE (
        empresa_id, produto_id, unidade_medida_id
    ),

    CONSTRAINT fk_prod_unid_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id),

    CONSTRAINT fk_prod_unid_unid FOREIGN KEY (unidade_medida_id)
        REFERENCES unidade_medida(id)
);

CREATE INDEX idx_prod_unidade_empresa
    ON produto_unidade (empresa_id);

-- ===========================================
-- 8) TABELA: produto_variacao
-- ===========================================
CREATE TABLE produto_variacao (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    sku VARCHAR(50) NOT NULL,
    preco DECIMAL(15,2),
    codigo_barras VARCHAR(13),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    cor VARCHAR(50),
    tamanho VARCHAR(50),
    material VARCHAR(100),

    CONSTRAINT uk_variacao_sku_empresa UNIQUE (empresa_id, sku),

    CONSTRAINT fk_var_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id)
);

CREATE INDEX idx_variacao_empresa
    ON produto_variacao (empresa_id);

-- ===========================================
-- 9) TABELA: fornecedor_produto
-- ===========================================
CREATE TABLE fornecedor_produto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    fornecedor_id BIGINT NOT NULL,
    codigo_fornecedor VARCHAR(50),

    produto_id BIGINT NOT NULL,
    preco_custo DECIMAL(15,2) NOT NULL,
    prazo_entrega_dias INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,

    CONSTRAINT fk_forn_prod_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id)
);

CREATE INDEX idx_forn_prod_empresa
    ON fornecedor_produto (empresa_id);

-- ===========================================
-- 10) TABELA: historico_preco
-- ===========================================
CREATE TABLE historico_preco (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    preco_anterior DECIMAL(15,2) NOT NULL,
    novo_preco DECIMAL(15,2) NOT NULL,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo VARCHAR(500),

    CONSTRAINT fk_hist_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id)
);

CREATE INDEX idx_hist_preco_empresa
    ON historico_preco (empresa_id);

-- ===========================================
-- 11) TABELA: imagem_produto
-- ===========================================
CREATE TABLE imagem_produto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    descricao_alt VARCHAR(255),
    principal BOOLEAN NOT NULL DEFAULT FALSE,
    ordem_exibicao INT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_img_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id)
);

CREATE INDEX idx_imagem_produto_empresa_ordem
    ON imagem_produto (empresa_id, produto_id, ordem_exibicao);
