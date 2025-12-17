-- ============================================================
--  V4 - INSERT INICIAL: Hierarquia de Papéis
-- ============================================================

INSERT INTO role_hierarchy_relation (
    id, parent_role, child_role,
    criado_em, ativo
) VALUES

-- ADMIN herda tudo
(1, 'ADMIN', 'GERENTE', NOW(), 1),
(2, 'ADMIN', 'SUPORTE', NOW(), 1),

-- GERENTE herda OPERADOR
(3, 'GERENTE', 'OPERADOR', NOW(), 1),

-- OPERADOR herda LEITURA
(4, 'OPERADOR', 'LEITURA', NOW(), 1);
