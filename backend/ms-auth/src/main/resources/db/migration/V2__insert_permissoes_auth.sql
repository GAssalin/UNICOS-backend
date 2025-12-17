-- ============================================================
--  V2 - INSERT INICIAIS: Permissões (Globais)
-- ============================================================

INSERT INTO permissao (
    id, nome, descricao,
    criado_em, ativo
) VALUES

-- MS-AUTH :: USUÁRIO
(1,  'USUARIO_CRIAR',    'Permite criar novos usuários', NOW(), 1),
(2,  'USUARIO_EDITAR',   'Permite atualizar informações de usuários', NOW(), 1),
(3,  'USUARIO_LISTAR',   'Permite visualizar usuários', NOW(), 1),
(4,  'USUARIO_EXCLUIR',  'Permite excluir usuários', NOW(), 1),

-- MS-AUTH :: ROLE
(5,  'ROLE_CRIAR',       'Permite criar papéis (roles)', NOW(), 1),
(6,  'ROLE_EDITAR',      'Permite alterar papéis (roles)', NOW(), 1),
(7,  'ROLE_LISTAR',      'Permite listar papéis (roles)', NOW(), 1),
(8,  'ROLE_EXCLUIR',     'Permite remover papéis (roles)', NOW(), 1),

-- MS-AUTH :: PERMISSÃO
(9,  'PERMISSAO_CRIAR',  'Permite criar permissões', NOW(), 1),
(10, 'PERMISSAO_EDITAR', 'Permite atualizar permissões', NOW(), 1),
(11, 'PERMISSAO_EXCLUIR','Permite excluir permissões', NOW(), 1),
(12, 'PERMISSAO_LISTAR','Permite listar permissões', NOW(), 1),

-- EMPRESA x ROLE x PERMISSÃO
(13, 'EMPRESA_ROLE_PERMISSAO_CRIAR',   'Permite vincular permissões a roles', NOW(), 1),
(14, 'EMPRESA_ROLE_PERMISSAO_EDITAR',  'Permite ativar ou desativar vínculos', NOW(), 1),
(15, 'EMPRESA_ROLE_PERMISSAO_LISTAR',  'Permite listar permissões por role', NOW(), 1),
(16, 'EMPRESA_ROLE_PERMISSAO_EXCLUIR', 'Permite remover vínculo role/permissão', NOW(), 1),

-- AUDITORIA
(17, 'AUDITORIA_LISTAR', 'Permite visualizar registros de auditoria', NOW(), 1),

-- SISTEMA / SUPORTE
(18, 'SISTEMA_CONFIGURAR', 'Permite configurar o sistema', NOW(), 1),
(19, 'SUPORTE_ACESSAR',    'Permite acesso a suporte técnico', NOW(), 1);
