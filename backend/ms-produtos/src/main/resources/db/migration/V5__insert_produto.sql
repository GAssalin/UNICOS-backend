-- ============================================================
--  V5 - INSERT INICIAIS: produto
--  Insere produtos de exemplo no catálogo
--  Arquitetura: MULTI-TENANT (empresa_id)
-- ============================================================

INSERT INTO produto (
    id,
    empresa_id,

    dados_basicos_nome,
    dados_basicos_descricao,
    dados_basicos_sku,
    dados_basicos_tipo,
    dados_basicos_classificacao,

    tributacao_ncm,
    tributacao_cest,
    tributacao_situacao,

    preco_custo,
    preco_venda,
    preco_minimo,
    margem_padrao,

    ativo,
    categoria_id,
    marca_id
)
VALUES

-- 1) Smartphone Samsung
(1, 1,
 'Smartphone Galaxy A54',
 'Smartphone Samsung com 8GB RAM e 256GB armazenamento.',
 'SM-A54-256-8',
 'MERCADORIA_PARA_REVENDA',
 'POPULAR',

 '85171231', '2800900', 'TRIBUTADO',

 1800.00, 2499.90, 1999.90, 25.00,
 TRUE, 1, 1),

-- 2) Mouse Logitech
(2, 1,
 'Mouse Sem Fio Logitech M280',
 'Mouse ergonômico de alta precisão com receptor USB.',
 'LT-M280',
 'MERCADORIA_PARA_REVENDA',
 'INDUSTRIAL',

 '84716052', '2302300', 'TRIBUTADO',

 60.00, 129.90, 89.90, 30.00,
 TRUE, 2, 2),

-- 3) Tênis Nike
(3, 1,
 'Tênis Nike Revolution 6',
 'Calçado esportivo para caminhadas e treinos leves.',
 'NK-REV6',
 'MERCADORIA_PARA_REVENDA',
 'LUXO',

 '64041100', '2000100', 'TRIBUTADO',

 200.00, 349.90, 299.90, 20.00,
 TRUE, 3, 3),

-- 4) Monitor Dell
(4, 1,
 'Monitor Dell 24 Polegadas',
 'Monitor LED Full HD com borda fina.',
 'DL-24-FHD',
 'MERCADORIA_PARA_REVENDA',
 'GENERICO',

 '85285210', '2102400', 'TRIBUTADO',

 600.00, 899.90, 799.90, 18.00,
 TRUE, 1, 4),

-- 5) Poltrona Tok&Stok
(5, 1,
 'Poltrona Conforto Premium',
 'Poltrona estofada para sala de estar.',
 'TS-POLTR-PRM',
 'MERCADORIA_PARA_REVENDA',
 'PREMIUM',

 '94016100', '3000900', 'TRIBUTADO',

 1200.00, 1799.90, 1599.90, 22.00,
 TRUE, 5, 5);
