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
  AND p.nome LIKE 'USUARIO_EMAIL_%';


-- ============================================================
-- ADMIN -> CRUD completo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
  AND p.nome LIKE 'USUARIO_EMAIL_%';


-- ============================================================
-- GERENTE -> apenas leitura
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome = 'USUARIO_EMAIL_LISTAR';


-- ============================================================
-- SUPORTE -> leitura operacional
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome = 'USUARIO_EMAIL_LISTAR';


-- ============================================================
-- LEITURA -> somente leitura
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
  AND p.nome = 'USUARIO_EMAIL_LISTAR';
