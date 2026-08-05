-- ============================================================
--  ROLE x PERMISSÃO (MS-PESSOAS)
-- ============================================================

-- ============================================================
-- UNICOS_ADMIN -> TODAS as permissões do módulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN'
  AND p.nome LIKE 'PESSOA_%';


-- ============================================================
-- ADMIN -> CRUD completo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
  AND p.nome LIKE 'PESSOA_%';


-- ============================================================
-- GERENTE -> leitura ampla + edição operacional
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome IN (
    'PESSOA_LISTAR',
    'PESSOA_CONTATO_LISTAR',
    'PESSOA_DOCUMENTO_LISTAR',
    'PESSOA_ENDERECO_LISTAR',
    'PESSOA_FISICA_LISTAR',
    'PESSOA_JURIDICA_LISTAR',
    'PESSOA_MUNICIPIO_LISTAR',
    'PESSOA_TIPO_RELACAO_PESSOA_LISTAR',

    'PESSOA_EDITAR',
    'PESSOA_CONTATO_EDITAR',
    'PESSOA_DOCUMENTO_EDITAR',
    'PESSOA_ENDERECO_EDITAR',
    'PESSOA_FISICA_EDITAR',
    'PESSOA_JURIDICA_EDITAR'
  );


-- ============================================================
-- OPERADOR -> leitura e manutenção operacional básica
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
  AND p.nome IN (
    'PESSOA_LISTAR',
    'PESSOA_CONTATO_LISTAR',
    'PESSOA_DOCUMENTO_LISTAR',
    'PESSOA_ENDERECO_LISTAR',
    'PESSOA_FISICA_LISTAR',
    'PESSOA_JURIDICA_LISTAR',

    'PESSOA_CONTATO_CRIAR',
    'PESSOA_CONTATO_EDITAR',
    'PESSOA_ENDERECO_CRIAR',
    'PESSOA_ENDERECO_EDITAR',
    'PESSOA_DOCUMENTO_CRIAR',
    'PESSOA_DOCUMENTO_EDITAR'
  );


-- ============================================================
-- SUPORTE -> leitura ampla + suporte cadastral
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome IN (
    'PESSOA_LISTAR',
    'PESSOA_CONTATO_LISTAR',
    'PESSOA_DOCUMENTO_LISTAR',
    'PESSOA_ENDERECO_LISTAR',
    'PESSOA_FISICA_LISTAR',
    'PESSOA_JURIDICA_LISTAR',
    'PESSOA_MUNICIPIO_LISTAR',
    'PESSOA_TIPO_RELACAO_PESSOA_LISTAR',

    'PESSOA_CONTATO_EDITAR',
    'PESSOA_DOCUMENTO_EDITAR',
    'PESSOA_ENDERECO_EDITAR'
  );


-- ============================================================
-- LEITURA -> somente leitura geral
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
  AND p.nome IN (
    'PESSOA_LISTAR',
    'PESSOA_CONTATO_LISTAR',
    'PESSOA_DOCUMENTO_LISTAR',
    'PESSOA_ENDERECO_LISTAR',
    'PESSOA_FISICA_LISTAR',
    'PESSOA_JURIDICA_LISTAR',
    'PESSOA_MUNICIPIO_LISTAR',
    'PESSOA_TIPO_RELACAO_PESSOA_LISTAR'
  );