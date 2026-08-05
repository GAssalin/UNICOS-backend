-- ============================================================
--  INSERT INICIAIS: MS-Usuarios
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
-- EMAIL
(1, 'USUARIO_EMAIL_CRIAR', 'Permite criar solicitações de verificação de e-mail de usuário', NOW(), 1),
(1, 'USUARIO_EMAIL_EDITAR', 'Permite atualizar verificação de e-mail de usuário', NOW(), 1),
(1, 'USUARIO_EMAIL_LISTAR', 'Permite consultar verificação de e-mail de usuário', NOW(), 1),
(1, 'USUARIO_EMAIL_EXCLUIR', 'Permite remover verificação de e-mail de usuário', NOW(), 1),

-- USUARIO
(1, 'USUARIO_CRIAR', 'Permite criar usuários', NOW(), 1),
(1, 'USUARIO_EDITAR', 'Permite editar usuários', NOW(), 1),
(1, 'USUARIO_LISTAR', 'Permite listar usuários', NOW(), 1),
(1, 'USUARIO_EXCLUIR', 'Permite excluir usuários', NOW(), 1),

-- PERFIL / DADOS CADASTRAIS
(1, 'USUARIO_PERFIL_VISUALIZAR', 'Permite visualizar perfil do usuário', NOW(), 1),
(1, 'USUARIO_PERFIL_EDITAR', 'Permite editar perfil do usuário', NOW(), 1),

-- SENHA
(1, 'USUARIO_SENHA_ALTERAR', 'Permite alterar senha do usuário', NOW(), 1),
(1, 'USUARIO_SENHA_RESETAR', 'Permite resetar senha do usuário', NOW(), 1),

-- TELEFONE
(1, 'USUARIO_TELEFONE_CRIAR', 'Permite cadastrar telefone do usuário', NOW(), 1),
(1, 'USUARIO_TELEFONE_EDITAR', 'Permite editar telefone do usuário', NOW(), 1),
(1, 'USUARIO_TELEFONE_LISTAR', 'Permite consultar telefone do usuário', NOW(), 1),
(1, 'USUARIO_TELEFONE_EXCLUIR', 'Permite remover telefone do usuário', NOW(), 1),

-- ENDERECO
(1, 'USUARIO_ENDERECO_CRIAR', 'Permite cadastrar endereço do usuário', NOW(), 1),
(1, 'USUARIO_ENDERECO_EDITAR', 'Permite editar endereço do usuário', NOW(), 1),
(1, 'USUARIO_ENDERECO_LISTAR', 'Permite consultar endereço do usuário', NOW(), 1),
(1, 'USUARIO_ENDERECO_EXCLUIR', 'Permite remover endereço do usuário', NOW(), 1),

-- STATUS
(1, 'USUARIO_ATIVAR', 'Permite ativar usuário', NOW(), 1),
(1, 'USUARIO_DESATIVAR', 'Permite desativar usuário', NOW(), 1);