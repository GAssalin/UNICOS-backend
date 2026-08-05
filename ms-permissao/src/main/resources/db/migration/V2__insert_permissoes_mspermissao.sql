-- ============================================================
--  INSERT INICIAIS: MS-Permissao
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
-- ROLE
(1, 'ROLE_CRIAR', 'Permite criar papéis (roles)', NOW(), 1),
(1, 'ROLE_EDITAR', 'Permite atualizar papéis (roles)', NOW(), 1),
(1, 'ROLE_LISTAR', 'Permite listar papéis (roles)', NOW(), 1),
(1, 'ROLE_EXCLUIR', 'Permite remover papéis (roles)', NOW(), 1),

-- PERMISSAO
(1, 'PERMISSAO_CRIAR', 'Permite criar permissões', NOW(), 1),
(1, 'PERMISSAO_EDITAR', 'Permite atualizar permissões', NOW(), 1),
(1, 'PERMISSAO_LISTAR', 'Permite listar permissões', NOW(), 1),
(1, 'PERMISSAO_EXCLUIR', 'Permite remover permissões', NOW(), 1),

-- ROLE_PERMISSAO
(1, 'ROLE_PERMISSAO_CRIAR', 'Permite criar vínculo entre role e permissão', NOW(), 1),
(1, 'ROLE_PERMISSAO_EDITAR', 'Permite atualizar vínculo entre role e permissão', NOW(), 1),
(1, 'ROLE_PERMISSAO_LISTAR', 'Permite listar vínculos entre role e permissão', NOW(), 1),
(1, 'ROLE_PERMISSAO_EXCLUIR', 'Permite remover vínculo entre role e permissão', NOW(), 1),

-- ROLE_USUARIO
(1, 'ROLE_USUARIO_CRIAR', 'Permite criar vínculo entre usuário e role', NOW(), 1),
(1, 'ROLE_USUARIO_EDITAR', 'Permite atualizar vínculo entre usuário e role', NOW(), 1),
(1, 'ROLE_USUARIO_LISTAR', 'Permite listar vínculos entre usuário e role', NOW(), 1),
(1, 'ROLE_USUARIO_EXCLUIR', 'Permite remover vínculo entre usuário e role', NOW(), 1);