-- ============================================================
--  INSERT INICIAIS: MS-Empresa
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
(1, 'EMPRESA_CONFIGURACAO_CRIAR', 'Permite criar configuração de empresa', NOW(), 1),
(1, 'EMPRESA_CONFIGURACAO_EDITAR', 'Permite atualizar configuração de empresa', NOW(), 1),
(1, 'EMPRESA_CONFIGURACAO_LISTAR', 'Permite listar configuração de empresa', NOW(), 1),
(1, 'EMPRESA_CONFIGURACAO_EXCLUIR', 'Permite remover configuração de empresa', NOW(), 1),

(1, 'EMPRESA_CONTATO_CRIAR', 'Permite criar contato de empresa', NOW(), 1),
(1, 'EMPRESA_CONTATO_EDITAR', 'Permite atualizar contato de empresa', NOW(), 1),
(1, 'EMPRESA_CONTATO_LISTAR', 'Permite listar contato de empresa', NOW(), 1),
(1, 'EMPRESA_CONTATO_EXCLUIR', 'Permite remover contato de empresa', NOW(), 1),

(1, 'EMPRESA_ENDERECO_CRIAR', 'Permite criar endereço de empresa', NOW(), 1),
(1, 'EMPRESA_ENDERECO_EDITAR', 'Permite atualizar endereço de empresa', NOW(), 1),
(1, 'EMPRESA_ENDERECO_LISTAR', 'Permite listar endereço de empresa', NOW(), 1),
(1, 'EMPRESA_ENDERECO_EXCLUIR', 'Permite remover endereço de empresa', NOW(), 1),

(1, 'EMPRESA_PARAMETRO_CRIAR', 'Permite criar parâmetro de empresa', NOW(), 1),
(1, 'EMPRESA_PARAMETRO_EDITAR', 'Permite atualizar parâmetro de empresa', NOW(), 1),
(1, 'EMPRESA_PARAMETRO_LISTAR', 'Permite listar parâmetro de empresa', NOW(), 1),
(1, 'EMPRESA_PARAMETRO_EXCLUIR', 'Permite remover parâmetro de empresa', NOW(), 1),

(1, 'EMPRESA_USUARIO_CRIAR', 'Permite vincular usuário à empresa', NOW(), 1),
(1, 'EMPRESA_USUARIO_EDITAR', 'Permite atualizar vínculo de usuário da empresa', NOW(), 1),
(1, 'EMPRESA_USUARIO_LISTAR', 'Permite listar usuários da empresa', NOW(), 1),
(1, 'EMPRESA_USUARIO_EXCLUIR', 'Permite remover vínculo de usuário da empresa', NOW(), 1);
