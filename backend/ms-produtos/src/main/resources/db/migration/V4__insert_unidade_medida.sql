-- ============================================================
--  V4 - INSERT INICIAIS: unidade_medida
--  Insere unidades de medida padronizadas
--  Arquitetura: MULTI-TENANT (empresa_id)
-- ============================================================

INSERT INTO unidade_medida (id, empresa_id, nome, sigla, descricao, ativo) VALUES
(1, 1, 'Unidade',     'UN', 'Unidade padrão para contagem geral', TRUE),
(2, 1, 'Quilograma',  'KG', 'Medida de massa utilizada para produtos pesados', TRUE),
(3, 1, 'Litro',       'L',  'Medida volumétrica para líquidos', TRUE),
(4, 1, 'Caixa',       'CX', 'Agrupamento de unidades em embalagens', TRUE),
(5, 1, 'Metro',       'M',  'Medida linear para tecidos e materiais diversos', TRUE);
