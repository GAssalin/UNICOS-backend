-- ============================================================
--  V2 - INSERT INICIAIS: Permissões (Tenant 0)
-- ============================================================

INSERT INTO permissao (
    empresa_id,
    nome,
    descricao,
    criado_em,
    ativo
) VALUES
(1, 'USUARIO_CRIAR',    'Permite criar novos usuários', NOW(), 1),
(1, 'USUARIO_EDITAR',   'Permite atualizar informações de usuários', NOW(), 1),
(1, 'USUARIO_LISTAR',   'Permite visualizar usuários', NOW(), 1),
(1, 'USUARIO_EXCLUIR',  'Permite excluir usuários', NOW(), 1),

(1, 'ROLE_CRIAR',       'Permite criar papéis (roles)', NOW(), 1),
(1, 'ROLE_EDITAR',      'Permite alterar papéis (roles)', NOW(), 1),
(1, 'ROLE_LISTAR',      'Permite listar papéis (roles)', NOW(), 1),
(1, 'ROLE_EXCLUIR',     'Permite remover papéis (roles)', NOW(), 1),

(1, 'PERMISSAO_CRIAR',  'Permite criar permissões', NOW(), 1),
(1, 'PERMISSAO_EDITAR', 'Permite atualizar permissões', NOW(), 1),
(1, 'PERMISSAO_EXCLUIR','Permite excluir permissões', NOW(), 1),
(1, 'PERMISSAO_LISTAR','Permite listar permissões', NOW(), 1),

(1, 'EMPRESA_ROLE_PERMISSAO_CRIAR',   'Permite vincular permissões a roles', NOW(), 1),
(1, 'EMPRESA_ROLE_PERMISSAO_EDITAR',  'Permite ativar ou desativar vínculos', NOW(), 1),
(1, 'EMPRESA_ROLE_PERMISSAO_LISTAR',  'Permite listar permissões por role', NOW(), 1),
(1, 'EMPRESA_ROLE_PERMISSAO_EXCLUIR', 'Permite remover vínculo role/permissão', NOW(), 1),

(1, 'AUDITORIA_LISTAR', 'Permite visualizar registros de auditoria', NOW(), 1),

(1, 'SISTEMA_CONFIGURAR', 'Permite configurar o sistema', NOW(), 1),
(1, 'SUPORTE_ACESSAR',    'Permite acesso a suporte técnico', NOW(), 1);
