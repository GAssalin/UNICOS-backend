-- ============================================================
--  V4 - INSERT INICIAIS: Relacionamento Role ↔ Permissão
-- ============================================================

-- ADMIN: Todas as permissões
INSERT INTO role_permissao (role_id, permissao_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10);

-- GERENTE: Gerencia usuários e papéis
INSERT INTO role_permissao (role_id, permissao_id) VALUES
(2, 1), -- criar usuário
(2, 2), -- editar usuário
(2, 3), -- listar usuário
(2, 5), -- criar role
(2, 6), -- editar role
(2, 7), -- listar role
(2, 9); -- listar permissão

-- OPERADOR: Apenas operações básicas
INSERT INTO role_permissao (role_id, permissao_id) VALUES
(3, 3), -- listar usuários
(3, 7), -- listar roles
(3, 9); -- listar permissões

-- SUPORTE: Permissões de consulta e investigação
INSERT INTO role_permissao (role_id, permissao_id) VALUES
(4, 3),
(4, 7),
(4, 9);

-- LEITURA: Somente leitura mínima
INSERT INTO role_permissao (role_id, permissao_id) VALUES
(5, 3), -- listar usuários
(5, 7); -- listar roles
