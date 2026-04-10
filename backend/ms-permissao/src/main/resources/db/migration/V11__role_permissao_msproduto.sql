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
-- GERENTE -> leitura completa + gestão operacional de catálogo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'GERENTE'
  AND p.nome IN (
    'PRODUTO_LISTAR',
    'PRODUTO_ATRIBUTOS_LISTAR',
    'PRODUTO_ATRIBUTOS_VALORES_LISTAR',
    'PRODUTO_CATEGORIA_LISTAR',
    'PRODUTO_CODIGO_BARRAS_LISTAR',
    'PRODUTO_IMAGENS_LISTAR',
    'PRODUTO_MARCAS_PRODUTO_LISTAR',
    'PRODUTO_PRECO_BASE_LISTAR',
    'PRODUTO_TIPOS_LISTAR',
    'PRODUTO_UNIDADE_MEDIDA_LISTAR',

    'PRODUTO_EDITAR',
    'PRODUTO_ATRIBUTOS_EDITAR',
    'PRODUTO_ATRIBUTOS_VALORES_EDITAR',
    'PRODUTO_CATEGORIA_EDITAR',
    'PRODUTO_CODIGO_BARRAS_EDITAR',
    'PRODUTO_IMAGENS_EDITAR',
    'PRODUTO_MARCAS_PRODUTO_EDITAR',
    'PRODUTO_PRECO_BASE_EDITAR',
    'PRODUTO_TIPOS_EDITAR',
    'PRODUTO_UNIDADE_MEDIDA_EDITAR'
  );


-- ============================================================
-- OPERADOR -> leitura operacional do catálogo utilizável
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'OPERADOR'
  AND p.nome IN (
    'PRODUTO_LISTAR',
    'PRODUTO_CATEGORIA_LISTAR',
    'PRODUTO_CODIGO_BARRAS_LISTAR',
    'PRODUTO_PRECO_BASE_LISTAR',
    'PRODUTO_TIPOS_LISTAR',
    'PRODUTO_UNIDADE_MEDIDA_LISTAR',
    'PRODUTO_MARCAS_PRODUTO_LISTAR',
    'PRODUTO_IMAGENS_LISTAR'
  );


-- ============================================================
-- SUPORTE -> leitura total + manutenção básica
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.nome = 'SUPORTE'
  AND p.nome IN (
    'PRODUTO_LISTAR',
    'PRODUTO_ATRIBUTOS_LISTAR',
    'PRODUTO_ATRIBUTOS_VALORES_LISTAR',
    'PRODUTO_CATEGORIA_LISTAR',
    'PRODUTO_CODIGO_BARRAS_LISTAR',
    'PRODUTO_IMAGENS_LISTAR',
    'PRODUTO_MARCAS_PRODUTO_LISTAR',
    'PRODUTO_PRECO_BASE_LISTAR',
    'PRODUTO_TIPOS_LISTAR',
    'PRODUTO_UNIDADE_MEDIDA_LISTAR',

    'PRODUTO_CODIGO_BARRAS_EDITAR',
    'PRODUTO_IMAGENS_EDITAR',
    'PRODUTO_PRECO_BASE_EDITAR'
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
    'PRODUTO_LISTAR',
    'PRODUTO_ATRIBUTOS_LISTAR',
    'PRODUTO_ATRIBUTOS_VALORES_LISTAR',
    'PRODUTO_CATEGORIA_LISTAR',
    'PRODUTO_CODIGO_BARRAS_LISTAR',
    'PRODUTO_IMAGENS_LISTAR',
    'PRODUTO_MARCAS_PRODUTO_LISTAR',
    'PRODUTO_PRECO_BASE_LISTAR',
    'PRODUTO_TIPOS_LISTAR',
    'PRODUTO_UNIDADE_MEDIDA_LISTAR'
  );