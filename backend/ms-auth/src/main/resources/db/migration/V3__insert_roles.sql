-- ============================================================
--  V3 - INSERT INICIAIS: Roles
-- ============================================================

INSERT INTO role (id, nome, descricao) VALUES
(1, 'ADMIN', 'Acesso total ao módulo de autenticação e autorização'),
(2, 'GERENTE', 'Gerencia usuários e papéis com permissões intermediárias'),
(3, 'OPERADOR', 'Acesso operacional básico sem permissões administrativas'),
(4, 'SUPORTE', 'Acesso restrito para consultas e apoio técnico'),
(5, 'LEITURA', 'Somente leitura, sem capacidade de alteração');
