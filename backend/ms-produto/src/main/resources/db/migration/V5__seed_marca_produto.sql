-- =========================================================
-- SEED - MARCAS PADRÃO DO CATÁLOGO
-- =========================================================

INSERT INTO marca_produto (
    empresa_id,
    nome,
    descricao,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES

-- Alimentos / Bebidas
(1, 'Coca-Cola', 'Marca de bebidas', 1, NOW(), NULL, NULL, TRUE),
(1, 'Pepsi', 'Marca de bebidas', 1, NOW(), NULL, NULL, TRUE),
(1, 'Nestlé', 'Alimentos e bebidas', 1, NOW(), NULL, NULL, TRUE),
(1, 'Ambev', 'Bebidas', 1, NOW(), NULL, NULL, TRUE),

-- Varejo / Genérico
(1, 'Genérico', 'Marca genérica (sem fabricante definido)', 1, NOW(), NULL, NULL, TRUE),

-- Informática
(1, 'Dell', 'Equipamentos de informática', 1, NOW(), NULL, NULL, TRUE),
(1, 'Lenovo', 'Equipamentos de informática', 1, NOW(), NULL, NULL, TRUE),
(1, 'HP', 'Equipamentos de informática', 1, NOW(), NULL, NULL, TRUE),
(1, 'Logitech', 'Periféricos e acessórios', 1, NOW(), NULL, NULL, TRUE)

ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
ativo = VALUES(ativo);
