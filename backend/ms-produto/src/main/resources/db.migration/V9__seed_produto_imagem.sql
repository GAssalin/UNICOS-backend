-- =========================================================
-- SEED - IMAGENS DE PRODUTO (GALERIA)
-- =========================================================

-- SKU-0001 (3 imagens)
INSERT INTO produto_imagem (
    empresa_id, produto_id, url, alt_texto, principal, ordem,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1, p.id,
    'https://picsum.photos/seed/SKU-0001-1/800/800',
    'Coca-Cola Lata 350ml - imagem principal',
    TRUE, 1,
    1, NOW(), NULL, NULL, TRUE
FROM produto p
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0001'
  AND NOT EXISTS (
      SELECT 1 FROM produto_imagem i
      WHERE i.empresa_id = 1 AND i.produto_id = p.id AND i.ordem = 1
  );

INSERT INTO produto_imagem (
    empresa_id, produto_id, url, alt_texto, principal, ordem,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1, p.id,
    'https://picsum.photos/seed/SKU-0001-2/800/800',
    'Coca-Cola Lata 350ml - imagem 2',
    FALSE, 2,
    1, NOW(), NULL, NULL, TRUE
FROM produto p
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0001'
  AND NOT EXISTS (
      SELECT 1 FROM produto_imagem i
      WHERE i.empresa_id = 1 AND i.produto_id = p.id AND i.ordem = 2
  );

INSERT INTO produto_imagem (
    empresa_id, produto_id, url, alt_texto, principal, ordem,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1, p.id,
    'https://picsum.photos/seed/SKU-0001-3/800/800',
    'Coca-Cola Lata 350ml - imagem 3',
    FALSE, 3,
    1, NOW(), NULL, NULL, TRUE
FROM produto p
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0001'
  AND NOT EXISTS (
      SELECT 1 FROM produto_imagem i
      WHERE i.empresa_id = 1 AND i.produto_id = p.id AND i.ordem = 3
  );



-- SKU-0003 (Notebook) (3 imagens)
INSERT INTO produto_imagem (
    empresa_id, produto_id, url, alt_texto, principal, ordem,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1, p.id,
    'https://picsum.photos/seed/SKU-0003-1/800/800',
    'Notebook Core i5 8GB - imagem principal',
    TRUE, 1,
    1, NOW(), NULL, NULL, TRUE
FROM produto p
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0003'
  AND NOT EXISTS (
      SELECT 1 FROM produto_imagem i
      WHERE i.empresa_id = 1 AND i.produto_id = p.id AND i.ordem = 1
  );

INSERT INTO produto_imagem (
    empresa_id, produto_id, url, alt_texto, principal, ordem,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1, p.id,
    'https://picsum.photos/seed/SKU-0003-2/800/800',
    'Notebook Core i5 8GB - imagem 2',
    FALSE, 2,
    1, NOW(), NULL, NULL, TRUE
FROM produto p
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0003'
  AND NOT EXISTS (
      SELECT 1 FROM produto_imagem i
      WHERE i.empresa_id = 1 AND i.produto_id = p.id AND i.ordem = 2
  );

INSERT INTO produto_imagem (
    empresa_id, produto_id, url, alt_texto, principal, ordem,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1, p.id,
    'https://picsum.photos/seed/SKU-0003-3/800/800',
    'Notebook Core i5 8GB - imagem 3',
    FALSE, 3,
    1, NOW(), NULL, NULL, TRUE
FROM produto p
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0003'
  AND NOT EXISTS (
      SELECT 1 FROM produto_imagem i
      WHERE i.empresa_id = 1 AND i.produto_id = p.id AND i.ordem = 3
  );



-- SKU-0005 (Água) (2 imagens)
INSERT INTO produto_imagem (
    empresa_id, produto_id, url, alt_texto, principal, ordem,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1, p.id,
    'https://picsum.photos/seed/SKU-0005-1/800/800',
    'Água Mineral 500ml - imagem principal',
    TRUE, 1,
    1, NOW(), NULL, NULL, TRUE
FROM produto p
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0005'
  AND NOT EXISTS (
      SELECT 1 FROM produto_imagem i
      WHERE i.empresa_id = 1 AND i.produto_id = p.id AND i.ordem = 1
  );

INSERT INTO produto_imagem (
    empresa_id, produto_id, url, alt_texto, principal, ordem,
    criado_por, criado_em, atualizado_por, atualizado_em, ativo
)
SELECT
    1, p.id,
    'https://picsum.photos/seed/SKU-0005-2/800/800',
    'Água Mineral 500ml - imagem 2',
    FALSE, 2,
    1, NOW(), NULL, NULL, TRUE
FROM produto p
WHERE p.empresa_id = 1 AND p.codigo = 'SKU-0005'
  AND NOT EXISTS (
      SELECT 1 FROM produto_imagem i
      WHERE i.empresa_id = 1 AND i.produto_id = p.id AND i.ordem = 2
  );
