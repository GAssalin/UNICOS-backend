-- ============================================================
--  V5 - INSERT INICIAIS: produto
--  Insere 5 produtos completos, com dados básicos,
--  tributação, estoque e preço padrão.
-- ============================================================

INSERT INTO produto (
    id,
    dados_basicos_nome,
    dados_basicos_descricao,
    dados_basicos_sku,
    dados_basicos_tipo,
    dados_basicos_classificacao,

    tributacao_ncm,
    tributacao_cest,
    tributacao_origem,
    tributacao_csosn,
    tributacao_cst,
    tributacao_cfop,
    tributacao_aliquota_icms,
    tributacao_aliquota_pis,
    tributacao_aliquota_cofins,

    estoqueconfig_controla_estoque,
    estoqueconfig_estoque_min,
    estoqueconfig_estoque_max,
    estoqueconfig_estoque_atual,

    precoatual_preco,
    precoatual_preco_promocional,
    precoatual_data_inicio_promocao,
    precoatual_data_fim_promocao,

    ativo,
    categoria_id,
    marca_id
)
VALUES
-- 1) Smartphone Samsung
(1,
 'Smartphone Galaxy A54',
 'Smartphone Samsung com 8GB RAM e 256GB armazenamento.',
 'SM-A54-256-8',
 'Produto',
 'Eletrônicos',

 '85171231', '2800900', '0', '102', '060', '5102',
 18.00, 1.65, 7.60,

 TRUE, 5, 50, 20,

 2499.90, NULL, NULL, NULL,
 TRUE, 1, 1),

-- 2) Mouse Logitech sem fio
(2,
 'Mouse Sem Fio Logitech M280',
 'Mouse ergonômico de alta precisão com receptor USB.',
 'LT-M280',
 'Produto',
 'Informática',

 '84716052', '2302300', '0', '102', '060', '5102',
 18.00, 1.65, 7.60,

 TRUE, 10, 200, 85,

 129.90, NULL, NULL, NULL,
 TRUE, 2, 2),

-- 3) Tênis Nike Revolution
(3,
 'Tênis Nike Revolution 6',
 'Calçado esportivo para caminhadas e treinos leves.',
 'NK-REV6',
 'Produto',
 'Vestuário',

 '64041100', '2000100', '0', '102', '060', '5102',
 12.00, 0.65, 3.00,

 TRUE, 3, 100, 42,

 349.90, NULL, NULL, NULL,
 TRUE, 3, 3),

-- 4) Monitor Dell 24"
(4,
 'Monitor Dell 24 Polegadas',
 'Monitor LED Full HD com borda fina.',
 'DL-24-FHD',
 'Produto',
 'Eletrônicos',

 '85285210', '2102400', '0', '102', '060', '5102',
 18.00, 1.65, 7.60,

 TRUE, 2, 40, 12,

 899.90, NULL, NULL, NULL,
 TRUE, 1, 4),

-- 5) Poltrona Tok&Stok
(5,
 'Poltrona Conforto Premium',
 'Poltrona estofada para sala de estar.',
 'TS-POLTR-PRM',
 'Produto',
 'Móveis',

 '94016100', '3000900', '0', '102', '060', '5102',
 12.00, 0.65, 3.00,

 FALSE, NULL, NULL, NULL,

 1799.90, NULL, NULL, NULL,
 TRUE, 5, 5);
