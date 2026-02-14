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
-- GERENTE -> somente leitura gerencial
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome LIKE 'PESSOA_%'
  AND p.nome LIKE '%_LISTAR';


-- ============================================================
-- OPERADOR -> leitura operacional básica
-- (apenas entidades principais)
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
AND p.nome IN (
    'PESSOA_LISTAR',
    'PESSOA_FISICA_LISTAR',
    'PESSOA_JURIDICA_LISTAR'
);


-- ============================================================
-- SUPORTE -> leitura ampla
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome LIKE 'PESSOA_%'
  AND p.nome LIKE '%_LISTAR';


-- ============================================================
-- LEITURA -> somente leitura geral
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
  AND p.nome LIKE 'PESSOA_%'
  AND p.nome LIKE '%_LISTAR';
