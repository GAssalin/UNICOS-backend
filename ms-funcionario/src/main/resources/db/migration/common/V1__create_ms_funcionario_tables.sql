-- ============================================================
-- TABELA: cargos
-- ============================================================
CREATE TABLE cargos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    -- Papel do cargo (VENDEDOR, SUPERVISOR, GERENTE, DIRETOR, ADMINISTRATIVO):
    -- define o escopo de acesso a carteira de clientes.
    papel VARCHAR(30) NOT NULL,

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_cargos_empresa_nome UNIQUE (empresa_id, nome)
);

CREATE INDEX idx_cargos_papel ON cargos(papel);

-- ============================================================
-- TABELA: funcionarios
-- ============================================================
CREATE TABLE funcionarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    empresa_id BIGINT NOT NULL,

    -- pessoa_id e usuario_id referenciam logicamente o ms-pessoas e filial_id o
    -- ms-empresa, sem FK entre servicos.
    pessoa_id BIGINT NOT NULL,
    usuario_id BIGINT,
    matricula VARCHAR(30),
    cargo_id BIGINT NOT NULL,
    superior_id BIGINT,
    filial_id BIGINT,
    data_admissao DATE NOT NULL,
    data_desligamento DATE,
    status VARCHAR(30) NOT NULL,

    criado_por BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_por BIGINT,
    atualizado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    -- Colunas opcionais (usuario_id, matricula) aceitam varios NULL nas restricoes de unicidade.
    CONSTRAINT uk_funcionarios_empresa_pessoa UNIQUE (empresa_id, pessoa_id),
    CONSTRAINT uk_funcionarios_empresa_usuario UNIQUE (empresa_id, usuario_id),
    CONSTRAINT uk_funcionarios_empresa_matricula UNIQUE (empresa_id, matricula),

    CONSTRAINT fk_funcionarios_cargo
        FOREIGN KEY (cargo_id) REFERENCES cargos(id),

    CONSTRAINT fk_funcionarios_superior
        FOREIGN KEY (superior_id) REFERENCES funcionarios(id)
);

CREATE INDEX idx_funcionarios_usuario ON funcionarios(usuario_id);
CREATE INDEX idx_funcionarios_superior ON funcionarios(superior_id);
CREATE INDEX idx_funcionarios_filial ON funcionarios(filial_id);
CREATE INDEX idx_funcionarios_status ON funcionarios(status);
