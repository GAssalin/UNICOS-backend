-- ============================================================
--  V2 - INSERT INICIAIS: Permissões (Globais)
-- ============================================================

INSERT INTO permissao (id, nome, descricao) VALUES

-- ============================================================
--  MS-AUTH :: USUÁRIO
-- ============================================================

(1,  'USUARIO_CRIAR',    'Permite criar novos usuários'),
(2,  'USUARIO_EDITAR',   'Permite atualizar informações de usuários'),
(3,  'USUARIO_LISTAR',   'Permite visualizar usuários'),
(4,  'USUARIO_EXCLUIR',  'Permite excluir usuários'),

-- ============================================================
--  MS-AUTH :: ROLE
-- ============================================================

(5,  'ROLE_CRIAR',       'Permite criar papéis (roles)'),
(6,  'ROLE_EDITAR',      'Permite alterar papéis (roles)'),
(7,  'ROLE_LISTAR',      'Permite listar papéis (roles)'),
(8,  'ROLE_EXCLUIR',     'Permite remover papéis (roles)'),

-- ============================================================
--  MS-AUTH :: PERMISSÃO
-- ============================================================

(9,  'PERMISSAO_CRIAR',  'Permite criar permissões'),
(10, 'PERMISSAO_EDITAR', 'Permite atualizar permissões'),
(11, 'PERMISSAO_EXCLUIR','Permite excluir permissões'),
(12, 'PERMISSAO_LISTAR','Permite listar permissões'),

-- ============================================================
--  MS-AUTH :: EMPRESA x ROLE x PERMISSÃO
-- ============================================================

(13, 'EMPRESA_ROLE_PERMISSAO_CRIAR',   'Permite vincular permissões a roles de uma empresa'),
(14, 'EMPRESA_ROLE_PERMISSAO_EDITAR',  'Permite ativar ou desativar permissões de uma role'),
(15, 'EMPRESA_ROLE_PERMISSAO_LISTAR',  'Permite listar permissões por empresa e role'),
(16, 'EMPRESA_ROLE_PERMISSAO_EXCLUIR', 'Permite remover vínculo entre role e permissão'),

-- ============================================================
--  MS-AUTH :: AUDITORIA
-- ============================================================

(17, 'AUDITORIA_LISTAR', 'Permite visualizar registros de auditoria'),

-- ============================================================
--  MS-AUTH :: SISTEMA / SUPORTE
-- ============================================================

(18, 'SISTEMA_CONFIGURAR', 'Permite acesso a configurações avançadas do sistema'),
(19, 'SUPORTE_ACESSAR',    'Permite acesso a funcionalidades de suporte técnico');
