-- ============================================================
--  ROLE x PERMISSÃO (MS-FILIAL)
-- ============================================================


-- ============================================================
-- UNICOS_ADMIN -> todas as permissões do módulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN'
  AND p.nome LIKE 'FILIAL_%';


-- ============================================================
-- ADMIN -> CRUD completo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'ADMIN'
  AND p.nome LIKE 'FILIAL_%';


-- ============================================================
-- GERENTE -> leitura total
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome LIKE 'FILIAL_%'
  AND p.nome LIKE '%_LISTAR';


-- ============================================================
-- OPERADOR -> leitura operacional (dados utilizáveis)
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
AND p.nome IN (
    'FILIAL_LISTAR',
    'FILIAL_CONTATO_LISTAR',
    'FILIAL_ENDERECO_LISTAR',
    'FILIAL_HORARIO_LISTAR',
    'FILIAL_STATUS_HISTORICO_LISTAR'
);


-- ============================================================
-- SUPORTE -> leitura ampla
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome LIKE 'FILIAL_%'
  AND p.nome LIKE '%_LISTAR';


-- ============================================================
-- LEITURA -> somente leitura
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'LEITURA'
  AND p.nome LIKE 'FILIAL_%'
  AND p.nome LIKE '%_LISTAR';
