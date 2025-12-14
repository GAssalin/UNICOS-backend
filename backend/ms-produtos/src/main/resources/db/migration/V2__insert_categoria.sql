-- ============================================================
--  V2 - INSERT INICIAIS: categoria
--  Insere 5 categorias de exemplo, incluindo hierarquia e vinculação a empresa
-- ============================================================

-- Inserindo categorias com empresa_id
INSERT INTO categoria (id, nome, descricao, categoria_pai_id, ativo, empresa_id) VALUES
(1, 'Eletrônicos', 'Produtos eletrônicos em geral', NULL, TRUE, 1),
(2, 'Informática', 'Equipamentos e acessórios de informática', 1, TRUE, 1),
(3, 'Vestuário', 'Roupas diversas', NULL, TRUE, 1),
(4, 'Calçados', 'Calçados masculinos e femininos', 3, TRUE, 1),
(5, 'Móveis', 'Produtos para mobília residencial', NULL, TRUE, 1);
