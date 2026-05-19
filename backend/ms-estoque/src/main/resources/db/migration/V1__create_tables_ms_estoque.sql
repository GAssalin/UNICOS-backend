-- V1__create_tables_ms_estoque.sql
-- Criação das tabelas base do microserviço ms-estoque
-- Banco alvo: MySQL 8+ (InnoDB)

-- =========================================================
-- TABELA: estoque
-- Entidade: Estoque extends BaseTenantEntity
-- =========================================================
CREATE TABLE estoque (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- BaseTenantEntity / EntidadeAuditavel
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL DEFAULT b'1',

    -- Domínio
    codigo VARCHAR(30) NOT NULL,
    nome VARCHAR(200) NOT NULL,
    descricao VARCHAR(500) NULL,
    status_estoque VARCHAR(20) NOT NULL,
    estoque_pai_id BIGINT NULL,

    CONSTRAINT pk_estoque PRIMARY KEY (id),
    CONSTRAINT uk_estoque_empresa_codigo UNIQUE (empresa_id, codigo)
) ENGINE=InnoDB;

CREATE INDEX ix_estoque_empresa_id ON estoque (empresa_id);
CREATE INDEX ix_estoque_status ON estoque (status_estoque);
CREATE INDEX ix_estoque_pai ON estoque (estoque_pai_id);


-- =========================================================
-- TABELA: estoque_produto
-- Entidade: EstoqueProduto extends BaseTenantEntity
-- =========================================================
CREATE TABLE estoque_produto (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- BaseTenantEntity / EntidadeAuditavel
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL DEFAULT b'1',

    -- Domínio
    estoque_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade_atual DECIMAL(19,4) NOT NULL,
    quantidade_reservada DECIMAL(19,4) NOT NULL,
    quantidade_disponivel DECIMAL(19,4) NOT NULL,

    CONSTRAINT pk_estoque_produto PRIMARY KEY (id),
    CONSTRAINT uk_estoque_produto_empresa_estoque_produto UNIQUE (empresa_id, estoque_id, produto_id)
) ENGINE=InnoDB;

CREATE INDEX ix_estoque_produto_empresa_id ON estoque_produto (empresa_id);
CREATE INDEX ix_estoque_produto_estoque_id ON estoque_produto (estoque_id);
CREATE INDEX ix_estoque_produto_produto_id ON estoque_produto (produto_id);


-- =========================================================
-- TABELA: responsavel_estoque
-- Entidade: ResponsavelEstoque extends BaseTenantEntity
-- =========================================================
CREATE TABLE responsavel_estoque (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- BaseTenantEntity / EntidadeAuditavel
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL DEFAULT b'1',

    -- Domínio
    estoque_id BIGINT NOT NULL,
    responsavel_id BIGINT NOT NULL,
    papel VARCHAR(30) NOT NULL,
    principal BIT(1) NOT NULL,
    vigencia_inicio DATE NOT NULL,
    vigencia_fim DATE NULL,
    status_responsavel_estoque VARCHAR(20) NOT NULL,

    CONSTRAINT pk_responsavel_estoque PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX ix_resp_est_empresa_id ON responsavel_estoque (empresa_id);
CREATE INDEX ix_resp_est_estoque_id ON responsavel_estoque (estoque_id);
CREATE INDEX ix_resp_est_responsavel_id ON responsavel_estoque (responsavel_id);
CREATE INDEX ix_resp_est_empresa_estoque ON responsavel_estoque (empresa_id, estoque_id);
CREATE INDEX ix_resp_est_status ON responsavel_estoque (status_responsavel_estoque);
CREATE INDEX ix_resp_est_principal ON responsavel_estoque (estoque_id, principal);


-- =========================================================
-- TABELA: vinculo_estoque_filial
-- Entidade: VinculoEstoqueFilial extends BaseTenantEntity
-- =========================================================
CREATE TABLE vinculo_estoque_filial (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- BaseTenantEntity / EntidadeAuditavel
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL DEFAULT b'1',

    -- Domínio
    estoque_id BIGINT NOT NULL,
    filial_id BIGINT NOT NULL,
    tipo_atuacao VARCHAR(30) NOT NULL,
    vigencia_inicio DATE NOT NULL,
    vigencia_fim DATE NULL,
    status_vinculo_estoque_filial VARCHAR(30) NOT NULL,

    CONSTRAINT pk_vinculo_estoque_filial PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX ix_vinc_est_fil_empresa_id ON vinculo_estoque_filial (empresa_id);
CREATE INDEX ix_vinc_est_fil_estoque_id ON vinculo_estoque_filial (estoque_id);
CREATE INDEX ix_vinc_est_fil_filial_id ON vinculo_estoque_filial (filial_id);
CREATE INDEX ix_vinc_est_fil_empresa_estoque ON vinculo_estoque_filial (empresa_id, estoque_id);
CREATE INDEX ix_vinc_est_fil_empresa_filial ON vinculo_estoque_filial (empresa_id, filial_id);
CREATE INDEX ix_vinc_est_fil_status ON vinculo_estoque_filial (status_vinculo_estoque_filial);
CREATE INDEX ix_vinc_est_fil_tipo_atuacao ON vinculo_estoque_filial (tipo_atuacao);
