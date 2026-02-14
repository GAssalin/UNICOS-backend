-- ============================================================
--  INSERT INICIAIS: MS-Departamento
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
(1, 'DEPARTAMENTO_CRIAR', 'Permite criar departamento', NOW(), 1),
(1, 'DEPARTAMENTO_EDITAR', 'Permite atualizar departamento', NOW(), 1),
(1, 'DEPARTAMENTO_LISTAR', 'Permite listar departamento', NOW(), 1),
(1, 'DEPARTAMENTO_EXCLUIR', 'Permite remover departamento', NOW(), 1),

(1, 'DEPARTAMENTO_RESPONSAVEL_CRIAR', 'Permite criar responsável de departamento', NOW(), 1),
(1, 'DEPARTAMENTO_RESPONSAVEL_EDITAR', 'Permite atualizar responsável de departamento', NOW(), 1),
(1, 'DEPARTAMENTO_RESPONSAVEL_LISTAR', 'Permite listar responsável de departamento', NOW(), 1),
(1, 'DEPARTAMENTO_RESPONSAVEL_EXCLUIR', 'Permite remover responsável de departamento', NOW(), 1),

(1, 'DEPARTAMENTO_VINCULO_FILIAL_CRIAR', 'Permite criar vínculo departamento x filial', NOW(), 1),
(1, 'DEPARTAMENTO_VINCULO_FILIAL_EDITAR', 'Permite atualizar vínculo departamento x filial', NOW(), 1),
(1, 'DEPARTAMENTO_VINCULO_FILIAL_LISTAR', 'Permite listar vínculo departamento x filial', NOW(), 1),
(1, 'DEPARTAMENTO_VINCULO_FILIAL_EXCLUIR', 'Permite remover vínculo departamento x filial', NOW(), 1);
