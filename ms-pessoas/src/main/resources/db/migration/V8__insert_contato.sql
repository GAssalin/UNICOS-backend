INSERT INTO contato (
    empresa_id, pessoa_id, tipo_contato,
    valor, principal,
    criado_por, criado_em, ativo
) VALUES
(1, 1, 'CELULAR', '11999999999', TRUE, 1, NOW(), TRUE),
(1, 1, 'EMAIL', 'joao@email.com', FALSE, 1, NOW(), TRUE),
(1, 3, 'EMAIL', 'contato@empresa.com.br', TRUE, 1, NOW(), TRUE);
