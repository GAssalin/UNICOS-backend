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
(1, 'ADMIN',    'GERENTE',  NOW(), 1),
(1, 'ADMIN',    'SUPORTE',  NOW(), 1),
(1, 'GERENTE',  'OPERADOR', NOW(), 1),
(1, 'OPERADOR', 'LEITURA',  NOW(), 1);
