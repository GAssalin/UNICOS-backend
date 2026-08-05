-- ============================================================
-- TABELA: cliente_categorias
-- ============================================================
CREATE TABLE cliente_categorias (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_cliente_categorias_empresa ON cliente_categorias(empresa_id);
CREATE INDEX idx_cliente_categorias_nome ON cliente_categorias(nome);

-- ============================================================
-- TABELA: clientes
-- ============================================================
CREATE TABLE clientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    pessoa_id BIGINT NOT NULL,
    vendedor_id BIGINT NOT NULL,
    filial_id BIGINT,
    codigo_interno VARCHAR(50),
    status VARCHAR(30) NOT NULL,
    categoria_id BIGINT,
    observacao_geral VARCHAR(1000),
    permite_venda_a_prazo BOOLEAN NOT NULL,
    limite_credito DECIMAL(15, 2),

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_clientes_pessoa
        FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),

    CONSTRAINT fk_clientes_categoria
        FOREIGN KEY (categoria_id) REFERENCES cliente_categorias(id)
);

CREATE INDEX idx_clientes_empresa ON clientes(empresa_id);
CREATE INDEX idx_clientes_pessoa ON clientes(pessoa_id);
CREATE INDEX idx_clientes_vendedor ON clientes(vendedor_id);
CREATE INDEX idx_clientes_filial ON clientes(filial_id);
CREATE INDEX idx_clientes_categoria ON clientes(categoria_id);
CREATE INDEX idx_clientes_status ON clientes(status);
CREATE INDEX idx_clientes_codigo_interno ON clientes(codigo_interno);

-- ============================================================
-- TABELA: cliente_historico_status
-- ============================================================
CREATE TABLE cliente_historico_status (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    cliente_id BIGINT NOT NULL,
    status_anterior VARCHAR(30),
    status_novo VARCHAR(30) NOT NULL,
    motivo VARCHAR(500),

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_cliente_historico_status_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_cliente_historico_status_empresa ON cliente_historico_status(empresa_id);
CREATE INDEX idx_cliente_historico_status_cliente ON cliente_historico_status(cliente_id);
CREATE INDEX idx_cliente_historico_status_status_novo ON cliente_historico_status(status_novo);

-- ============================================================
-- TABELA: cliente_observacoes
-- ============================================================
CREATE TABLE cliente_observacoes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    cliente_id BIGINT NOT NULL,
    titulo VARCHAR(100),
    descricao VARCHAR(1000) NOT NULL,
    tipo VARCHAR(30) NOT NULL,

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_cliente_observacoes_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_cliente_observacoes_empresa ON cliente_observacoes(empresa_id);
CREATE INDEX idx_cliente_observacoes_cliente ON cliente_observacoes(cliente_id);
CREATE INDEX idx_cliente_observacoes_tipo ON cliente_observacoes(tipo);
