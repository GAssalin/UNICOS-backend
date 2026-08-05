-- ============================================================
-- INSERT INICIAIS: MS-EMPRESA
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
-- EMPRESA
(1, 'EMPRESA_CRIAR', 'Permite criar empresa', NOW(), 1),
(1, 'EMPRESA_EDITAR', 'Permite atualizar empresa', NOW(), 1),
(1, 'EMPRESA_LISTAR', 'Permite listar empresa', NOW(), 1),
(1, 'EMPRESA_EXCLUIR', 'Permite remover empresa', NOW(), 1),

-- CONFIGURACOES
(1, 'EMPRESA_CONFIGURACAO_CRIAR', 'Permite criar configuracao da empresa', NOW(), 1),
(1, 'EMPRESA_CONFIGURACAO_EDITAR', 'Permite atualizar configuracao da empresa', NOW(), 1),
(1, 'EMPRESA_CONFIGURACAO_LISTAR', 'Permite listar configuracoes da empresa', NOW(), 1),
(1, 'EMPRESA_CONFIGURACAO_EXCLUIR', 'Permite remover configuracao da empresa', NOW(), 1),

-- CONTATOS
(1, 'EMPRESA_CONTATO_CRIAR', 'Permite criar contato da empresa', NOW(), 1),
(1, 'EMPRESA_CONTATO_EDITAR', 'Permite atualizar contato da empresa', NOW(), 1),
(1, 'EMPRESA_CONTATO_LISTAR', 'Permite listar contatos da empresa', NOW(), 1),
(1, 'EMPRESA_CONTATO_EXCLUIR', 'Permite remover contato da empresa', NOW(), 1),

-- ENDERECOS
(1, 'EMPRESA_ENDERECO_CRIAR', 'Permite criar endereco da empresa', NOW(), 1),
(1, 'EMPRESA_ENDERECO_EDITAR', 'Permite atualizar endereco da empresa', NOW(), 1),
(1, 'EMPRESA_ENDERECO_LISTAR', 'Permite listar enderecos da empresa', NOW(), 1),
(1, 'EMPRESA_ENDERECO_EXCLUIR', 'Permite remover endereco da empresa', NOW(), 1),

-- PARAMETROS
(1, 'EMPRESA_PARAMETRO_CRIAR', 'Permite criar parametro da empresa', NOW(), 1),
(1, 'EMPRESA_PARAMETRO_EDITAR', 'Permite atualizar parametro da empresa', NOW(), 1),
(1, 'EMPRESA_PARAMETRO_LISTAR', 'Permite listar parametros da empresa', NOW(), 1),
(1, 'EMPRESA_PARAMETRO_EXCLUIR', 'Permite remover parametro da empresa', NOW(), 1),

-- USUARIOS
(1, 'EMPRESA_USUARIO_CRIAR', 'Permite vincular usuario a empresa', NOW(), 1),
(1, 'EMPRESA_USUARIO_EDITAR', 'Permite atualizar perfil do usuario na empresa', NOW(), 1),
(1, 'EMPRESA_USUARIO_LISTAR', 'Permite listar usuarios da empresa', NOW(), 1),
(1, 'EMPRESA_USUARIO_EXCLUIR', 'Permite remover usuario da empresa', NOW(), 1);
