-- ============================================================
--  ROLE x PERMISSÃO (MS-EMPRESA)
-- ============================================================


-- ============================================================
-- UNICOS_ADMIN -> todas as permissões do módulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN'
  AND p.nome LIKE 'EMPRESA_%';


-- ============================================================
-- ADMIN -> CRUD completo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
  AND p.nome LIKE 'EMPRESA_%';


-- ============================================================
-- GERENTE -> apenas leitura
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome LIKE 'EMPRESA_%'
  AND p.nome LIKE '%_LISTAR';


-- ============================================================
-- OPERADOR -> leitura operacional básica
-- (contato, endereço e usuários vinculados)
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
AND p.nome IN (
    'EMPRESA_CONTATO_LISTAR',
    'EMPRESA_ENDERECO_LISTAR',
    'EMPRESA_USUARIO_LISTAR'
);


-- ============================================================
-- SUPORTE -> leitura ampla
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome LIKE 'EMPRESA_%'
  AND p.nome LIKE '%_LISTAR';


-- ============================================================
-- LEITURA -> somente leitura
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
  AND p.nome LIKE 'EMPRESA_%'
  AND p.nome LIKE '%_LISTAR';
