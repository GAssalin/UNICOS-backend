-- =========================================================
-- SEED - CATEGORIAS PADRÃO DO CATÁLOGO
-- =========================================================

-- ---------------------------------------------------------
-- NÍVEL RAIZ
-- ---------------------------------------------------------
INSERT INTO categoria_produto (
    empresa_id,
    nome,
    descricao,
    categoria_pai_id,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
)
VALUES
(1, 'Alimentos', 'Produtos alimentícios em geral', NULL, 1, NOW(), NULL, NULL, TRUE),
(1, 'Bebidas', 'Bebidas alcoólicas e não alcoólicas', NULL, 1, NOW(), NULL, NULL, TRUE),
(1, 'Informática', 'Equipamentos e acessórios de TI', NULL, 1, NOW(), NULL, NULL, TRUE),
(1, 'Serviços', 'Serviços prestados pela empresa', NULL, 1, NOW(), NULL, NULL, TRUE),
(1, 'Materiais de Escritório', 'Itens de uso administrativo', NULL, 1, NOW(), NULL, NULL, TRUE)
ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();



-- ---------------------------------------------------------
-- SUBCATEGORIAS
-- ---------------------------------------------------------

-- Bebidas -> Refrigerantes
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo)
SELECT
    1,
    'Refrigerantes',
    'Bebidas gaseificadas',
    c.id,
    1,
    NOW(),
    NULL,
    NULL,
    TRUE
FROM categoria_produto c
WHERE c.empresa_id = 1
  AND c.nome = 'Bebidas'
ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
categoria_pai_id = VALUES(categoria_pai_id),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();

-- Bebidas -> Água
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo)
SELECT
    1,
    'Água',
    'Água mineral',
    c.id,
    1,
    NOW(),
    NULL,
    NULL,
    TRUE
FROM categoria_produto c
WHERE c.empresa_id = 1
  AND c.nome = 'Bebidas'
ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
categoria_pai_id = VALUES(categoria_pai_id),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();



-- Informática -> Computadores
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo)
SELECT
    1,
    'Computadores',
    'Desktops e notebooks',
    c.id,
    1,
    NOW(),
    NULL,
    NULL,
    TRUE
FROM categoria_produto c
WHERE c.empresa_id = 1
  AND c.nome = 'Informática'
ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
categoria_pai_id = VALUES(categoria_pai_id),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();

-- Informática -> Periféricos
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo)
SELECT
    1,
    'Periféricos',
    'Teclado, mouse, monitor',
    c.id,
    1,
    NOW(),
    NULL,
    NULL,
    TRUE
FROM categoria_produto c
WHERE c.empresa_id = 1
  AND c.nome = 'Informática'
ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
categoria_pai_id = VALUES(categoria_pai_id),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();

-- Informática -> Cabos e Conectores
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo)
SELECT
    1,
    'Cabos e Conectores',
    'Cabos diversos',
    c.id,
    1,
    NOW(),
    NULL,
    NULL,
    TRUE
FROM categoria_produto c
WHERE c.empresa_id = 1
  AND c.nome = 'Informática'
ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
categoria_pai_id = VALUES(categoria_pai_id),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();



-- Serviços -> Serviços Técnicos
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo)
SELECT
    1,
    'Serviços Técnicos',
    'Manutenção e suporte',
    c.id,
    1,
    NOW(),
    NULL,
    NULL,
    TRUE
FROM categoria_produto c
WHERE c.empresa_id = 1
  AND c.nome = 'Serviços'
ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
categoria_pai_id = VALUES(categoria_pai_id),
ativo = VALUES(ativo),
atualizado_por = 1,
atualizado_em = NOW();
