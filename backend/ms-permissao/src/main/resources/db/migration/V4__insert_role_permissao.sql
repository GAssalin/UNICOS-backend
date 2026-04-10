-- ============================================================
--  INSERT INICIAL: Role x Permissão
-- ============================================================

-- ============================================================
-- UNICOS_ADMIN -> todas as permissões existentes
-- ============================================================
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


-- ============================================================
-- ADMIN -> permissões administrativas completas
-- ============================================================
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


-- ============================================================
-- GERENTE -> leitura administrativa
-- ============================================================
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


-- ============================================================
-- OPERADOR -> leitura de vínculo usuário x role
-- ============================================================
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


-- ============================================================
-- SUPORTE -> leitura administrativa
-- ============================================================
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


-- ============================================================
-- LEITURA -> somente leitura
-- ============================================================
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