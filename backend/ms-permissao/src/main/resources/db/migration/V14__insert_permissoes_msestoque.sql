-- ============================================================
-- INSERT INICIAIS: MS-ESTOQUE
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
-- ESTOQUE
(1, 'ESTOQUE_CRIAR', 'Permite criar estoque', NOW(), 1),
(1, 'ESTOQUE_EDITAR', 'Permite atualizar estoque', NOW(), 1),
(1, 'ESTOQUE_LISTAR', 'Permite consultar e listar estoques', NOW(), 1),
(1, 'ESTOQUE_EXCLUIR', 'Permite remover estoque', NOW(), 1),

-- RESPONSAVEL DE ESTOQUE
(1, 'ESTOQUE_RESPONSAVEL_CRIAR', 'Permite criar vinculo de responsavel por estoque', NOW(), 1),
(1, 'ESTOQUE_RESPONSAVEL_EDITAR', 'Permite atualizar vinculo de responsavel por estoque', NOW(), 1),
(1, 'ESTOQUE_RESPONSAVEL_LISTAR', 'Permite consultar e listar responsaveis por estoque', NOW(), 1),
(1, 'ESTOQUE_RESPONSAVEL_EXCLUIR', 'Permite remover vinculo de responsavel por estoque', NOW(), 1),

-- VINCULO ESTOQUE x FILIAL
(1, 'ESTOQUE_VINCULO_FILIAL_CRIAR', 'Permite criar vinculo entre estoque e filial', NOW(), 1),
(1, 'ESTOQUE_VINCULO_FILIAL_EDITAR', 'Permite atualizar vinculo entre estoque e filial', NOW(), 1),
(1, 'ESTOQUE_VINCULO_FILIAL_LISTAR', 'Permite consultar e listar vinculos entre estoque e filial', NOW(), 1),
(1, 'ESTOQUE_VINCULO_FILIAL_EXCLUIR', 'Permite remover vinculo entre estoque e filial', NOW(), 1);
