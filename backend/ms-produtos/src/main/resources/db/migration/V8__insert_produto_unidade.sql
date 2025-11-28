-- ============================================================
--  V8 - INSERT INICIAIS: produto_unidade
--  Insere 5 registros de unidades por produto,
--  respeitando a FK de produto e unidade_medida.
-- ============================================================

INSERT INTO produto_unidade (id, produto_id, unidade_medida_id, quantidade_padrao, fator_conversao)
VALUES
-- Produto 1 – Smartphone (unidade)
(1, 1, 1, 1, 1.0),

-- Produto 2 – Mouse Logitech (unidade)
(2, 2, 1, 1, 1.0),

-- Produto 3 – Tênis Nike (unidade)
(3, 3, 1, 1, 1.0),

-- Produto 4 – Monitor Dell (unidade)
(4, 4, 1, 1, 1.0),

-- Produto 5 – Poltrona Tok&Stok (unidade)
(5, 5, 1, 1, 1.0);
