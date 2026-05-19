-- V3__create_tables_estoque_transacao.sql
-- Criação das tabelas transacionais do microserviço ms-estoque
-- Banco alvo: MySQL 8+ (InnoDB)

-- =========================================================
-- TABELA: movimentacao_estoque
-- Entidade: MovimentacaoEstoque extends BaseTenantEntity
-- =========================================================
CREATE TABLE movimentacao_estoque (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- BaseTenantEntity / EntidadeAuditavel
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL DEFAULT b'1',

    -- Domínio
    tipo_movimentacao VARCHAR(30) NOT NULL,
    estoque_origem_id BIGINT NULL,
    estoque_destino_id BIGINT NULL,
    data_movimentacao DATETIME(6) NOT NULL,
    observacao VARCHAR(500) NULL,
    documento_referencia VARCHAR(100) NULL,
    usuario_responsavel_id BIGINT NULL,
    status_movimentacao VARCHAR(20) NOT NULL,

    CONSTRAINT pk_movimentacao_estoque PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX ix_mov_est_empresa_id ON movimentacao_estoque (empresa_id);
CREATE INDEX ix_mov_est_origem_id ON movimentacao_estoque (estoque_origem_id);
CREATE INDEX ix_mov_est_destino_id ON movimentacao_estoque (estoque_destino_id);
CREATE INDEX ix_mov_est_tipo ON movimentacao_estoque (tipo_movimentacao);
CREATE INDEX ix_mov_est_status ON movimentacao_estoque (status_movimentacao);
CREATE INDEX ix_mov_est_data ON movimentacao_estoque (data_movimentacao);


-- =========================================================
-- TABELA: movimentacao_estoque_item
-- Entidade: MovimentacaoEstoqueItem extends BaseTenantEntity
-- =========================================================
CREATE TABLE movimentacao_estoque_item (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- BaseTenantEntity / EntidadeAuditavel
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL DEFAULT b'1',

    -- Domínio
    movimentacao_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(19,4) NOT NULL,
    valor_unitario DECIMAL(19,4) NULL,

    CONSTRAINT pk_movimentacao_estoque_item PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX ix_mov_item_empresa_id ON movimentacao_estoque_item (empresa_id);
CREATE INDEX ix_mov_item_movimentacao_id ON movimentacao_estoque_item (movimentacao_id);
CREATE INDEX ix_mov_item_produto_id ON movimentacao_estoque_item (produto_id);
