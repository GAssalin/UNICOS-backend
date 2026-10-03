-- ============================================================
--  Permissões dedicadas aos saldos por produto (/v1/estoques-produtos)
--  e às movimentações de estoque (/v1/movimentacoes-estoque e
--  /v1/movimentacoes-estoque-itens).
--
--  Os saldos eram protegidos pelas permissões ESTOQUE_* e as
--  movimentações não exigiam permissão alguma. Cada role recebe as
--  novas permissões equivalentes às ESTOQUE_* que já possuía.
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
(1, 'ESTOQUE_PRODUTO_CRIAR',        'Permite criar saldos de produto em estoque', NOW(), 1),
(1, 'ESTOQUE_PRODUTO_EDITAR',       'Permite atualizar saldos de produto em estoque', NOW(), 1),
(1, 'ESTOQUE_PRODUTO_LISTAR',       'Permite consultar saldos de produto em estoque', NOW(), 1),
(1, 'ESTOQUE_PRODUTO_EXCLUIR',      'Permite remover saldos de produto em estoque', NOW(), 1),
(1, 'ESTOQUE_MOVIMENTACAO_CRIAR',   'Permite registrar movimentações de estoque', NOW(), 1),
(1, 'ESTOQUE_MOVIMENTACAO_EDITAR',  'Permite atualizar movimentações de estoque', NOW(), 1),
(1, 'ESTOQUE_MOVIMENTACAO_LISTAR',  'Permite consultar movimentações de estoque', NOW(), 1),
(1, 'ESTOQUE_MOVIMENTACAO_EXCLUIR', 'Permite remover movimentações de estoque', NOW(), 1);

INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT rp.empresa_id, rp.role_id, nova.id, NOW(), rp.ativo
FROM role_permissao rp
JOIN permissao antiga ON antiga.id = rp.permissao_id
JOIN permissao nova ON nova.nome IN (
        REPLACE(antiga.nome, 'ESTOQUE_', 'ESTOQUE_PRODUTO_'),
        REPLACE(antiga.nome, 'ESTOQUE_', 'ESTOQUE_MOVIMENTACAO_')
    )
WHERE antiga.nome IN ('ESTOQUE_CRIAR', 'ESTOQUE_EDITAR', 'ESTOQUE_LISTAR', 'ESTOQUE_EXCLUIR');
