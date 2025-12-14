-- ============================================================
--  V9 - INSERT INICIAIS: produto_variacao
--  Insere variações de produtos
--  Arquitetura: MULTI-TENANT (empresa_id)
-- ============================================================

INSERT INTO produto_variacao (
    id,
    empresa_id,
    produto_id,
    nome,
    sku,
    preco,
    codigo_barras,
    ativo,
    cor,
    tamanho,
    material
)
VALUES

-- 1) Smartphone – Cor Azul
(1, 1, 1, 'Galaxy A54 Azul', 'SM-A54-AZUL', 2549.90, '7890000000011', TRUE, 'Azul', NULL, NULL),

-- 2) Mouse Logitech – Cor Vermelho
(2, 1, 2, 'Mouse Logitech M280 Vermelho', 'LT-M280-VM', 139.90, '7890000000012', TRUE, 'Vermelho', NULL, NULL),

-- 3) Tênis Nike – Tamanho 40
(3, 1, 3, 'Nike Revolution 6 – Tam 40', 'NK-REV6-40', 329.90, '7890000000013', TRUE, NULL, '40', 'Tecido'),

-- 4) Monitor Dell – Versão com HDMI Duplo
(4, 1, 4, 'Monitor Dell 24 – HDMI Duplo', 'DL-24-HDMI2', 949.90, '7890000000014', TRUE, NULL, NULL, 'Metal e Plástico'),

-- 5) Poltrona Tok&Stok – Cor Bege
(5, 1, 5, 'Poltrona Premium Bege', 'TS-POLTR-BG', 1849.90, '7890000000015', TRUE, 'Bege', NULL, 'Tecido Sintético');
