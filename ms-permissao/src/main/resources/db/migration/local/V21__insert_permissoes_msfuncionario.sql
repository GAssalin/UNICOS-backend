-- ============================================================
-- INSERT INICIAIS: MS-FUNCIONARIO
--
-- A consulta do próprio cadastro (/v1/funcionarios/me) não exige
-- permissão: está disponível para qualquer usuário autenticado.
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
-- FUNCIONARIOS
(1, 'FUNCIONARIO_CRIAR', 'Permite cadastrar funcionario', NOW(), 1),
(1, 'FUNCIONARIO_EDITAR', 'Permite atualizar funcionario', NOW(), 1),
(1, 'FUNCIONARIO_LISTAR', 'Permite consultar e listar funcionarios', NOW(), 1),
(1, 'FUNCIONARIO_EXCLUIR', 'Permite remover funcionario', NOW(), 1),

-- CARGOS (o papel do cargo define o acesso a carteira de clientes)
(1, 'FUNCIONARIO_CARGO_CRIAR', 'Permite criar cargo', NOW(), 1),
(1, 'FUNCIONARIO_CARGO_EDITAR', 'Permite atualizar cargo', NOW(), 1),
(1, 'FUNCIONARIO_CARGO_LISTAR', 'Permite consultar e listar cargos', NOW(), 1),
(1, 'FUNCIONARIO_CARGO_EXCLUIR', 'Permite remover cargo', NOW(), 1);


-- ============================================================
-- UNICOS_ADMIN e ADMIN -> todas as permissoes do modulo
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.empresa_id = 1
  AND r.nome IN ('UNICOS_ADMIN', 'ADMIN')
  AND p.nome LIKE 'FUNCIONARIO_%';


-- ============================================================
-- GERENTE -> gestao da equipe; cargos apenas para consulta
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.empresa_id = 1
  AND r.nome = 'GERENTE'
  AND p.nome IN (
    'FUNCIONARIO_CRIAR',
    'FUNCIONARIO_EDITAR',
    'FUNCIONARIO_LISTAR',
    'FUNCIONARIO_CARGO_LISTAR'
  );


-- ============================================================
-- SUPORTE e LEITURA -> somente leitura
-- ============================================================
INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT 1, r.id, p.id, NOW(), 1
FROM role r
JOIN permissao p
WHERE r.empresa_id = 1
  AND r.nome IN ('SUPORTE', 'LEITURA')
  AND p.nome IN (
    'FUNCIONARIO_LISTAR',
    'FUNCIONARIO_CARGO_LISTAR'
  );
