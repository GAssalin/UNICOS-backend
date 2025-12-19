-- ============================================================
--  V5 - INSERT INICIAL: Usuário ADMIN
-- ============================================================

INSERT INTO usuario (
    empresa_id,
    login,
    pessoa_id,
    password,
    email,
    email_verificado,
    refresh_token,
    expiracao_refresh_token,
    criado_em,
    ativo
) VALUES (
    0,
    'admin',
    NULL,
    '$2a$10$LAnWRm2Abm4HN0b.cYnUQeu7Tf5DbGeI6BzueIFsdJF6Bv7eb8vzq',
    'admin@unicos.com',
    TRUE,
    NULL,
    NULL,
    NOW(),
    TRUE
);

INSERT INTO usuario (
    empresa_id,
    login,
    pessoa_id,
    password,
    email,
    email_verificado,
    refresh_token,
    expiracao_refresh_token,
    criado_em,
    ativo
) VALUES (
    0,
    'gerente',
    NULL,
    '$2a$10$LAnWRm2Abm4HN0b.cYnUQeu7Tf5DbGeI6BzueIFsdJF6Bv7eb8vzq',
    'gerente@unicos.com',
    TRUE,
    NULL,
    NULL,
    NOW(),
    TRUE
);

INSERT INTO usuario (
    empresa_id,
    login,
    pessoa_id,
    password,
    email,
    email_verificado,
    refresh_token,
    expiracao_refresh_token,
    criado_em,
    ativo
) VALUES (
    0,
    'operador',
    NULL,
    '$2a$10$LAnWRm2Abm4HN0b.cYnUQeu7Tf5DbGeI6BzueIFsdJF6Bv7eb8vzq',
    'operador@unicos.com',
    TRUE,
    NULL,
    NULL,
    NOW(),
    TRUE
);

INSERT INTO usuario (
    empresa_id,
    login,
    pessoa_id,
    password,
    email,
    email_verificado,
    refresh_token,
    expiracao_refresh_token,
    criado_em,
    ativo
) VALUES (
    0,
    'suporte',
    NULL,
    '$2a$10$LAnWRm2Abm4HN0b.cYnUQeu7Tf5DbGeI6BzueIFsdJF6Bv7eb8vzq',
    'suporte@unicos.com',
    TRUE,
    NULL,
    NULL,
    NOW(),
    TRUE
);

INSERT INTO usuario (
    empresa_id,
    login,
    pessoa_id,
    password,
    email,
    email_verificado,
    refresh_token,
    expiracao_refresh_token,
    criado_em,
    ativo
) VALUES (
    0,
    'leitura',
    NULL,
    '$2a$10$LAnWRm2Abm4HN0b.cYnUQeu7Tf5DbGeI6BzueIFsdJF6Bv7eb8vzq',
    'leitura@unicos.com',
    TRUE,
    NULL,
    NULL,
    NOW(),
    TRUE
);
