-- ============================================================
--  INSERT INICIAIS: MS-Pessoas
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
-- PESSOA
(1, 'PESSOA_CRIAR', 'Permite criar pessoa', NOW(), 1),
(1, 'PESSOA_EDITAR', 'Permite atualizar pessoa', NOW(), 1),
(1, 'PESSOA_LISTAR', 'Permite listar pessoa', NOW(), 1),
(1, 'PESSOA_EXCLUIR', 'Permite remover pessoa', NOW(), 1),

-- CONTATO
(1, 'PESSOA_CONTATO_CRIAR', 'Permite criar contato de pessoa', NOW(), 1),
(1, 'PESSOA_CONTATO_EDITAR', 'Permite atualizar contato de pessoa', NOW(), 1),
(1, 'PESSOA_CONTATO_LISTAR', 'Permite listar contato de pessoa', NOW(), 1),
(1, 'PESSOA_CONTATO_EXCLUIR', 'Permite remover contato de pessoa', NOW(), 1),

-- DOCUMENTO
(1, 'PESSOA_DOCUMENTO_CRIAR', 'Permite criar documento de pessoa', NOW(), 1),
(1, 'PESSOA_DOCUMENTO_EDITAR', 'Permite atualizar documento de pessoa', NOW(), 1),
(1, 'PESSOA_DOCUMENTO_LISTAR', 'Permite listar documento de pessoa', NOW(), 1),
(1, 'PESSOA_DOCUMENTO_EXCLUIR', 'Permite remover documento de pessoa', NOW(), 1),

-- ENDERECO
(1, 'PESSOA_ENDERECO_CRIAR', 'Permite criar endereço de pessoa', NOW(), 1),
(1, 'PESSOA_ENDERECO_EDITAR', 'Permite atualizar endereço de pessoa', NOW(), 1),
(1, 'PESSOA_ENDERECO_LISTAR', 'Permite listar endereço de pessoa', NOW(), 1),
(1, 'PESSOA_ENDERECO_EXCLUIR', 'Permite remover endereço de pessoa', NOW(), 1),

-- PESSOA FISICA
(1, 'PESSOA_FISICA_CRIAR', 'Permite criar pessoa física', NOW(), 1),
(1, 'PESSOA_FISICA_EDITAR', 'Permite atualizar pessoa física', NOW(), 1),
(1, 'PESSOA_FISICA_LISTAR', 'Permite listar pessoa física', NOW(), 1),
(1, 'PESSOA_FISICA_EXCLUIR', 'Permite remover pessoa física', NOW(), 1),

-- PESSOA JURIDICA
(1, 'PESSOA_JURIDICA_CRIAR', 'Permite criar pessoa jurídica', NOW(), 1),
(1, 'PESSOA_JURIDICA_EDITAR', 'Permite atualizar pessoa jurídica', NOW(), 1),
(1, 'PESSOA_JURIDICA_LISTAR', 'Permite listar pessoa jurídica', NOW(), 1),
(1, 'PESSOA_JURIDICA_EXCLUIR', 'Permite remover pessoa jurídica', NOW(), 1),

-- MUNICIPIO
(1, 'PESSOA_MUNICIPIO_CRIAR', 'Permite criar município relacionado à pessoa', NOW(), 1),
(1, 'PESSOA_MUNICIPIO_EDITAR', 'Permite atualizar município relacionado à pessoa', NOW(), 1),
(1, 'PESSOA_MUNICIPIO_LISTAR', 'Permite listar município relacionado à pessoa', NOW(), 1),
(1, 'PESSOA_MUNICIPIO_EXCLUIR', 'Permite remover município relacionado à pessoa', NOW(), 1),

-- TIPO RELACAO PESSOA
(1, 'PESSOA_TIPO_RELACAO_PESSOA_CRIAR', 'Permite criar tipo de relação de pessoa', NOW(), 1),
(1, 'PESSOA_TIPO_RELACAO_PESSOA_EDITAR', 'Permite atualizar tipo de relação de pessoa', NOW(), 1),
(1, 'PESSOA_TIPO_RELACAO_PESSOA_LISTAR', 'Permite listar tipo de relação de pessoa', NOW(), 1),
(1, 'PESSOA_TIPO_RELACAO_PESSOA_EXCLUIR', 'Permite remover tipo de relação de pessoa', NOW(), 1);