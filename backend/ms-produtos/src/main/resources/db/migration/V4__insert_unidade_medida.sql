-- ============================================================
--  V4 - INSERT INICIAIS: unidade_medida
--  Insere 5 unidades de medida padronizadas
-- ============================================================

INSERT INTO unidade_medida (id, nome, sigla, descricao, ativo) VALUES
(1, 'Unidade', 'UN', 'Unidade padrão para contagem geral', TRUE),
(2, 'Quilograma', 'KG', 'Medida de massa utilizada para produtos pesados', TRUE),
(3, 'Litro', 'L', 'Medida volumétrica para líquidos', TRUE),
(4, 'Caixa', 'CX', 'Agrupamento de unidades em embalagens', TRUE),
(5, 'Metro', 'M', 'Medida linear para tecidos e materiais diversos', TRUE);
