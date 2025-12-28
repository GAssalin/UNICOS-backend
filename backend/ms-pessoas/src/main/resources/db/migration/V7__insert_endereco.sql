INSERT INTO endereco (
    empresa_id, pessoa_id, tipo_endereco,
    logradouro, numero, complemento, bairro,
    municipio_id, cep, principal,
    criado_por, criado_em, ativo
) VALUES
(1, 1, 'RESIDENCIAL', 'Rua das Flores', '123', NULL, 'Centro', 2, '09750000', TRUE, 1, NOW(), TRUE),
(1, 3, 'COMERCIAL', 'Av. Paulista', '1000', '10º andar', 'Bela Vista', 1, '01310000', TRUE, 1, NOW(), TRUE);
