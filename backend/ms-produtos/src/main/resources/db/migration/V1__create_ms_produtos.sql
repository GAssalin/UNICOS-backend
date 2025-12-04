-- ============================================================
--  MIGRATION: Criação completa do schema do ms-produtos
--  Arquivo: V1__create_ms_produtos.sql
-- ============================================================

-- ===========================================
-- 1) TABELA: categoria
-- ===========================================
CREATE TABLE categoria (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    categoria_pai_id BIGINT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_categoria_nome_pai UNIQUE (nome, categoria_pai_id),
    CONSTRAINT fk_categoria_pai FOREIGN KEY (categoria_pai_id)
        REFERENCES categoria(id)
);

-- ===========================================
-- 2) TABELA: marca
-- ===========================================
CREATE TABLE marca (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    pais_origem VARCHAR(100)
);

CREATE INDEX idx_marca_nome ON marca (nome);
CREATE INDEX idx_marca_pais ON marca (pais_origem);

-- ===========================================
-- 3) TABELA: unidade_medida
-- ===========================================
CREATE TABLE unidade_medida (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    sigla VARCHAR(10) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ===========================================
-- 4) TABELA: produto
--    Inclui todos os campos embutidos (ProdutoBase, TributacaoBase etc.)
-- ===========================================
CREATE TABLE produto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- ProdutoBase
    dados_basicos_nome VARCHAR(150),
    dados_basicos_descricao VARCHAR(500),
    dados_basicos_sku VARCHAR(50),
    dados_basicos_tipo VARCHAR(50),
    dados_basicos_classificacao VARCHAR(50),

    -- ProdutoTributacaoBase
    tributacao_ncm VARCHAR(10),
    tributacao_cest VARCHAR(20),
    tributacao_origem VARCHAR(20),
    tributacao_csosn VARCHAR(10),
    tributacao_cst VARCHAR(10),
    tributacao_cfop VARCHAR(10),
    tributacao_aliquota_icms DECIMAL(15,2),
    tributacao_aliquota_pis DECIMAL(15,2),
    tributacao_aliquota_cofins DECIMAL(15,2),

    -- ProdutoEstoqueBase
    estoqueconfig_controla_estoque BOOLEAN,
    estoqueconfig_estoque_min DECIMAL(15,3),
    estoqueconfig_estoque_max DECIMAL(15,3),
    estoqueconfig_estoque_atual DECIMAL(15,3),

    -- PrecoBase
    precoatual_preco DECIMAL(15,2),
    precoatual_preco_promocional DECIMAL(15,2),
    precoatual_data_inicio_promocao TIMESTAMP,
    precoatual_data_fim_promocao TIMESTAMP,

    -- Operacionais
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    categoria_id BIGINT,
    marca_id BIGINT,

    CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria(id),

    CONSTRAINT fk_produto_marca FOREIGN KEY (marca_id)
        REFERENCES marca(id)
);

-- Índice para SKU
CREATE INDEX idx_produto_sku ON produto (dados_basicos_sku);

-- ===========================================
-- 5) TABELA: atributo_personalizado
-- ===========================================
CREATE TABLE atributo_personalizado (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria_id BIGINT NOT NULL,

    CONSTRAINT uk_atributo_categoria UNIQUE (nome, categoria_id),
    CONSTRAINT fk_atributo_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
);

-- ===========================================
-- 6) TABELA: produto_atributo_valor
-- ===========================================
CREATE TABLE produto_atributo_valor (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    atributo_personalizado_id BIGINT NOT NULL,
    valor VARCHAR(100) NOT NULL,

    CONSTRAINT fk_prod_atrib_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id),

    CONSTRAINT fk_prod_atrib_attr FOREIGN KEY (atributo_personalizado_id)
        REFERENCES atributo_personalizado(id),

    CONSTRAINT uk_produto_atributo UNIQUE (produto_id, atributo_personalizado_id)
);

-- ===========================================
-- 7) TABELA: produto_unidade
-- ===========================================
CREATE TABLE produto_unidade (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    unidade_medida_id BIGINT NOT NULL,
    quantidade_padrao DOUBLE NOT NULL,
    fator_conversao DOUBLE NOT NULL DEFAULT 1.0,

    CONSTRAINT fk_prod_unid_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id),

    CONSTRAINT fk_prod_unid_unid FOREIGN KEY (unidade_medida_id)
        REFERENCES unidade_medida(id),

    CONSTRAINT uk_produto_unidade UNIQUE (produto_id, unidade_medida_id)
);

-- ===========================================
-- 8) TABELA: produto_variacao
-- ===========================================
CREATE TABLE produto_variacao (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE,
    preco DECIMAL(15,2),
    codigo_barras VARCHAR(13) UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    cor VARCHAR(50),
    tamanho VARCHAR(50),
    material VARCHAR(100),

    CONSTRAINT fk_var_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id)
);

-- ===========================================
-- 9) TABELA: fornecedor_produto
-- ===========================================
CREATE TABLE fornecedor_produto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
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

-- ===========================================
-- 10) TABELA: historico_preco
-- ===========================================
CREATE TABLE historico_preco (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    preco_anterior DECIMAL(15,2) NOT NULL,
    novo_preco DECIMAL(15,2) NOT NULL,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo VARCHAR(500),

    CONSTRAINT fk_hist_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id)
);

-- ===========================================
-- 11) TABELA: imagem_produto
-- ===========================================
CREATE TABLE imagem_produto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    descricao_alt VARCHAR(255),
    principal BOOLEAN NOT NULL DEFAULT FALSE,
    ordem_exibicao INT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_img_prod FOREIGN KEY (produto_id)
        REFERENCES produto(id)
);

CREATE INDEX idx_imagem_produto_ordem
    ON imagem_produto (produto_id, ordem_exibicao);
