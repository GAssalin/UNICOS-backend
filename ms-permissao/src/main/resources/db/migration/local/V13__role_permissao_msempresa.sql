-- ============================================================
-- ROLE x PERMISSAO (MS-EMPRESA)
-- ============================================================

-- ============================================================
-- UNICOS_ADMIN -> todas as permissoes do modulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN'
  AND p.nome LIKE 'EMPRESA_%';


-- ============================================================
-- ADMIN -> CRUD completo do modulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
  AND p.nome LIKE 'EMPRESA_%';


-- ============================================================
-- GERENTE -> gestao operacional completa, exceto exclusoes sensiveis
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome IN (
    'EMPRESA_LISTAR',
    'EMPRESA_EDITAR',

    'EMPRESA_CONFIGURACAO_CRIAR',
    'EMPRESA_CONFIGURACAO_EDITAR',
    'EMPRESA_CONFIGURACAO_LISTAR',

    'EMPRESA_CONTATO_CRIAR',
    'EMPRESA_CONTATO_EDITAR',
    'EMPRESA_CONTATO_LISTAR',

    'EMPRESA_ENDERECO_CRIAR',
    'EMPRESA_ENDERECO_EDITAR',
    'EMPRESA_ENDERECO_LISTAR',

    'EMPRESA_PARAMETRO_CRIAR',
    'EMPRESA_PARAMETRO_EDITAR',
    'EMPRESA_PARAMETRO_LISTAR',

    'EMPRESA_USUARIO_CRIAR',
    'EMPRESA_USUARIO_EDITAR',
    'EMPRESA_USUARIO_LISTAR'
  );


-- ============================================================
-- OPERADOR -> leitura operacional dos dados da empresa
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
  AND p.nome IN (
    'EMPRESA_LISTAR',
    'EMPRESA_CONFIGURACAO_LISTAR',
    'EMPRESA_CONTATO_LISTAR',
    'EMPRESA_ENDERECO_LISTAR',
    'EMPRESA_PARAMETRO_LISTAR',
    'EMPRESA_USUARIO_LISTAR'
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
    'EMPRESA_LISTAR',

    'EMPRESA_CONFIGURACAO_LISTAR',
    'EMPRESA_CONFIGURACAO_EDITAR',

    'EMPRESA_CONTATO_LISTAR',
    'EMPRESA_CONTATO_EDITAR',

    'EMPRESA_ENDERECO_LISTAR',
    'EMPRESA_ENDERECO_EDITAR',

    'EMPRESA_PARAMETRO_LISTAR',
    'EMPRESA_PARAMETRO_EDITAR',

    'EMPRESA_USUARIO_LISTAR'
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
    'EMPRESA_LISTAR',
    'EMPRESA_CONFIGURACAO_LISTAR',
    'EMPRESA_CONTATO_LISTAR',
    'EMPRESA_ENDERECO_LISTAR',
    'EMPRESA_PARAMETRO_LISTAR',
    'EMPRESA_USUARIO_LISTAR'
  );
