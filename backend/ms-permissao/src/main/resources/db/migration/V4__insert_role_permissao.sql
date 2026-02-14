-- ============================================================
--  INSERT INICIAL: Role x Permissão (Tenant 0 / empresa 1)
-- ============================================================

-- UNICOS_ADMIN -> todas as permissões existentes
INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    1,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN';

-- ADMIN -> todas as permissões administrativas (ROLE, PERMISSAO, ROLE_PERMISSAO, ROLE_USUARIO)
INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    1,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
AND p.nome IN (
    'ROLE_CRIAR',
    'ROLE_EDITAR',
    'ROLE_LISTAR',
    'ROLE_EXCLUIR',

    'PERMISSAO_CRIAR',
    'PERMISSAO_EDITAR',
    'PERMISSAO_LISTAR',
    'PERMISSAO_EXCLUIR',

    'ROLE_PERMISSAO_CRIAR',
    'ROLE_PERMISSAO_EDITAR',
    'ROLE_PERMISSAO_LISTAR',
    'ROLE_PERMISSAO_EXCLUIR',

    'ROLE_USUARIO_CRIAR',
    'ROLE_USUARIO_EDITAR',
    'ROLE_USUARIO_LISTAR',
    'ROLE_USUARIO_EXCLUIR'
);

-- GERENTE -> permissões de listagem (visão / leitura)
INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    1,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
AND p.nome IN (
    'ROLE_LISTAR',
    'PERMISSAO_LISTAR',
    'ROLE_PERMISSAO_LISTAR',
    'ROLE_USUARIO_LISTAR'
);

-- OPERADOR -> apenas listagem de vínculo role/usuario (equivalente ao "USUARIO_LISTAR" do seu script antigo)
INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    1,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
AND p.nome IN (
    'ROLE_USUARIO_LISTAR'
);

-- SUPORTE -> permissões de listagem para apoio operacional
INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    1,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
AND p.nome IN (
    'ROLE_LISTAR',
    'PERMISSAO_LISTAR',
    'ROLE_PERMISSAO_LISTAR',
    'ROLE_USUARIO_LISTAR'
);

-- LEITURA -> somente listagem
INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    1,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
AND p.nome IN (
    'ROLE_LISTAR',
    'PERMISSAO_LISTAR',
    'ROLE_PERMISSAO_LISTAR',
    'ROLE_USUARIO_LISTAR'
);
