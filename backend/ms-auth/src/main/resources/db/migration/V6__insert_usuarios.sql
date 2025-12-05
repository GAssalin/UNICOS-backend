-- ============================================================
--  V6 - INSERT INICIAIS: Usuários
-- ============================================================

-- Todos com senha: "12345678"
-- Hash Bcrypt gerado com custo 10
-- $2a$10$N9qo8uLOickgx2ZMRZo5e.Px/...  (exemplo real abaixo)

-- Hash real (senha 12345678):
-- $2a$10$Bo5G1KVgIo8oc1wmQ6um1O01mRC17UZ/kO5gMP7w1hLolc.a1LdZa

INSERT INTO usuario (
    id, login, pessoa_id, password, email, email_verificado, refresh_token,
    expiracao_refresh_token, ativo, criado_em, atualizado_em
) VALUES
(1, 'admin', NULL, '$2a$10$Bo5G1KVgIo8oc1wmQ6um1O01mRC17UZ/kO5gMP7w1hLolc.a1LdZa',
    'admin@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW()),

(2, 'gerente', NULL, '$2a$10$Bo5G1KVgIo8oc1wmQ6um1O01mRC17UZ/kO5gMP7w1hLolc.a1LdZa',
    'gerente@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW()),

(3, 'operador', NULL, '$2a$10$Bo5G1KVgIo8oc1wmQ6um1O01mRC17UZ/kO5gMP7w1hLolc.a1LdZa',
    'operador@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW()),

(4, 'suporte', NULL, '$2a$10$Bo5G1KVgIo8oc1wmQ6um1O01mRC17UZ/kO5gMP7w1hLolc.a1LdZa',
    'suporte@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW()),

(5, 'leitura', NULL, '$2a$10$Bo5G1KVgIo8oc1wmQ6um1O01mRC17UZ/kO5gMP7w1hLolc.a1LdZa',
    'leitura@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW());
