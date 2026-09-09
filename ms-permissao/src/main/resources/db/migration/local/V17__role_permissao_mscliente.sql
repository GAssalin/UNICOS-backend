-- ============================================================
-- ROLE x PERMISSAO (MS-CLIENTE)
-- ============================================================

-- ============================================================
-- UNICOS_ADMIN -> todas as permissoes do modulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN'
  AND (
    p.nome LIKE 'CLIENTE_%'
  );


-- ============================================================
-- ADMIN -> CRUD completo do modulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
  AND (
    p.nome LIKE 'CLIENTE_%'
  );


-- ============================================================
-- GERENTE -> gestao operacional completa, exceto exclusoes sensiveis
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome IN (
    'CLIENTE_CRIAR',
    'CLIENTE_EDITAR',
    'CLIENTE_LISTAR',

    'CLIENTE_CATEGORIA_CRIAR',
    'CLIENTE_CATEGORIA_EDITAR',
    'CLIENTE_CATEGORIA_LISTAR',

    'CLIENTE_OBSERVACAO_CRIAR',
    'CLIENTE_OBSERVACAO_EDITAR',
    'CLIENTE_OBSERVACAO_LISTAR'
  );


-- ============================================================
-- OPERADOR -> leitura operacional + criacao de observacoes
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
  AND p.nome IN (
    'CLIENTE_LISTAR',
    'CLIENTE_CATEGORIA_LISTAR',
    'CLIENTE_OBSERVACAO_LISTAR',
    'CLIENTE_OBSERVACAO_CRIAR'
  );


-- ============================================================
-- SUPORTE -> leitura total + manutencao basica de cadastro
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome IN (
    'CLIENTE_LISTAR',
    'CLIENTE_EDITAR',

    'CLIENTE_CATEGORIA_LISTAR',
    'CLIENTE_CATEGORIA_EDITAR',

    'CLIENTE_OBSERVACAO_LISTAR',
    'CLIENTE_OBSERVACAO_EDITAR'
  );


-- ============================================================
-- LEITURA -> somente leitura
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
  AND p.nome IN (
    'CLIENTE_LISTAR',
    'CLIENTE_CATEGORIA_LISTAR',
    'CLIENTE_OBSERVACAO_LISTAR'
  );
