INSERT INTO tipo_relacao_pessoa (
    empresa_id, nome, descricao,
    criado_por, criado_em, ativo
) VALUES
(1, 'Pai', 'Relação de paternidade', 1, NOW(), TRUE),
(1, 'Mãe', 'Relação de maternidade', 1, NOW(), TRUE),
(1, 'Dependente', 'Pessoa dependente financeiramente', 1, NOW(), TRUE),
(1, 'Sócio', 'Sócio de pessoa jurídica', 1, NOW(), TRUE),
(1, 'Representante Legal', 'Representante legal da pessoa', 1, NOW(), TRUE);
