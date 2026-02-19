-- V2__seed_ms_estoque_dados_ficticios.sql
-- Seed de dados fictícios para o microserviço ms-estoque (MVP)
-- Banco alvo: MySQL 8+ (InnoDB)

-- Observações:
-- 1) Ajuste o tenant (empresa_id) conforme seu ambiente.
-- 2) "ativo" é BIT(1): use b'1' e b'0'.
-- 3) criado_em é obrigatório (EntidadeAuditavel). Usaremos NOW(6).
-- 4) filial_id é integração lógica (ms-filial): valores fictícios (ex.: 1001, 1002, 1003).

SET @TENANT_ID := 1;
SET @CRIADO_POR := 1;

-- =========================================================
-- DEPARTAMENTOS (alguns raiz e alguns filhos)
-- =========================================================
INSERT INTO estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    codigo, nome, descricao, status_estoque, estoque_pai_id
) VALUES
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    'DEP-ADM', 'Administrativo', 'Área administrativa e suporte interno.', 'ATIVO', NULL
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    'DEP-FIN', 'Financeiro', 'Contas a pagar/receber, conciliação e gestão financeira.', 'ATIVO', NULL
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    'DEP-COM', 'Comercial', 'Vendas, relacionamento e funil comercial.', 'ATIVO', NULL
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    'DEP-RH', 'Recursos Humanos', 'Rotinas de RH, benefícios e desenvolvimento.', 'ATIVO', NULL
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    'DEP-TI', 'Tecnologia', 'Infra, sistemas e suporte técnico.', 'ATIVO', NULL
);

-- filhos (estoque_pai_id via subquery por código)
INSERT INTO estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    codigo, nome, descricao, status_estoque, estoque_pai_id
) VALUES
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    'DEP-FIN-AP', 'Financeiro - Contas a Pagar', 'Gestão de pagamentos e obrigações.', 'ATIVO',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-FIN' LIMIT 1)
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    'DEP-FIN-AR', 'Financeiro - Contas a Receber', 'Cobrança, recebíveis e inadimplência.', 'ATIVO',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-FIN' LIMIT 1)
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    'DEP-TI-SUP', 'TI - Suporte', 'Atendimento de chamados e apoio aos usuários.', 'ATIVO',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-TI' LIMIT 1)
);

-- =========================================================
-- RESPONSÁVEIS DEPARTAMENTO
-- =========================================================
-- responsavel_id é integração lógica com ms-pessoas/ms-rh: ids fictícios (ex.: 501, 502...)
-- vigenciaFim nula = vigente

INSERT INTO responsavel_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, responsavel_id, papel, principal, vigencia_inicio, vigencia_fim, status_responsavel_estoque
) VALUES
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-ADM' LIMIT 1),
    501, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-FIN' LIMIT 1),
    502, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-COM' LIMIT 1),
    503, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-RH' LIMIT 1),
    504, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-TI' LIMIT 1),
    505, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-TI-SUP' LIMIT 1),
    506, 'PONTO_FOCAL', b'0', '2025-02-01', NULL, 'ATIVO'
);

-- =========================================================
-- VÍNCULOS DEPARTAMENTO x FILIAL
-- =========================================================
-- filial_id: ids fictícios (integração lógica com ms-filial)

-- Estoques raiz vinculados a 3 filiais
INSERT INTO vinculo_estoque_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_estoque_filial
) VALUES
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-ADM' LIMIT 1),
    1001, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-ADM' LIMIT 1),
    1002, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-ADM' LIMIT 1),
    1003, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
);

-- Financeiro como corporativo atendendo várias filiais (mesmo estoque_id em múltiplas filiais)
INSERT INTO vinculo_estoque_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_estoque_filial
) VALUES
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-FIN' LIMIT 1),
    1001, 'CORPORATIVO', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-FIN' LIMIT 1),
    1002, 'CORPORATIVO', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-FIN' LIMIT 1),
    1003, 'CORPORATIVO', '2025-01-01', NULL, 'ATIVO'
);

-- TI local em 2 filiais e compartilhado em 1
INSERT INTO vinculo_estoque_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_estoque_filial
) VALUES
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-TI' LIMIT 1),
    1001, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-TI' LIMIT 1),
    1002, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
),
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-TI' LIMIT 1),
    1003, 'COMPARTILHADO', '2025-01-01', NULL, 'ATIVO'
);

-- Comercial apenas na filial 1001
INSERT INTO vinculo_estoque_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_estoque_filial
) VALUES
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-COM' LIMIT 1),
    1001, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
);

-- RH descontinuado na filial 1003 (exemplo de histórico com vigencia_fim e status INATIVO)
INSERT INTO vinculo_estoque_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_estoque_filial
) VALUES
(
    @TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM estoque WHERE empresa_id = @TENANT_ID AND codigo = 'DEP-RH' LIMIT 1),
    1003, 'LOCAL', '2024-01-01', '2024-12-31', 'INATIVO'
);
