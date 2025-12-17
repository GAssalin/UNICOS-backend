-- ============================================================
--  V4 - INSERT INICIAL: Hierarquia de Papéis
-- ============================================================

INSERT INTO role_hierarchy_relation (
    empresa_id,
    parent_role,
    child_role,
    criado_em,
    ativo
) VALUES
(0, 'ADMIN',    'GERENTE',  NOW(), 1),
(0, 'ADMIN',    'SUPORTE',  NOW(), 1),
(0, 'GERENTE',  'OPERADOR', NOW(), 1),
(0, 'OPERADOR', 'LEITURA',  NOW(), 1);
