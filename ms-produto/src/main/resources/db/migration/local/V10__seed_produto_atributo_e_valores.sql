-- =========================================================
-- SEED - ATRIBUTOS DO PRODUTO + VALORES POR PRODUTO
-- =========================================================
-- empresa_id = 1
-- criado_por = 1
-- ativo = true
-- =========================================================


-- ---------------------------------------------------------
-- 1) ATRIBUTOS (produto_atributo)
-- ---------------------------------------------------------
INSERT INTO produto_atributo (
    empresa_id,
    nome,
    descricao,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 'Cor', 'Cor do produto', 1, NOW(), NULL, NULL, TRUE),
(1, 'Voltagem', 'Voltagem (110V/220V)', 1, NOW(), NULL, NULL, TRUE),
(1, 'Tamanho', 'Tamanho (P/M/G)', 1, NOW(), NULL, NULL, TRUE),
(1, 'Material', 'Material predominante', 1, NOW(), NULL, NULL, TRUE)
ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
ativo = VALUES(ativo);


-- ---------------------------------------------------------
-- 2) VALORES POR PRODUTO (produto_atributo_valor)
--    Regras:
--    - UK: (empresa_id, produto_id, atributo_id)
--    - então usamos INSERT SELECT + ON DUPLICATE KEY UPDATE
-- ---------------------------------------------------------

-- SKU-0003 (Notebook) -> Cor: Preto
INSERT INTO produto_atributo_valor (
    empresa_id, produto_id, atributo_id, valor,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1,
    p.id,
    a.id,
    'Preto',
    1, NOW(), NULL, NULL, TRUE
FROM produto p
JOIN produto_atributo a ON a.empresa_id = 1 AND a.nome = 'Cor'
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0003'
ON DUPLICATE KEY UPDATE
valor = VALUES(valor),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();

-- SKU-0003 (Notebook) -> Voltagem: 110V/220V (bivolt)
INSERT INTO produto_atributo_valor (
    empresa_id, produto_id, atributo_id, valor,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1,
    p.id,
    a.id,
    'Bivolt (110V/220V)',
    1, NOW(), NULL, NULL, TRUE
FROM produto p
JOIN produto_atributo a ON a.empresa_id = 1 AND a.nome = 'Voltagem'
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0003'
ON DUPLICATE KEY UPDATE
valor = VALUES(valor),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();


-- SKU-0004 (Cabo CAT6) -> Material: PVC
INSERT INTO produto_atributo_valor (
    empresa_id, produto_id, atributo_id, valor,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1,
    p.id,
    a.id,
    'PVC',
    1, NOW(), NULL, NULL, TRUE
FROM produto p
JOIN produto_atributo a ON a.empresa_id = 1 AND a.nome = 'Material'
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0004'
ON DUPLICATE KEY UPDATE
valor = VALUES(valor),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();


-- SKU-0001 (Coca-Cola lata) -> Tamanho: 350ml
INSERT INTO produto_atributo_valor (
    empresa_id, produto_id, atributo_id, valor,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1,
    p.id,
    a.id,
    '350ml',
    1, NOW(), NULL, NULL, TRUE
FROM produto p
JOIN produto_atributo a ON a.empresa_id = 1 AND a.nome = 'Tamanho'
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0001'
ON DUPLICATE KEY UPDATE
valor = VALUES(valor),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();


-- SKU-0005 (Água 500ml) -> Tamanho: 500ml
INSERT INTO produto_atributo_valor (
    empresa_id, produto_id, atributo_id, valor,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1,
    p.id,
    a.id,
    '500ml',
    1, NOW(), NULL, NULL, TRUE
FROM produto p
JOIN produto_atributo a ON a.empresa_id = 1 AND a.nome = 'Tamanho'
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0005'
ON DUPLICATE KEY UPDATE
valor = VALUES(valor),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();
