-- V1__create_tables_ms_estoque.sql
-- Criação das tabelas do microserviço ms-estoque (MVP)
-- Banco alvo: MySQL 8+ (InnoDB)

-- =========================================================
-- TABELA: estoque
-- =========================================================
CREATE TABLE IF NOT EXISTS estoque (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- tenant / auditoria (BaseTenantEntity -> EntidadeAuditavel)
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL,

    -- campos do domínio
    codigo VARCHAR(30) NOT NULL,
    nome VARCHAR(200) NOT NULL,
    descricao VARCHAR(500) NULL,
    status_estoque VARCHAR(20) NOT NULL,
    estoque_pai_id BIGINT NULL,

    CONSTRAINT pk_estoque PRIMARY KEY (id),
    CONSTRAINT uk_estoque_codigo UNIQUE (codigo),

    -- opcional (auto-relacionamento lógico). Mantido como FK física por performance/consistência.
    -- Se você quiser "sem FK" até intra-MS, remova esta constraint.
    CONSTRAINT fk_estoque_pai
        FOREIGN KEY (estoque_pai_id)
        REFERENCES estoque (id)
        ON DELETE SET NULL
        ON UPDATE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX ix_estoque_empresa_id ON estoque (empresa_id);
CREATE INDEX ix_estoque_status ON estoque (status_estoque);
CREATE INDEX ix_estoque_pai ON estoque (estoque_pai_id);

-- =========================================================
-- TABELA: responsavel_estoque
-- =========================================================
CREATE TABLE IF NOT EXISTS responsavel_estoque (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- tenant / auditoria
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL,

    -- campos do domínio
    estoque_id BIGINT NOT NULL,
    responsavel_id BIGINT NOT NULL,
    papel VARCHAR(30) NOT NULL,
    principal BIT(1) NOT NULL,
    vigencia_inicio DATE NOT NULL,
    vigencia_fim DATE NULL,
    status_responsavel_estoque VARCHAR(20) NOT NULL,

    CONSTRAINT pk_responsavel_estoque PRIMARY KEY (id),

    -- FK física interna do MS (recomendado). Se não quiser, remova.
    CONSTRAINT fk_resp_dep_estoque
        FOREIGN KEY (estoque_id)
        REFERENCES estoque (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX ix_resp_dep_empresa_id ON responsavel_estoque (empresa_id);
CREATE INDEX ix_resp_dep_estoque_id ON responsavel_estoque (estoque_id);
CREATE INDEX ix_resp_dep_responsavel_id ON responsavel_estoque (responsavel_id);
CREATE INDEX ix_resp_dep_status ON responsavel_estoque (status_responsavel_estoque);
CREATE INDEX ix_resp_dep_principal ON responsavel_estoque (estoque_id, principal);

-- =========================================================
-- TABELA: vinculo_estoque_filial
-- =========================================================
CREATE TABLE IF NOT EXISTS vinculo_estoque_filial (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- tenant / auditoria
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL,

    -- campos do domínio
    estoque_id BIGINT NOT NULL,
    filial_id BIGINT NOT NULL,
    tipo_atuacao VARCHAR(30) NOT NULL,
    vigencia_inicio DATE NOT NULL,
    vigencia_fim DATE NULL,
    status_vinculo_estoque_filial VARCHAR(20) NOT NULL,

    CONSTRAINT pk_vinculo_estoque_filial PRIMARY KEY (id),

    -- FK física interna do MS (recomendado). filial_id é externo (ms-filial), então não há FK.
    CONSTRAINT fk_vinc_dep_fil_estoque
        FOREIGN KEY (estoque_id)
        REFERENCES estoque (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX ix_vinc_dep_fil_empresa_id ON vinculo_estoque_filial (empresa_id);
CREATE INDEX ix_vinc_dep_fil_estoque_id ON vinculo_estoque_filial (estoque_id);
CREATE INDEX ix_vinc_dep_fil_filial_id ON vinculo_estoque_filial (filial_id);
CREATE INDEX ix_vinc_dep_fil_status ON vinculo_estoque_filial (status_vinculo_estoque_filial);
CREATE INDEX ix_vinc_dep_fil_tipo_atuacao ON vinculo_estoque_filial (tipo_atuacao);

-- Evita duplicidade do par (estoque_id, filial_id) dentro do tenant.
-- Mantém 1 linha por par; histórico pode ser feito via vigência e atualização da mesma linha,
-- ou você remove a unique e passa a controlar via (vigencia_fim) e múltiplas linhas.
ALTER TABLE vinculo_estoque_filial
    ADD CONSTRAINT uk_vinc_dep_fil_tenant_estoque_filial
    UNIQUE (empresa_id, estoque_id, filial_id);
