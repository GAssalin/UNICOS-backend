-- ============================================================
-- V7 - Vincular Usuário ADMIN à ROLE UNICOS_ADMIN
-- ============================================================

INSERT INTO role_usuario (
    empresa_id,
    usuario_id,
    role_nome,
    criado_em,
    ativo
)
VALUES (
    0,
    1,
    'UNICOS_ADMIN',
    NOW(),
    TRUE
);

INSERT INTO role_usuario (
    empresa_id,
    usuario_id,
    role_nome,
    criado_em,
    ativo
) VALUES (
    0,
    2,
    'GERENTE',
    NOW(),
    TRUE
);

INSERT INTO role_usuario (
    empresa_id,
    usuario_id,
    role_nome,
    criado_em,
    ativo
) VALUES (
    0,
    3,
    'OPERADOR',
    NOW(),
    TRUE
);

INSERT INTO role_usuario (
    empresa_id,
    usuario_id,
    role_nome,
    criado_em,
    ativo
) VALUES (
    0,
    4,
    'SUPORTE',
    NOW(),
    TRUE
);

INSERT INTO role_usuario (
    empresa_id,
    usuario_id,
    role_nome,
    criado_em,
    ativo
) VALUES (
    0,
    5,
    'LEITURA',
    NOW(),
    TRUE
);
