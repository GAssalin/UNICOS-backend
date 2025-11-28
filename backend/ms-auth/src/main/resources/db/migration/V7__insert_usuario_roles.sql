-- ============================================================
--  V7 - Vínculo inicial entre Usuários e Roles
-- ============================================================

-- ADMIN possui todos os papéis
INSERT INTO usuario_role (usuario_id, role_id) VALUES
(1, 1), -- ROLE_ADMIN
(1, 2), -- ROLE_GERENTE
(1, 3), -- ROLE_OPERADOR
(1, 4), -- ROLE_LEITURA
(1, 5); -- ROLE_SUPORTE

-- GERENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES
(2, 2), -- ROLE_GERENTE
(2, 3), -- ROLE_OPERADOR
(2, 4); -- ROLE_LEITURA

-- OPERADOR
INSERT INTO usuario_role (usuario_id, role_id) VALUES
(3, 3), -- ROLE_OPERADOR
(3, 4); -- ROLE_LEITURA

-- SUPORTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES
(4, 5), -- ROLE_SUPORTE
(4, 4); -- ROLE_LEITURA

-- LEITURA
INSERT INTO usuario_role (usuario_id, role_id) VALUES
(5, 4); -- ROLE_LEITURA
