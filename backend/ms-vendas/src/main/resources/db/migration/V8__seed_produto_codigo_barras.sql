-- =========================================================
-- SEED - CÓDIGOS DE BARRAS DO PRODUTO (PRINCIPAL + ALTERNATIVOS)
-- =========================================================

-- PRINCIPAIS
INSERT INTO produto_codigo_barras (
    empresa_id,
    produto_id,
    codigo_barras,
    principal,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
)
SELECT
    1 AS empresa_id,
    p.id AS produto_id,
    CASE p.codigo
        WHEN 'SKU-0001' THEN '7894900011517'
        WHEN 'SKU-0002' THEN '7896006716110'
        WHEN 'SKU-0003' THEN '7891234567890'
        WHEN 'SKU-0004' THEN '7899876543210'
        WHEN 'SKU-0005' THEN '7891000100103'
        ELSE NULL
    END AS codigo_barras,
    TRUE AS principal,
    1 AS criado_por,
    NOW() AS criado_em,
    NULL AS atualizado_por,
    NULL AS atualizado_em,
    TRUE AS ativo
FROM produto p
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0001','SKU-0002','SKU-0003','SKU-0004','SKU-0005')

ON DUPLICATE KEY UPDATE
produto_id = VALUES(produto_id),
principal = VALUES(principal),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();



-- ALTERNATIVOS (exemplos)
-- SKU-0001: outro EAN (lote/embalagem)
INSERT INTO produto_codigo_barras (
    empresa_id,
    produto_id,
    codigo_barras,
    principal,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
)
SELECT
    1,
    p.id,
    '7894900011518',
    FALSE,
    1,
    NOW(),
    NULL,
    NULL,
    TRUE
FROM produto p
WHERE p.empresa_id = 1
  AND p.codigo = 'SKU-0001'
ON DUPLICATE KEY UPDATE
produto_id = VALUES(produto_id),
principal = VALUES(principal),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();


-- SKU-0004: código alternativo (fornecedor)
INSERT INTO produto_codigo_barras (
    empresa_id,
    produto_id,
    codigo_barras,
    principal,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
)
SELECT
    1,
    p.id,
    '7899876543211',
    FALSE,
    1,
    NOW(),
    NULL,
    NULL,
    TRUE
FROM produto p
WHERE p.empresa_id = 1
  AND p.codigo = 'SKU-0004'
ON DUPLICATE KEY UPDATE
produto_id = VALUES(produto_id),
principal = VALUES(principal),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();
