-- ============================================================
--  INSERT INICIAIS: MS-Permissao
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
(1, 'ROLE_CRIAR', 'Permite criar papéis (roles)', NOW(), 1),
(1, 'ROLE_EDITAR', 'Permite alterar papéis (roles)', NOW(), 1),
(1, 'ROLE_LISTAR', 'Permite listar papéis (roles)', NOW(), 1),
(1, 'ROLE_EXCLUIR', 'Permite remover papéis (roles)', NOW(), 1),

(1, 'PERMISSAO_CRIAR', 'Permite criar permissões', NOW(), 1),
(1, 'PERMISSAO_EDITAR', 'Permite atualizar permissões', NOW(), 1),
(1, 'PERMISSAO_LISTAR', 'Permite excluir permissões', NOW(), 1),
(1, 'PERMISSAO_EXCLUIR', 'Permite listar permissões', NOW(), 1),

(1, 'ROLE_PERMISSAO_CRIAR', 'Permite criar papéis (roles)', NOW(), 1),
(1, 'ROLE_PERMISSAO_EDITAR', 'Permite ativar ou desativar vínculos role/permissão', NOW(), 1),
(1, 'ROLE_PERMISSAO_LISTAR', 'Permite listar vínculos de role/permissão', NOW(), 1),
(1, 'ROLE_PERMISSAO_EXCLUIR', 'Permite remover vínculo role/permissão', NOW(), 1),

(1, 'ROLE_USUARIO_CRIAR', 'Permite criar papéis (roles)', NOW(), 1),
(1, 'ROLE_USUARIO_EDITAR', 'Permite ativar ou desativar vínculos role/permissão', NOW(), 1),
(1, 'ROLE_USUARIO_LISTAR', 'Permite listar vínculos de role/permissão', NOW(), 1),
(1, 'ROLE_USUARIO_EXCLUIR', 'Permite remover vínculo role/permissão', NOW(), 1);
