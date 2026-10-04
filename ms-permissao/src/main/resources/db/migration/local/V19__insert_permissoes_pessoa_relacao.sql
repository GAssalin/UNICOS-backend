-- ============================================================
--  Permissões dedicadas às relações entre pessoas (/v1/pessoas-relacoes).
--
--  Até aqui essas rotas eram protegidas pelas permissões
--  PESSOA_JURIDICA_*. Para preservar o acesso atual, cada role
--  recebe a permissão PESSOA_RELACAO_* equivalente à
--  PESSOA_JURIDICA_* que já possuía.
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
(1, 'PESSOA_RELACAO_CRIAR',   'Permite criar relações entre pessoas', NOW(), 1),
(1, 'PESSOA_RELACAO_EDITAR',  'Permite atualizar relações entre pessoas', NOW(), 1),
(1, 'PESSOA_RELACAO_LISTAR',  'Permite listar relações entre pessoas', NOW(), 1),
(1, 'PESSOA_RELACAO_EXCLUIR', 'Permite remover relações entre pessoas', NOW(), 1);

INSERT INTO role_permissao (empresa_id, role_id, permissao_id, criado_em, ativo)
SELECT rp.empresa_id, rp.role_id, nova.id, NOW(), rp.ativo
FROM role_permissao rp
JOIN permissao antiga ON antiga.id = rp.permissao_id
JOIN permissao nova ON nova.nome = REPLACE(antiga.nome, 'PESSOA_JURIDICA_', 'PESSOA_RELACAO_')
WHERE antiga.nome IN (
    'PESSOA_JURIDICA_CRIAR',
    'PESSOA_JURIDICA_EDITAR',
    'PESSOA_JURIDICA_LISTAR',
    'PESSOA_JURIDICA_EXCLUIR'
);
