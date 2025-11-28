-- ============================================================
--  V8 - Inserts iniciais: Tokens de verificação de e-mail
-- ============================================================

INSERT INTO usuario_email_verificacao (id, usuario_id, tokenHash, expiracao, utilizado)
VALUES
(1, 1, 'HASH_ADMIN_ABC123', DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE),
(2, 2, 'HASH_GERENTE_DEF456', DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE),
(3, 3, 'HASH_OPERADOR_GHI789', DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE),
(4, 4, 'HASH_SUPORTE_JKL012', DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE),
(5, 5, 'HASH_LEITURA_MNO345', DATE_ADD(NOW(), INTERVAL 24 HOUR), FALSE);
