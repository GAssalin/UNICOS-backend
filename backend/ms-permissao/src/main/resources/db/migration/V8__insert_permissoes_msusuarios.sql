-- ============================================================
--  INSERT INICIAIS: MS-Usuarios
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
(1, 'USUARIO_EMAIL_CRIAR', 'Permite criar solicitações de verificação de e-mail de usuário', NOW(), 1),
(1, 'USUARIO_EMAIL_EDITAR', 'Permite atualizar verificação de e-mail de usuário', NOW(), 1),
(1, 'USUARIO_EMAIL_LISTAR', 'Permite consultar verificação de e-mail de usuário', NOW(), 1),
(1, 'USUARIO_EMAIL_EXCLUIR', 'Permite remover verificação de e-mail de usuário', NOW(), 1);
