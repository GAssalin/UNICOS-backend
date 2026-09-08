INSERT INTO documento (
    empresa_id, pessoa_id, tipo_documento,
    numero, orgao_emissor, data_emissao,
    criado_por, criado_em, ativo
) VALUES
(1, 1, 'RG', '44556677', 'SSP-SP', '2010-04-12', 1, NOW(), TRUE),
(1, 3, 'CNPJ', '11222333000199', 'Receita Federal', '2005-03-01', 1, NOW(), TRUE);
