-- =========================================================
-- VINCULA PRODUTO -> CATEGORIA / MARCA (SEM FK FÍSICA)
-- =========================================================
-- Estratégia:
-- 1) Resolve IDs por nome (categoria / marca)
-- 2) Atualiza produtos seedados por codigo (SKU/SRV/DIG/SUB)
-- =========================================================

-- -----------------------------
-- Categorias alvo (IDs)
-- -----------------------------
-- Bebidas -> Refrigerantes
UPDATE produto p
JOIN categoria_produto c ON c.empresa_id = p.empresa_id AND c.nome = 'Refrigerantes'
SET p.categoria_id = c.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0001');

-- Bebidas -> Água
UPDATE produto p
JOIN categoria_produto c ON c.empresa_id = p.empresa_id AND c.nome = 'Água'
SET p.categoria_id = c.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0005');

-- Alimentos (raiz)
UPDATE produto p
JOIN categoria_produto c ON c.empresa_id = p.empresa_id AND c.nome = 'Alimentos'
SET p.categoria_id = c.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0002');

-- Informática -> Computadores
UPDATE produto p
JOIN categoria_produto c ON c.empresa_id = p.empresa_id AND c.nome = 'Computadores'
SET p.categoria_id = c.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0003');

-- Informática -> Cabos e Conectores
UPDATE produto p
JOIN categoria_produto c ON c.empresa_id = p.empresa_id AND c.nome = 'Cabos e Conectores'
SET p.categoria_id = c.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0004');

-- Serviços -> Serviços Técnicos
UPDATE produto p
JOIN categoria_produto c ON c.empresa_id = p.empresa_id AND c.nome = 'Serviços Técnicos'
SET p.categoria_id = c.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SRV-0001', 'SRV-0002', 'SUB-0001');

-- Digital (aqui fica em Informática por padrão, se quiser depois criamos "Digital")
UPDATE produto p
JOIN categoria_produto c ON c.empresa_id = p.empresa_id AND c.nome = 'Informática'
SET p.categoria_id = c.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('DIG-0001');


-- -----------------------------
-- Marcas alvo (IDs)
-- -----------------------------

-- Coca-Cola
UPDATE produto p
JOIN marca_produto m ON m.empresa_id = p.empresa_id AND m.nome = 'Coca-Cola'
SET p.marca_id = m.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0001');

-- Genérico
UPDATE produto p
JOIN marca_produto m ON m.empresa_id = p.empresa_id AND m.nome = 'Genérico'
SET p.marca_id = m.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0002', 'SKU-0004', 'SKU-0005', 'SRV-0001', 'SRV-0002', 'DIG-0001', 'SUB-0001');

-- Dell (para o notebook seedado)
UPDATE produto p
JOIN marca_produto m ON m.empresa_id = p.empresa_id AND m.nome = 'Dell'
SET p.marca_id = m.id,
    p.atualizado_por = 1,
    p.atualizado_em = NOW()
WHERE p.empresa_id = 1
  AND p.codigo IN ('SKU-0003');
