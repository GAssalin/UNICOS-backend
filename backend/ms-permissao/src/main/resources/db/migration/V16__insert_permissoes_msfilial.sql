-- ============================================================
--  INSERT INICIAIS: MS-Filial
-- ============================================================

INSERT INTO permissao (empresa_id, nome, descricao, criado_em, ativo) VALUES
(1, 'FILIAL_CRIAR', 'Permite criar filial', NOW(), 1),
(1, 'FILIAL_EDITAR', 'Permite atualizar filial', NOW(), 1),
(1, 'FILIAL_LISTAR', 'Permite listar filial', NOW(), 1),
(1, 'FILIAL_EXCLUIR', 'Permite remover filial', NOW(), 1),

(1, 'FILIAL_CONTATO_CRIAR', 'Permite criar contato de filial', NOW(), 1),
(1, 'FILIAL_CONTATO_EDITAR', 'Permite atualizar contato de filial', NOW(), 1),
(1, 'FILIAL_CONTATO_LISTAR', 'Permite listar contato de filial', NOW(), 1),
(1, 'FILIAL_CONTATO_EXCLUIR', 'Permite remover contato de filial', NOW(), 1),

(1, 'FILIAL_ENDERECO_CRIAR', 'Permite criar endereço de filial', NOW(), 1),
(1, 'FILIAL_ENDERECO_EDITAR', 'Permite atualizar endereço de filial', NOW(), 1),
(1, 'FILIAL_ENDERECO_LISTAR', 'Permite listar endereço de filial', NOW(), 1),
(1, 'FILIAL_ENDERECO_EXCLUIR', 'Permite remover endereço de filial', NOW(), 1),

(1, 'FILIAL_HORARIO_CRIAR', 'Permite criar horário de filial', NOW(), 1),
(1, 'FILIAL_HORARIO_EDITAR', 'Permite atualizar horário de filial', NOW(), 1),
(1, 'FILIAL_HORARIO_LISTAR', 'Permite listar horário de filial', NOW(), 1),
(1, 'FILIAL_HORARIO_EXCLUIR', 'Permite remover horário de filial', NOW(), 1),

(1, 'FILIAL_PARAMETRO_CRIAR', 'Permite criar parâmetro de filial', NOW(), 1),
(1, 'FILIAL_PARAMETRO_EDITAR', 'Permite atualizar parâmetro de filial', NOW(), 1),
(1, 'FILIAL_PARAMETRO_LISTAR', 'Permite listar parâmetro de filial', NOW(), 1),
(1, 'FILIAL_PARAMETRO_EXCLUIR', 'Permite remover parâmetro de filial', NOW(), 1),

(1, 'FILIAL_STATUS_HISTORICO_CRIAR', 'Permite criar histórico de status de filial', NOW(), 1),
(1, 'FILIAL_STATUS_HISTORICO_EDITAR', 'Permite atualizar histórico de status de filial', NOW(), 1),
(1, 'FILIAL_STATUS_HISTORICO_LISTAR', 'Permite listar histórico de status de filial', NOW(), 1),
(1, 'FILIAL_STATUS_HISTORICO_EXCLUIR', 'Permite remover histórico de status de filial', NOW(), 1);
