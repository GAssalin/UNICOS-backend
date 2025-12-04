-- ============================================================
--  V6 - INSERT INICIAIS: atributo_personalizado
--  Insere 5 atributos, cada um vinculado a uma categoria.
-- ============================================================

INSERT INTO atributo_personalizado (id, nome, categoria_id)
VALUES
-- Categoria 1: Eletrônicos
(1, 'Cor', 1),

-- Categoria 2: Informática
(2, 'Conectividade', 2),

-- Categoria 3: Vestuário
(3, 'Tamanho', 3),

-- Categoria 4: Calçados
(4, 'Material', 4),

-- Categoria 5: Móveis
(5, 'Cor do Tecido', 5);
