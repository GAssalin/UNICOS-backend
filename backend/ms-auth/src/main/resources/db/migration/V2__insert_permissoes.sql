-- ============================================================
--  V2 - INSERT INICIAIS: Permissões
-- ============================================================

INSERT INTO permissao (id, nome, descricao) VALUES

-- ============================================================
--  MS-AUTH
-- ============================================================

(1, 'USUARIO_CRIAR', 'Permite criar novos usuários'),
(2, 'USUARIO_EDITAR', 'Permite atualizar informações de usuários'),
(3, 'USUARIO_LISTAR', 'Permite visualizar usuários'),
(4, 'USUARIO_EXCLUIR', 'Permite excluir um usuário'),

(5, 'ROLE_CRIAR', 'Permite criar papéis'),
(6, 'ROLE_EDITAR', 'Permite alterar papéis'),
(7, 'ROLE_LISTAR', 'Permite ver a lista de papéis'),
(8, 'ROLE_EXCLUIR', 'Permite remover papéis'),

(9,  'PERMISSAO_CRIAR', 'Permite gestão completa de permissões'),
(10, 'PERMISSAO_EDITAR', 'Permite atualizar permissões'),
(11, 'PERMISSAO_EXCLUIR', 'Permite excluir permissões'),
(12, 'PERMISSAO_LISTAR', 'Permite listar permissões disponíveis'),

-- ============================================================
--  MS-PESSOAS
-- ============================================================

(13, 'CONTATO_CRIAR', 'Permite criar contatos de pessoas'),
(14, 'CONTATO_EDITAR', 'Permite editar contatos de pessoas'),
(15, 'CONTATO_EXCLUIR', 'Permite excluir contatos de pessoas'),
(16, 'CONTATO_LISTAR', 'Permite listar contatos de pessoas'),

(17, 'DOCUMENTO_CRIAR', 'Permite cadastrar documentos'),
(18, 'DOCUMENTO_EDITAR', 'Permite editar documentos'),
(19, 'DOCUMENTO_EXCLUIR', 'Permite excluir documentos'),
(20, 'DOCUMENTO_LISTAR', 'Permite listar documentos'),

(21, 'ENDERECO_CRIAR', 'Permite cadastrar endereços'),
(22, 'ENDERECO_EDITAR', 'Permite atualizar endereços'),
(23, 'ENDERECO_EXCLUIR', 'Permite excluir endereços'),
(24, 'ENDERECO_LISTAR', 'Permite visualizar endereços'),

(25, 'MUNICIPIO_CRIAR', 'Permite cadastrar municípios'),
(26, 'MUNICIPIO_EDITAR', 'Permite alterar municípios'),
(27, 'MUNICIPIO_EXCLUIR', 'Permite excluir municípios'),
(28, 'MUNICIPIO_LISTAR', 'Permite listar municípios'),

(29, 'PESSOA_LISTAR', 'Permite listar qualquer tipo de pessoa'),

(30, 'PESSOA_FISICA_CRIAR', 'Permite criar pessoas físicas'),
(31, 'PESSOA_FISICA_EDITAR', 'Permite atualizar pessoas físicas'),
(32, 'PESSOA_FISICA_EXCLUIR', 'Permite excluir pessoas físicas'),
(33, 'PESSOA_FISICA_LISTAR', 'Permite listar pessoas físicas'),

(34, 'PESSOA_JURIDICA_CRIAR', 'Permite criar pessoas jurídicas'),
(35, 'PESSOA_JURIDICA_EDITAR', 'Permite atualizar pessoas jurídicas'),
(36, 'PESSOA_JURIDICA_EXCLUIR', 'Permite excluir pessoas jurídicas'),
(37, 'PESSOA_JURIDICA_LISTAR', 'Permite listar pessoas jurídicas'),

(38, 'PESSOA_RELACAO_CRIAR', 'Permite criar relações entre pessoas'),
(39, 'PESSOA_RELACAO_EDITAR', 'Permite editar relações entre pessoas'),
(40, 'PESSOA_RELACAO_EXCLUIR', 'Permite excluir relações entre pessoas'),
(41, 'PESSOA_RELACAO_LISTAR', 'Permite listar relações entre pessoas'),

(42, 'TIPO_RELACAO_PESSOA_CRIAR', 'Permite criar tipos de relação entre pessoas'),
(43, 'TIPO_RELACAO_PESSOA_EDITAR', 'Permite editar tipos de relação entre pessoas'),
(44, 'TIPO_RELACAO_PESSOA_EXCLUIR', 'Permite excluir tipos de relação entre pessoas'),
(45, 'TIPO_RELACAO_PESSOA_LISTAR', 'Permite listar tipos de relação entre pessoas'),

-- ============================================================
--  MS-PRODUTOS
-- ============================================================

(46, 'ATRIBUTO_PERSONALIZADO_CRIAR', 'Permite criar atributos personalizados'),
(47, 'ATRIBUTO_PERSONALIZADO_EDITAR', 'Permite editar atributos personalizados'),
(48, 'ATRIBUTO_PERSONALIZADO_EXCLUIR', 'Permite excluir atributos personalizados'),
(49, 'ATRIBUTO_PERSONALIZADO_LISTAR', 'Permite listar atributos personalizados'),

(50, 'CATEGORIA_CRIAR', 'Permite criar categorias de produto'),
(51, 'CATEGORIA_EDITAR', 'Permite atualizar categorias de produto'),
(52, 'CATEGORIA_EXCLUIR', 'Permite excluir categorias de produto'),
(53, 'CATEGORIA_LISTAR', 'Permite listar categorias de produto'),

(54, 'FORNECEDOR_PRODUTO_CRIAR', 'Permite vincular fornecedores a produtos'),
(55, 'FORNECEDOR_PRODUTO_EDITAR', 'Permite editar vínculos fornecedor-produto'),
(56, 'FORNECEDOR_PRODUTO_EXCLUIR', 'Permite excluir vínculos fornecedor-produto'),
(57, 'FORNECEDOR_PRODUTO_LISTAR', 'Permite listar vínculos fornecedor-produto'),

(58, 'HISTORICO_PRECO_CRIAR', 'Permite registrar histórico de alterações de preço'),
(59, 'HISTORICO_PRECO_EDITAR', 'Permite editar registros do histórico de preço'),
(60, 'HISTORICO_PRECO_EXCLUIR', 'Permite excluir registros do histórico de preço'),
(61, 'HISTORICO_PRECO_LISTAR', 'Permite listar histórico de preço'),

(62, 'MARCA_CRIAR', 'Permite criar marcas'),
(63, 'MARCA_EDITAR', 'Permite atualizar marcas'),
(64, 'MARCA_EXCLUIR', 'Permite excluir marcas'),
(65, 'MARCA_LISTAR', 'Permite listar marcas'),

(66, 'PRODUTO_CRIAR', 'Permite criar produtos'),
(67, 'PRODUTO_EDITAR', 'Permite editar produtos'),
(68, 'PRODUTO_EXCLUIR', 'Permite excluir produtos'),
(69, 'PRODUTO_LISTAR', 'Permite listar produtos'),

(70, 'UNIDADE_MEDIDA_CRIAR', 'Permite criar unidades de medida'),
(71, 'UNIDADE_MEDIDA_EDITAR', 'Permite atualizar unidades de medida'),
(72, 'UNIDADE_MEDIDA_EXCLUIR', 'Permite excluir unidades de medida'),
(73, 'UNIDADE_MEDIDA_LISTAR', 'Permite listar unidades de medida');
