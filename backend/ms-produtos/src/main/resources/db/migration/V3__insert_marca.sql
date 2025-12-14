-- ============================================================
--  V3 - INSERT INICIAIS: marca
--  Insere 5 marcas para uso no catálogo, vinculadas a uma empresa
-- ============================================================

-- Inserindo marcas com empresa_id
INSERT INTO marca (id, nome, descricao, pais_origem, empresa_id) VALUES
(1, 'Samsung', 'Fabricante sul-coreana de eletrônicos e eletrodomésticos', 'Coreia do Sul', 1),
(2, 'Logitech', 'Fabricante de periféricos e acessórios de informática', 'Suíça', 1),
(3, 'Nike', 'Líder mundial em roupas esportivas e calçados', 'Estados Unidos', 1),
(4, 'Dell', 'Fabricante de computadores e monitores', 'Estados Unidos', 1),
(5, 'Tok&Stok', 'Especializada em móveis e decoração', 'Brasil', 1);
