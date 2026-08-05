INSERT INTO pessoa_relacao (
    empresa_id, pessoa_id, relacionado_id, tipo_relacao_pessoa_id,
    criado_por, criado_em, ativo
) VALUES
(1, 1, 2, 1, 1, NOW(), TRUE), -- João é Pai de Maria
(1, 3, 1, 5, 1, NOW(), TRUE); -- João é Representante Legal da Empresa
