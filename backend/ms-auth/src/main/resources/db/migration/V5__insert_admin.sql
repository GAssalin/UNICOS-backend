-- ============================================================
--  V5 - INSERT INICIAL: Usuário ADMIN + Atribuição de Role
-- ============================================================

-- Criar usuário administrador
INSERT INTO usuario (
    id,
    empresa_id,
    login,
    pessoa_id,
    password,
    email,
    email_verificado,
    refresh_token,
    expiracao_refresh_token,
    ativo,
    criado_em,
    atualizado_em
) VALUES (
    1,
    0,
    'admin',
    NULL,
    '$2a$10$kMBq/q.UCd6YpdUBKCbELO0O55dnROSVdOGgFyxPK1jjd6If6HfNO',
    'admin@unicos.com',
    1,
    NULL,
    NULL,
    1,
    NOW(),
    NOW()
);

-- Atribuir ROLE_ADMIN (role_id = 1)
INSERT INTO usuario_role (usuario_id, role_id)
VALUES (1, 1);