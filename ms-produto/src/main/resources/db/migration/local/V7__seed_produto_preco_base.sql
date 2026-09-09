-- =========================================================
-- SEED - PREÇO BASE DO PRODUTO (CUSTO / PREÇO / MARGEM)
-- =========================================================

INSERT INTO produto_preco_base (
    empresa_id,
    produto_id,
    custo_base,
    preco_venda_base,
    margem_base,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
)
SELECT
    1 AS empresa_id,
    p.id AS produto_id,

    -- custo_base
    CASE p.codigo
        WHEN 'SKU-0001' THEN 3.2000
        WHEN 'SKU-0002' THEN 21.5000
        WHEN 'SKU-0003' THEN 2800.0000
        WHEN 'SKU-0004' THEN 6.0000
        WHEN 'SKU-0005' THEN 1.2000
        WHEN 'DIG-0001' THEN 35.0000
        WHEN 'SRV-0001' THEN 0.0000
        WHEN 'SRV-0002' THEN 0.0000
        WHEN 'SUB-0001' THEN 0.0000
        ELSE NULL
    END AS custo_base,

    -- preco_venda_base
    CASE p.codigo
        WHEN 'SKU-0001' THEN 5.9900
        WHEN 'SKU-0002' THEN 28.9000
        WHEN 'SKU-0003' THEN 3499.9000
        WHEN 'SKU-0004' THEN 12.5000
        WHEN 'SKU-0005' THEN 2.5000
        WHEN 'DIG-0001' THEN 89.9000
        WHEN 'SRV-0001' THEN 120.0000
        WHEN 'SRV-0002' THEN 180.0000
        WHEN 'SUB-0001' THEN 250.0000
        ELSE NULL
    END AS preco_venda_base,

    -- margem_base (%)
    CASE p.codigo
        WHEN 'SKU-0001' THEN 40.00
        WHEN 'SKU-0002' THEN 25.00
        WHEN 'SKU-0003' THEN 25.00
        WHEN 'SKU-0004' THEN 52.00
        WHEN 'SKU-0005' THEN 52.00
        WHEN 'DIG-0001' THEN 60.00
        WHEN 'SRV-0001' THEN 100.00
        WHEN 'SRV-0002' THEN 100.00
        WHEN 'SUB-0001' THEN 100.00
        ELSE NULL
    END AS margem_base,

    1 AS criado_por,
    NOW() AS criado_em,
    NULL AS atualizado_por,
    NULL AS atualizado_em,
    TRUE AS ativo
FROM produto p
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0001','SKU-0002','SKU-0003','SKU-0004','SKU-0005','DIG-0001','SRV-0001','SRV-0002','SUB-0001')

ON DUPLICATE KEY UPDATE
custo_base = VALUES(custo_base),
preco_venda_base = VALUES(preco_venda_base),
margem_base = VALUES(margem_base),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();
