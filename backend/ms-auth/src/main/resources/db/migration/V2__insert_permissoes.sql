-- ============================================================
--  V2 - INSERT INICIAIS: Permissões
-- ============================================================

INSERT INTO permissao (id, nome, descricao) VALUES
(1, 'USUARIO_CRIAR', 'Permite criar novos usuários'),
(2, 'USUARIO_EDITAR', 'Permite atualizar informações de usuários'),
(3, 'USUARIO_LISTAR', 'Permite visualizar usuários'),
(4, 'USUARIO_EXCLUIR', 'Permite excluir um usuário'),

(5, 'ROLE_CRIAR', 'Permite criar papéis'),
(6, 'ROLE_EDITAR', 'Permite alterar papéis'),
(7, 'ROLE_LISTAR', 'Permite ver a lista de papéis'),
(8, 'ROLE_EXCLUIR', 'Permite remover papéis'),

(9, 'PERMISSAO_LISTAR', 'Permite listar permissões disponíveis'),
(10, 'PERMISSAO_GERENCIAR', 'Permite gestão completa de permissões');
