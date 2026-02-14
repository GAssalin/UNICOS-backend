-- ============================================================
--  INSERT INICIAIS: MS-Produto
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
(1, 'PRODUTO_CRIAR', 'Permite criar produto', NOW(), 1),
(1, 'PRODUTO_EDITAR', 'Permite atualizar produto', NOW(), 1),
(1, 'PRODUTO_LISTAR', 'Permite listar produto', NOW(), 1),
(1, 'PRODUTO_EXCLUIR', 'Permite remover produto', NOW(), 1),

(1, 'PRODUTO_ATRIBUTOS_CRIAR', 'Permite criar atributo de produto', NOW(), 1),
(1, 'PRODUTO_ATRIBUTOS_EDITAR', 'Permite atualizar atributo de produto', NOW(), 1),
(1, 'PRODUTO_ATRIBUTOS_LISTAR', 'Permite listar atributo de produto', NOW(), 1),
(1, 'PRODUTO_ATRIBUTOS_EXCLUIR', 'Permite remover atributo de produto', NOW(), 1),

(1, 'PRODUTO_ATRIBUTOS_VALORES_CRIAR', 'Permite criar valor de atributo de produto', NOW(), 1),
(1, 'PRODUTO_ATRIBUTOS_VALORES_EDITAR', 'Permite atualizar valor de atributo de produto', NOW(), 1),
(1, 'PRODUTO_ATRIBUTOS_VALORES_LISTAR', 'Permite listar valor de atributo de produto', NOW(), 1),
(1, 'PRODUTO_ATRIBUTOS_VALORES_EXCLUIR', 'Permite remover valor de atributo de produto', NOW(), 1),

(1, 'PRODUTO_CATEGORIA_CRIAR', 'Permite criar categoria de produto', NOW(), 1),
(1, 'PRODUTO_CATEGORIA_EDITAR', 'Permite atualizar categoria de produto', NOW(), 1),
(1, 'PRODUTO_CATEGORIA_LISTAR', 'Permite listar categoria de produto', NOW(), 1),
(1, 'PRODUTO_CATEGORIA_EXCLUIR', 'Permite remover categoria de produto', NOW(), 1),

(1, 'PRODUTO_CODIGO_BARRAS_CRIAR', 'Permite criar código de barras de produto', NOW(), 1),
(1, 'PRODUTO_CODIGO_BARRAS_EDITAR', 'Permite atualizar código de barras de produto', NOW(), 1),
(1, 'PRODUTO_CODIGO_BARRAS_LISTAR', 'Permite listar código de barras de produto', NOW(), 1),
(1, 'PRODUTO_CODIGO_BARRAS_EXCLUIR', 'Permite remover código de barras de produto', NOW(), 1),

(1, 'PRODUTO_IMAGENS_CRIAR', 'Permite criar imagem de produto', NOW(), 1),
(1, 'PRODUTO_IMAGENS_EDITAR', 'Permite atualizar imagem de produto', NOW(), 1),
(1, 'PRODUTO_IMAGENS_LISTAR', 'Permite listar imagem de produto', NOW(), 1),
(1, 'PRODUTO_IMAGENS_EXCLUIR', 'Permite remover imagem de produto', NOW(), 1),

(1, 'PRODUTO_MARCAS_PRODUTO_CRIAR', 'Permite criar marca de produto', NOW(), 1),
(1, 'PRODUTO_MARCAS_PRODUTO_EDITAR', 'Permite atualizar marca de produto', NOW(), 1),
(1, 'PRODUTO_MARCAS_PRODUTO_LISTAR', 'Permite listar marca de produto', NOW(), 1),
(1, 'PRODUTO_MARCAS_PRODUTO_EXCLUIR', 'Permite remover marca de produto', NOW(), 1),

(1, 'PRODUTO_PRECO_BASE_CRIAR', 'Permite criar preço base de produto', NOW(), 1),
(1, 'PRODUTO_PRECO_BASE_EDITAR', 'Permite atualizar preço base de produto', NOW(), 1),
(1, 'PRODUTO_PRECO_BASE_LISTAR', 'Permite listar preço base de produto', NOW(), 1),
(1, 'PRODUTO_PRECO_BASE_EXCLUIR', 'Permite remover preço base de produto', NOW(), 1),

(1, 'PRODUTO_TIPOS_CRIAR', 'Permite criar tipo de produto', NOW(), 1),
(1, 'PRODUTO_TIPOS_EDITAR', 'Permite atualizar tipo de produto', NOW(), 1),
(1, 'PRODUTO_TIPOS_LISTAR', 'Permite listar tipo de produto', NOW(), 1),
(1, 'PRODUTO_TIPOS_EXCLUIR', 'Permite remover tipo de produto', NOW(), 1),

(1, 'PRODUTO_UNIDADE_MEDIDA_CRIAR', 'Permite criar unidade de medida de produto', NOW(), 1),
(1, 'PRODUTO_UNIDADE_MEDIDA_EDITAR', 'Permite atualizar unidade de medida de produto', NOW(), 1),
(1, 'PRODUTO_UNIDADE_MEDIDA_LISTAR', 'Permite listar unidade de medida de produto', NOW(), 1),
(1, 'PRODUTO_UNIDADE_MEDIDA_EXCLUIR', 'Permite remover unidade de medida de produto', NOW(), 1);
