-- ============================================================
--  V10 - INSERT INICIAIS: fornecedor_produto
--  Insere 5 vínculos de fornecedores com produtos
-- ============================================================

INSERT INTO fornecedor_produto (
    id,
    fornecedor_id,
    codigo_fornecedor,
    produto_id,
    preco_custo,
    prazo_entrega_dias,
    data_criacao,
    data_atualizacao
)
VALUES
-- 1) Fornecedor 101 fornece o Smartphone
(1, 101, 'SM-A54-FRN', 1, 1899.90, 7, NOW(), NOW()),

-- 2) Fornecedor 102 fornece o Mouse Logitech
(2, 102, 'LT-M280-FRN', 2, 79.90, 5, NOW(), NOW()),

-- 3) Fornecedor 103 fornece o Tênis Nike
(3, 103, 'NK-REV6-FRN', 3, 199.90, 10, NOW(), NOW()),

-- 4) Fornecedor 104 fornece o Monitor Dell
(4, 104, 'DL-24-FRN', 4, 699.90, 8, NOW(), NOW()),

-- 5) Fornecedor 105 fornece a Poltrona
(5, 105, 'TS-POLTR-FRN', 5, 1299.90, 12, NOW(), NOW());
