-- V2__seed_ms_departamento_dados_ficticios.sql
-- Seed de dados fictícios para o microserviço ms-departamento (MVP)
-- Banco alvo: MySQL 8+ (InnoDB)

-- =========================================================
-- DEPARTAMENTOS (alguns raiz e alguns filhos)
-- =========================================================
INSERT INTO departamento (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    codigo, nome, descricao, status_departamento, departamento_pai_id
) VALUES
(
    1, 1, NOW(6), NULL, NULL, b'1',
    'DEP-ADM', 'Administrativo', 'Área administrativa e suporte interno.', 'ATIVO', NULL
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    'DEP-FIN', 'Financeiro', 'Contas a pagar/receber, conciliação e gestão financeira.', 'ATIVO', NULL
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    'DEP-COM', 'Comercial', 'Vendas, relacionamento e funil comercial.', 'ATIVO', NULL
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    'DEP-RH', 'Recursos Humanos', 'Rotinas de RH, benefícios e desenvolvimento.', 'ATIVO', NULL
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    'DEP-TI', 'Tecnologia', 'Infra, sistemas e suporte técnico.', 'ATIVO', NULL
);

-- filhos inseridos sem pai para evitar erro do MySQL ao consultar a mesma tabela no INSERT
INSERT INTO departamento (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    codigo, nome, descricao, status_departamento, departamento_pai_id
) VALUES
(
    1, 1, NOW(6), NULL, NULL, b'1',
    'DEP-FIN-AP', 'Financeiro - Contas a Pagar', 'Gestão de pagamentos e obrigações.', 'ATIVO', NULL
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    'DEP-FIN-AR', 'Financeiro - Contas a Receber', 'Cobrança, recebíveis e inadimplência.', 'ATIVO', NULL
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    'DEP-TI-SUP', 'TI - Suporte', 'Atendimento de chamados e apoio aos usuários.', 'ATIVO', NULL
);

-- relacionamento hierárquico definido em etapa separada
UPDATE departamento filho
JOIN departamento pai
  ON pai.empresa_id = filho.empresa_id
SET filho.departamento_pai_id = pai.id
WHERE filho.empresa_id = 1
  AND (
      (filho.codigo = 'DEP-FIN-AP' AND pai.codigo = 'DEP-FIN') OR
      (filho.codigo = 'DEP-FIN-AR' AND pai.codigo = 'DEP-FIN') OR
      (filho.codigo = 'DEP-TI-SUP' AND pai.codigo = 'DEP-TI')
  );

-- =========================================================
-- RESPONSÁVEIS DEPARTAMENTO
-- =========================================================
-- responsavel_id é integração lógica com ms-pessoas/ms-rh: ids fictícios (ex.: 501, 502...)
-- vigenciaFim nula = vigente

INSERT INTO responsavel_departamento (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    departamento_id, responsavel_id, papel, principal, vigencia_inicio, vigencia_fim, status_responsavel_departamento
) VALUES
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-ADM' LIMIT 1),
    501, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-FIN' LIMIT 1),
    502, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-COM' LIMIT 1),
    503, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-RH' LIMIT 1),
    504, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-TI' LIMIT 1),
    505, 'GESTOR', b'1', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-TI-SUP' LIMIT 1),
    506, 'PONTO_FOCAL', b'0', '2025-02-01', NULL, 'ATIVO'
);

-- =========================================================
-- VÍNCULOS DEPARTAMENTO x FILIAL
-- =========================================================
-- filial_id: ids fictícios (integração lógica com ms-filial)

-- Departamentos raiz vinculados a 3 filiais
INSERT INTO vinculo_departamento_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    departamento_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_departamento_filial
) VALUES
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-ADM' LIMIT 1),
    1001, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-ADM' LIMIT 1),
    1002, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-ADM' LIMIT 1),
    1003, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
);

-- Financeiro como corporativo atendendo várias filiais (mesmo departamento_id em múltiplas filiais)
INSERT INTO vinculo_departamento_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    departamento_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_departamento_filial
) VALUES
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-FIN' LIMIT 1),
    1001, 'CORPORATIVO', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-FIN' LIMIT 1),
    1002, 'CORPORATIVO', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-FIN' LIMIT 1),
    1003, 'CORPORATIVO', '2025-01-01', NULL, 'ATIVO'
);

-- TI local em 2 filiais e compartilhado em 1
INSERT INTO vinculo_departamento_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    departamento_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_departamento_filial
) VALUES
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-TI' LIMIT 1),
    1001, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-TI' LIMIT 1),
    1002, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
),
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-TI' LIMIT 1),
    1003, 'COMPARTILHADO', '2025-01-01', NULL, 'ATIVO'
);

-- Comercial apenas na filial 1001
INSERT INTO vinculo_departamento_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    departamento_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_departamento_filial
) VALUES
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-COM' LIMIT 1),
    1001, 'LOCAL', '2025-01-01', NULL, 'ATIVO'
);

-- RH descontinuado na filial 1003 (exemplo de histórico com vigencia_fim e status INATIVO)
INSERT INTO vinculo_departamento_filial (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    departamento_id, filial_id, tipo_atuacao, vigencia_inicio, vigencia_fim, status_vinculo_departamento_filial
) VALUES
(
    1, 1, NOW(6), NULL, NULL, b'1',
    (SELECT id FROM departamento WHERE empresa_id = 1 AND codigo = 'DEP-RH' LIMIT 1),
    1003, 'LOCAL', '2024-01-01', '2024-12-31', 'INATIVO'
);
