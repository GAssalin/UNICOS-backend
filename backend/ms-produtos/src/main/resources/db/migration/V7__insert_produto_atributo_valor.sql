-- ============================================================
--  V7 - INSERT INICIAIS: produto_atributo_valor
--  Insere 5 valores de atributos vinculados aos produtos.
-- ============================================================

INSERT INTO produto_atributo_valor (id, produto_id, atributo_personalizado_id, valor)
VALUES
-- Produto 1 (Smartphone) – atributo "Cor"
(1, 1, 1, 'Preto'),

-- Produto 2 (Mouse Logitech) – atributo "Conectividade"
(2, 2, 2, 'Wireless'),

-- Produto 3 (Tênis Nike) – atributo "Tamanho"
(3, 3, 3, '42'),

-- Produto 4 (Monitor Dell) – atributo "Material"
(4, 4, 4, 'Plástico / Metal'),

-- Produto 5 (Poltrona Tok&Stok) – atributo "Cor do Tecido"
(5, 5, 5, 'Cinza Chumbo');
