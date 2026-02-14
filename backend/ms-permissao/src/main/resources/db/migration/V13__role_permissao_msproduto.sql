-- ============================================================
--  ROLE x PERMISSÃO (MS-PRODUTO)
-- ============================================================


-- ============================================================
-- UNICOS_ADMIN -> todas as permissões do módulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN'
  AND p.nome LIKE 'PRODUTO_%';


-- ============================================================
-- ADMIN -> CRUD completo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
  AND p.nome LIKE 'PRODUTO_%';


-- ============================================================
-- GERENTE -> leitura total do catálogo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome LIKE 'PRODUTO_%'
  AND p.nome LIKE '%_LISTAR';


-- ============================================================
-- OPERADOR -> leitura operacional (somente catálogo utilizável)
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
AND p.nome IN (
    'PRODUTO_LISTAR',
    'PRODUTO_CODIGO_BARRAS_LISTAR',
    'PRODUTO_PRECO_BASE_LISTAR'
);


-- ============================================================
-- SUPORTE -> leitura total
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome LIKE 'PRODUTO_%'
  AND p.nome LIKE '%_LISTAR';


-- ============================================================
-- LEITURA -> somente leitura
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
  AND p.nome LIKE 'PRODUTO_%'
  AND p.nome LIKE '%_LISTAR';
