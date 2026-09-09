-- ============================================================
-- INSERT INICIAIS: MS-CLIENTE
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
-- CLIENTES
(1, 'CLIENTE_CRIAR', 'Permite criar cliente', NOW(), 1),
(1, 'CLIENTE_EDITAR', 'Permite atualizar cliente', NOW(), 1),
(1, 'CLIENTE_LISTAR', 'Permite consultar e listar clientes', NOW(), 1),
(1, 'CLIENTE_EXCLUIR', 'Permite remover cliente', NOW(), 1),

-- CATEGORIAS DE CLIENTE
(1, 'CLIENTE_CATEGORIA_CRIAR', 'Permite criar categoria de cliente', NOW(), 1),
(1, 'CLIENTE_CATEGORIA_EDITAR', 'Permite atualizar categoria de cliente', NOW(), 1),
(1, 'CLIENTE_CATEGORIA_LISTAR', 'Permite consultar e listar categorias de cliente', NOW(), 1),
(1, 'CLIENTE_CATEGORIA_EXCLUIR', 'Permite remover categoria de cliente', NOW(), 1),

-- OBSERVACOES DE CLIENTE
(1, 'CLIENTE_OBSERVACAO_CRIAR', 'Permite criar observacao de cliente', NOW(), 1),
(1, 'CLIENTE_OBSERVACAO_EDITAR', 'Permite atualizar observacao de cliente', NOW(), 1),
(1, 'CLIENTE_OBSERVACAO_LISTAR', 'Permite consultar e listar observacoes de cliente', NOW(), 1),
(1, 'CLIENTE_OBSERVACAO_EXCLUIR', 'Permite remover observacao de cliente', NOW(), 1);
