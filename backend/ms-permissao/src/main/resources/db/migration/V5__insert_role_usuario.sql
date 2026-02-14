-- ============================================================
-- Vincular Usuário ADMIN à ROLE UNICOS_ADMIN
-- ============================================================

INSERT INTO role_usuario (
    empresa_id,
    usuario_id,
    role_nome,
    criado_em,
    ativo
)
VALUES (
    1,
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
    1,
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
    1,
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
    1,
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
    1,
    5,
    'LEITURA',
    NOW(),
    TRUE
);
