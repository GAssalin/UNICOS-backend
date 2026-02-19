-- =========================================================
-- SEED - UNIDADES DE MEDIDA PADRÃO DO SISTEMA
-- =========================================================
-- empresa_id = 1 (tenant inicial)
-- criado_por = 1 (system)
-- ativo = true

INSERT INTO unidade_medida (
    empresa_id,
    codigo,
    descricao,
    fracionavel,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES

-- Básicas comerciais
(1, 'UN', 'Unidade', FALSE, 1, NOW(), NULL, NULL, TRUE),
(1, 'PC', 'Peça', FALSE, 1, NOW(), NULL, NULL, TRUE),
(1, 'CX', 'Caixa', FALSE, 1, NOW(), NULL, NULL, TRUE),
(1, 'FD', 'Fardo', FALSE, 1, NOW(), NULL, NULL, TRUE),

-- Peso
(1, 'KG', 'Quilograma', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 'G',  'Grama', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 'T',  'Tonelada', TRUE, 1, NOW(), NULL, NULL, TRUE),

-- Volume
(1, 'L',  'Litro', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 'ML', 'Mililitro', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 'M3', 'Metro cúbico', TRUE, 1, NOW(), NULL, NULL, TRUE),

-- Comprimento
(1, 'M',  'Metro', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 'CM', 'Centímetro', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 'MM', 'Milímetro', TRUE, 1, NOW(), NULL, NULL, TRUE),

-- Área
(1, 'M2', 'Metro quadrado', TRUE, 1, NOW(), NULL, NULL, TRUE),

-- Outros comuns
(1, 'PAR', 'Par', FALSE, 1, NOW(), NULL, NULL, TRUE),
(1, 'KIT', 'Kit', FALSE, 1, NOW(), NULL, NULL, TRUE),
(1, 'PCT', 'Pacote', FALSE, 1, NOW(), NULL, NULL, TRUE),
(1, 'SC',  'Saco', FALSE, 1, NOW(), NULL, NULL, TRUE),
(1, 'BD',  'Balde', FALSE, 1, NOW(), NULL, NULL, TRUE)

ON DUPLICATE KEY UPDATE
descricao = VALUES(descricao),
fracionavel = VALUES(fracionavel),
ativo = VALUES(ativo);
