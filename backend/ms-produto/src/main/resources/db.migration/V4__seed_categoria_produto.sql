-- =========================================================
-- SEED - CATEGORIAS PADRÃO DO CATÁLOGO
-- =========================================================

-- Nível raiz
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
ON DUPLICATE KEY UPDATE nome = nome;



-- =========================================================
-- SUBCATEGORIAS
-- =========================================================

-- Bebidas
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, ativo)
SELECT 1, 'Refrigerantes', 'Bebidas gaseificadas',
       id, 1, NOW(), TRUE
FROM categoria_produto
WHERE empresa_id = 1 AND nome = 'Bebidas'
ON DUPLICATE KEY UPDATE nome = nome;

INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, ativo)
SELECT 1, 'Água', 'Água mineral',
       id, 1, NOW(), TRUE
FROM categoria_produto
WHERE empresa_id = 1 AND nome = 'Bebidas'
ON DUPLICATE KEY UPDATE nome = nome;



-- Informática
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, ativo)
SELECT 1, 'Computadores', 'Desktops e notebooks',
       id, 1, NOW(), TRUE
FROM categoria_produto
WHERE empresa_id = 1 AND nome = 'Informática'
ON DUPLICATE KEY UPDATE nome = nome;

INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, ativo)
SELECT 1, 'Periféricos', 'Teclado, mouse, monitor',
       id, 1, NOW(), TRUE
FROM categoria_produto
WHERE empresa_id = 1 AND nome = 'Informática'
ON DUPLICATE KEY UPDATE nome = nome;

INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, ativo)
SELECT 1, 'Cabos e Conectores', 'Cabos diversos',
       id, 1, NOW(), TRUE
FROM categoria_produto
WHERE empresa_id = 1 AND nome = 'Informática'
ON DUPLICATE KEY UPDATE nome = nome;



-- Serviços
INSERT INTO categoria_produto
(empresa_id, nome, descricao, categoria_pai_id, criado_por, criado_em, ativo)
SELECT 1, 'Serviços Técnicos', 'Manutenção e suporte',
       id, 1, NOW(), TRUE
FROM categoria_produto
WHERE empresa_id = 1 AND nome = 'Serviços'
ON DUPLICATE KEY UPDATE nome = nome;
