-- ============================================================
--  ROLE x PERMISSÃO (MS-USUARIOS)
-- ============================================================

-- ============================================================
-- UNICOS_ADMIN -> TODAS as permissões do módulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN'
  AND (
       p.nome LIKE 'USUARIO_EMAIL_%'
    OR p.nome LIKE 'USUARIO_%'
  );


-- ============================================================
-- ADMIN -> CRUD completo + operações relevantes
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
  AND p.nome IN (
    'USUARIO_CRIAR',
    'USUARIO_EDITAR',
    'USUARIO_LISTAR',
    'USUARIO_EXCLUIR',

    'USUARIO_EMAIL_CRIAR',
    'USUARIO_EMAIL_EDITAR',
    'USUARIO_EMAIL_LISTAR',
    'USUARIO_EMAIL_EXCLUIR',

    'USUARIO_TELEFONE_CRIAR',
    'USUARIO_TELEFONE_EDITAR',
    'USUARIO_TELEFONE_LISTAR',
    'USUARIO_TELEFONE_EXCLUIR',

    'USUARIO_ENDERECO_CRIAR',
    'USUARIO_ENDERECO_EDITAR',
    'USUARIO_ENDERECO_LISTAR',
    'USUARIO_ENDERECO_EXCLUIR',

    'USUARIO_PERFIL_VISUALIZAR',
    'USUARIO_PERFIL_EDITAR',

    'USUARIO_SENHA_ALTERAR',
    'USUARIO_SENHA_RESETAR',

    'USUARIO_ATIVAR',
    'USUARIO_DESATIVAR'
  );


-- ============================================================
-- GERENTE -> leitura + gestão operacional básica
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome IN (
    'USUARIO_LISTAR',
    'USUARIO_EMAIL_LISTAR',
    'USUARIO_TELEFONE_LISTAR',
    'USUARIO_ENDERECO_LISTAR',
    'USUARIO_PERFIL_VISUALIZAR',
    'USUARIO_ATIVAR',
    'USUARIO_DESATIVAR'
  );


-- ============================================================
-- SUPORTE -> leitura operacional + ações de suporte
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome IN (
    'USUARIO_LISTAR',
    'USUARIO_EMAIL_LISTAR',
    'USUARIO_TELEFONE_LISTAR',
    'USUARIO_ENDERECO_LISTAR',
    'USUARIO_PERFIL_VISUALIZAR',
    'USUARIO_SENHA_RESETAR'
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
    'USUARIO_LISTAR',
    'USUARIO_EMAIL_LISTAR',
    'USUARIO_TELEFONE_LISTAR',
    'USUARIO_ENDERECO_LISTAR',
    'USUARIO_PERFIL_VISUALIZAR'
  );