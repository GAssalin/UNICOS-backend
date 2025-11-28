-- ============================================================
--  V2 - INSERT INICIAIS: categoria
--  Insere 5 categorias de exemplo, incluindo hierarquia
-- ============================================================

INSERT INTO categoria (id, nome, descricao, categoria_pai_id, ativo) VALUES
(1, 'Eletrônicos', 'Produtos eletrônicos em geral', NULL, TRUE),
(2, 'Informática', 'Equipamentos e acessórios de informática', 1, TRUE),
(3, 'Vestuário', 'Roupas diversas', NULL, TRUE),
(4, 'Calçados', 'Calçados masculinos e femininos', 3, TRUE),
(5, 'Móveis', 'Produtos para mobília residencial', NULL, TRUE);
