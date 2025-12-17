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
    1,
    NULL,
    NULL,
    NOW(),
    1
);

-- Atribuir ROLE UNICOS_ADMIN
INSERT INTO usuario_role (
    empresa_id,
    usuario_id,
    role_id,
    criado_em,
    ativo
)
SELECT
    0,
    u.id,
    r.id,
    NOW(),
    1
FROM usuario u, role r
WHERE u.login = 'admin'
  AND r.nome = 'UNICOS_ADMIN';
