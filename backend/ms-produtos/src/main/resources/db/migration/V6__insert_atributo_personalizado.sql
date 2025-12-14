-- ============================================================
--  V6 - INSERT INICIAIS: atributo_personalizado
--  Insere atributos personalizados vinculados a categorias
--  Arquitetura: MULTI-TENANT (empresa_id)
-- ============================================================

INSERT INTO atributo_personalizado (
    id,
    empresa_id,
    nome,
    categoria_id
)
VALUES

-- Categoria 1: Eletrônicos
(1, 1, 'Cor', 1),

-- Categoria 2: Informática
(2, 1, 'Conectividade', 2),

-- Categoria 3: Vestuário
(3, 1, 'Tamanho', 3),

-- Categoria 4: Calçados
(4, 1, 'Material', 4),

-- Categoria 5: Móveis
(5, 1, 'Cor do Tecido', 5);
