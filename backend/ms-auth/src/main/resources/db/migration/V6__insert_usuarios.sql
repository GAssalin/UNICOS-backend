-- ============================================================
--  V6 - INSERT INICIAIS: Usuários
-- ============================================================

-- Todos com senha: "123456"
-- Hash Bcrypt gerado com custo 10
-- $2a$10$N9qo8uLOickgx2ZMRZo5e.Px/...  (exemplo real abaixo)

-- Hash real (senha 123456):
-- $2a$10$Dow1thHB0h8/QqXFz3vY5uAZy21nDZq.bw7lupaWQ57QfQhUCnH3i

INSERT INTO usuario (
    id, login, pessoa_id, password, email, email_verificado, refresh_token,
    expiracao_refresh_token, ativo, criado_em, atualizado_em
) VALUES
(1, 'admin', NULL, '$2a$10$Dow1thHB0h8/QqXFz3vY5uAZy21nDZq.bw7lupaWQ57QfQhUCnH3i',
    'admin@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW()),

(2, 'gerente', NULL, '$2a$10$Dow1thHB0h8/QqXFz3vY5uAZy21nDZq.bw7lupaWQ57QfQhUCnH3i',
    'gerente@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW()),

(3, 'operador', NULL, '$2a$10$Dow1thHB0h8/QqXFz3vY5uAZy21nDZq.bw7lupaWQ57QfQhUCnH3i',
    'operador@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW()),

(4, 'suporte', NULL, '$2a$10$Dow1thHB0h8/QqXFz3vY5uAZy21nDZq.bw7lupaWQ57QfQhUCnH3i',
    'suporte@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW()),

(5, 'leitura', NULL, '$2a$10$Dow1thHB0h8/QqXFz3vY5uAZy21nDZq.bw7lupaWQ57QfQhUCnH3i',
    'leitura@unicos.com', TRUE, NULL, NULL, TRUE, NOW(), NOW());
