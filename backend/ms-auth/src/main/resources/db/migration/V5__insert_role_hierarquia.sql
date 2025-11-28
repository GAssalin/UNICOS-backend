-- ============================================================
--  V5 - INSERT INICIAIS: Hierarquia de Papeis (RoleHierarchyRelation)
-- ============================================================

-- ADMIN herda tudo
INSERT INTO role_hierarchy_relation (id, parent_role, child_role) VALUES
(1, 'ROLE_ADMIN', 'ROLE_GERENTE'),
(2, 'ROLE_ADMIN', 'ROLE_SUPORTE'),

-- GERENTE herda OPERADOR
(3, 'ROLE_GERENTE', 'ROLE_OPERADOR'),

-- OPERADOR herda LEITURA
(4, 'ROLE_OPERADOR', 'ROLE_LEITURA');
