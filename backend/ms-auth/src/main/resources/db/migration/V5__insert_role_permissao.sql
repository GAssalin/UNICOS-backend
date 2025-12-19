-- ============================================================
--  V6 - INSERT INICIAL: Role x Permissão (Tenant 0)
-- ============================================================

INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    0,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN';

INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    0,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
AND p.nome IN (
    'USUARIO_CRIAR',
    'USUARIO_EDITAR',
    'USUARIO_LISTAR',
    'USUARIO_EXCLUIR',
    'ROLE_CRIAR',
    'ROLE_EDITAR',
    'ROLE_LISTAR',
    'ROLE_EXCLUIR',
    'PERMISSAO_CRIAR',
    'PERMISSAO_EDITAR',
    'PERMISSAO_LISTAR',
    'PERMISSAO_EXCLUIR',
    'EMPRESA_ROLE_PERMISSAO_CRIAR',
    'EMPRESA_ROLE_PERMISSAO_EDITAR',
    'EMPRESA_ROLE_PERMISSAO_LISTAR',
    'EMPRESA_ROLE_PERMISSAO_EXCLUIR',
    'AUDITORIA_LISTAR'
);

INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    0,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
AND p.nome IN (
    'USUARIO_LISTAR',
    'ROLE_LISTAR',
    'PERMISSAO_LISTAR',
    'AUDITORIA_LISTAR'
);

INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    0,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
AND p.nome IN (
    'USUARIO_LISTAR'
);

INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    0,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
AND p.nome IN (
    'SUPORTE_ACESSAR',
    'AUDITORIA_LISTAR'
);

INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    0,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
AND p.nome IN (
    'USUARIO_LISTAR',
    'ROLE_LISTAR',
    'PERMISSAO_LISTAR'
);

