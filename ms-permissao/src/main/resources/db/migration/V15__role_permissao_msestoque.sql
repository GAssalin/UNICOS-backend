-- ============================================================
-- ROLE x PERMISSAO (MS-ESTOQUE)
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
    p.nome LIKE 'ESTOQUE_%'
    OR p.nome LIKE 'RESPONSAVEL_ESTOQUE_%'
    OR p.nome LIKE 'VINCULO_ESTOQUE_FILIAL_%'
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
    p.nome LIKE 'ESTOQUE_%'
    OR p.nome LIKE 'RESPONSAVEL_ESTOQUE_%'
    OR p.nome LIKE 'VINCULO_ESTOQUE_FILIAL_%'
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
    'ESTOQUE_CRIAR',
    'ESTOQUE_EDITAR',
    'ESTOQUE_LISTAR',

    'RESPONSAVEL_ESTOQUE_CRIAR',
    'RESPONSAVEL_ESTOQUE_EDITAR',
    'RESPONSAVEL_ESTOQUE_LISTAR',

    'VINCULO_ESTOQUE_FILIAL_CRIAR',
    'VINCULO_ESTOQUE_FILIAL_EDITAR',
    'VINCULO_ESTOQUE_FILIAL_LISTAR'
  );


-- ============================================================
-- OPERADOR -> leitura operacional do modulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
  AND p.nome IN (
    'ESTOQUE_LISTAR',
    'RESPONSAVEL_ESTOQUE_LISTAR',
    'VINCULO_ESTOQUE_FILIAL_LISTAR'
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
    'ESTOQUE_LISTAR',
    'ESTOQUE_EDITAR',

    'RESPONSAVEL_ESTOQUE_LISTAR',
    'RESPONSAVEL_ESTOQUE_EDITAR',

    'VINCULO_ESTOQUE_FILIAL_LISTAR',
    'VINCULO_ESTOQUE_FILIAL_EDITAR'
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
    'ESTOQUE_LISTAR',
    'RESPONSAVEL_ESTOQUE_LISTAR',
    'VINCULO_ESTOQUE_FILIAL_LISTAR'
  );
