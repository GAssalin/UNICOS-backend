-- V1__create_tables_ms_departamento.sql
-- Criação das tabelas do microserviço ms-departamento (MVP)
-- Banco alvo: MySQL 8+ (InnoDB)

-- =========================================================
-- TABELA: departamento
-- =========================================================
CREATE TABLE IF NOT EXISTS departamento (
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
    status_departamento VARCHAR(20) NOT NULL,
    departamento_pai_id BIGINT NULL,

    CONSTRAINT pk_departamento PRIMARY KEY (id),
    CONSTRAINT uk_departamento_codigo UNIQUE (codigo),

    -- opcional (auto-relacionamento lógico). Mantido como FK física por performance/consistência.
    -- Se você quiser "sem FK" até intra-MS, remova esta constraint.
    CONSTRAINT fk_departamento_pai
        FOREIGN KEY (departamento_pai_id)
        REFERENCES departamento (id)
        ON DELETE SET NULL
        ON UPDATE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX ix_departamento_empresa_id ON departamento (empresa_id);
CREATE INDEX ix_departamento_status ON departamento (status_departamento);
CREATE INDEX ix_departamento_pai ON departamento (departamento_pai_id);

-- =========================================================
-- TABELA: responsavel_departamento
-- =========================================================
CREATE TABLE IF NOT EXISTS responsavel_departamento (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- tenant / auditoria
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL,

    -- campos do domínio
    departamento_id BIGINT NOT NULL,
    responsavel_id BIGINT NOT NULL,
    papel VARCHAR(30) NOT NULL,
    principal BIT(1) NOT NULL,
    vigencia_inicio DATE NOT NULL,
    vigencia_fim DATE NULL,
    status_responsavel_departamento VARCHAR(20) NOT NULL,

    CONSTRAINT pk_responsavel_departamento PRIMARY KEY (id),

    -- FK física interna do MS (recomendado). Se não quiser, remova.
    CONSTRAINT fk_resp_dep_departamento
        FOREIGN KEY (departamento_id)
        REFERENCES departamento (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX ix_resp_dep_empresa_id ON responsavel_departamento (empresa_id);
CREATE INDEX ix_resp_dep_departamento_id ON responsavel_departamento (departamento_id);
CREATE INDEX ix_resp_dep_responsavel_id ON responsavel_departamento (responsavel_id);
CREATE INDEX ix_resp_dep_status ON responsavel_departamento (status_responsavel_departamento);
CREATE INDEX ix_resp_dep_principal ON responsavel_departamento (departamento_id, principal);

-- =========================================================
-- TABELA: vinculo_departamento_filial
-- =========================================================
CREATE TABLE IF NOT EXISTS vinculo_departamento_filial (
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- tenant / auditoria
    empresa_id BIGINT NOT NULL,
    criado_por BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_por BIGINT NULL,
    atualizado_em DATETIME(6) NULL,
    ativo BIT(1) NOT NULL,

    -- campos do domínio
    departamento_id BIGINT NOT NULL,
    filial_id BIGINT NOT NULL,
    tipo_atuacao VARCHAR(30) NOT NULL,
    vigencia_inicio DATE NOT NULL,
    vigencia_fim DATE NULL,
    status_vinculo_departamento_filial VARCHAR(20) NOT NULL,

    CONSTRAINT pk_vinculo_departamento_filial PRIMARY KEY (id),

    -- FK física interna do MS (recomendado). filial_id é externo (ms-filial), então não há FK.
    CONSTRAINT fk_vinc_dep_fil_departamento
        FOREIGN KEY (departamento_id)
        REFERENCES departamento (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX ix_vinc_dep_fil_empresa_id ON vinculo_departamento_filial (empresa_id);
CREATE INDEX ix_vinc_dep_fil_departamento_id ON vinculo_departamento_filial (departamento_id);
CREATE INDEX ix_vinc_dep_fil_filial_id ON vinculo_departamento_filial (filial_id);
CREATE INDEX ix_vinc_dep_fil_status ON vinculo_departamento_filial (status_vinculo_departamento_filial);
CREATE INDEX ix_vinc_dep_fil_tipo_atuacao ON vinculo_departamento_filial (tipo_atuacao);

-- Evita duplicidade do par (departamento_id, filial_id) dentro do tenant.
-- Mantém 1 linha por par; histórico pode ser feito via vigência e atualização da mesma linha,
-- ou você remove a unique e passa a controlar via (vigencia_fim) e múltiplas linhas.
ALTER TABLE vinculo_departamento_filial
    ADD CONSTRAINT uk_vinc_dep_fil_tenant_departamento_filial
    UNIQUE (empresa_id, departamento_id, filial_id);
