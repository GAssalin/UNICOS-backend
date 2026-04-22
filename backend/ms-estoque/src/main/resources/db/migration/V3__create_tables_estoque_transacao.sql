-- Flyway migration gerada a partir dos models:
--   - EstoqueProduto
--   - MovimentacaoEstoque
--   - MovimentacaoEstoqueItem
--
-- Observação importante:
-- O model herda de BaseTenantEntity, mas essa classe não foi enviada.
-- Nesta migration foi adicionada apenas a coluna `empresa_id`, porque ela aparece
-- explicitamente no unique constraint de EstoqueProduto.
-- Se o seu BaseTenantEntity possuir outros campos obrigatórios (ex.: created_at,
-- updated_at, deleted, version, tenant_id etc.), eles devem ser acrescentados.

CREATE TABLE estoque_produto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,
    estoque_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade_atual DECIMAL(19,4) NOT NULL,
    quantidade_reservada DECIMAL(19,4) NOT NULL,
    quantidade_disponivel DECIMAL(19,4) NOT NULL,
    CONSTRAINT pk_estoque_produto PRIMARY KEY (id),
    CONSTRAINT uk_estoque_produto_empresa_estoque_produto
        UNIQUE (empresa_id, estoque_id, produto_id)
);

CREATE INDEX idx_estoque_produto_empresa_id ON estoque_produto (empresa_id);
CREATE INDEX idx_estoque_produto_estoque_id ON estoque_produto (estoque_id);
CREATE INDEX idx_estoque_produto_produto_id ON estoque_produto (produto_id);

CREATE TABLE movimentacao_estoque (
    id BIGINT NOT NULL AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,
    tipo_movimentacao VARCHAR(30) NOT NULL,
    estoque_origem_id BIGINT NULL,
    estoque_destino_id BIGINT NULL,
    data_movimentacao DATETIME NOT NULL,
    observacao VARCHAR(500) NULL,
    documento_referencia VARCHAR(100) NULL,
    usuario_responsavel_id BIGINT NULL,
    status_movimentacao VARCHAR(20) NOT NULL,
    CONSTRAINT pk_movimentacao_estoque PRIMARY KEY (id),
    CONSTRAINT ck_movimentacao_estoque_tipo CHECK (
        tipo_movimentacao IN (
            'ENTRADA',
            'SAIDA',
            'TRANSFERENCIA',
            'AJUSTE_ENTRADA',
            'AJUSTE_SAIDA',
            'RESERVA',
            'LIBERACAO_RESERVA',
            'INVENTARIO',
            'ESTORNO'
        )
    ),
    CONSTRAINT ck_movimentacao_estoque_status CHECK (
        status_movimentacao IN (
            'PENDENTE',
            'PROCESSADA',
            'CANCELADA',
            'ESTORNADA',
            'REJEITADA'
        )
    )
);

CREATE INDEX idx_movimentacao_estoque_empresa_id ON movimentacao_estoque (empresa_id);
CREATE INDEX idx_movimentacao_estoque_origem_id ON movimentacao_estoque (estoque_origem_id);
CREATE INDEX idx_movimentacao_estoque_destino_id ON movimentacao_estoque (estoque_destino_id);
CREATE INDEX idx_movimentacao_estoque_data_movimentacao ON movimentacao_estoque (data_movimentacao);
CREATE INDEX idx_movimentacao_estoque_usuario_resp_id ON movimentacao_estoque (usuario_responsavel_id);
CREATE INDEX idx_movimentacao_estoque_status ON movimentacao_estoque (status_movimentacao);

CREATE TABLE movimentacao_estoque_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,
    movimentacao_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(19,4) NOT NULL,
    valor_unitario DECIMAL(19,4) NULL,
    CONSTRAINT pk_movimentacao_estoque_item PRIMARY KEY (id),
    CONSTRAINT fk_movimentacao_estoque_item_movimentacao
        FOREIGN KEY (movimentacao_id)
        REFERENCES movimentacao_estoque (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

CREATE INDEX idx_movimentacao_estoque_item_empresa_id ON movimentacao_estoque_item (empresa_id);
CREATE INDEX idx_movimentacao_estoque_item_movimentacao_id ON movimentacao_estoque_item (movimentacao_id);
CREATE INDEX idx_movimentacao_estoque_item_produto_id ON movimentacao_estoque_item (produto_id);
