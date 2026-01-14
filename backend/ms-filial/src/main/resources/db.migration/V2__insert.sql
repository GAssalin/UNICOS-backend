-- ================================
--  SCHEMA MS-FILIAL - UniCoS
--  Flyway Migration V2 (Seeds)
-- ================================

-- Observação:
-- - empresa_id = tenant
-- - empresa_id_proprietaria = empresa (ms-empresa) dona das filiais
-- - IDs abaixo são fictícios e assumem banco vazio após V1

-- ============================================================
-- 1) FILIAIS
-- ============================================================
INSERT INTO filial (
    empresa_id,
    codigo,
    nome,
    cnpj,
    status_filial,
    empresa_id_proprietaria,
    endereco_filial_id,
    contato_filial_id,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 'FIL-001', 'Filial São Bernardo do Campo', '12345678000190', 'ATIVA', 1001, NULL, NULL, 1, NOW(), NULL, NULL, TRUE),
(1, 'FIL-002', 'Filial Santo André',          '12345678000271', 'ATIVA', 1001, NULL, NULL, 1, NOW(), NULL, NULL, TRUE),
(1, 'FIL-003', 'Filial Campinas',             '12345678000352', 'SUSPENSA', 1001, NULL, NULL, 1, NOW(), NULL, NULL, TRUE),
(2, 'FIL-101', 'Filial Rio de Janeiro Centro', '98765432000110', 'ATIVA', 2001, NULL, NULL, 2, NOW(), NULL, NULL, TRUE);

-- ============================================================
-- 2) ENDEREÇOS (referenciam filial_id)
-- ============================================================
INSERT INTO endereco_filial (
    empresa_id,
    filial_id,
    logradouro,
    numero,
    complemento,
    bairro,
    cidade,
    uf,
    cep,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, 'Avenida Kennedy', '1500', 'Sala 12', 'Jardim do Mar', 'São Bernardo do Campo', 'SP', '09726000', 1, NOW(), NULL, NULL, TRUE),
(1, 2, 'Rua das Figueiras', '900', NULL, 'Jardim', 'Santo André', 'SP', '09080000', 1, NOW(), NULL, NULL, TRUE),
(1, 3, 'Avenida Norte-Sul', '250', 'Bloco B', 'Cambuí', 'Campinas', 'SP', '13025000', 1, NOW(), NULL, NULL, TRUE),
(2, 4, 'Avenida Rio Branco', '1', '10º andar', 'Centro', 'Rio de Janeiro', 'RJ', '20090003', 2, NOW(), NULL, NULL, TRUE);

-- ============================================================
-- 3) CONTATOS (referenciam filial_id)
-- ============================================================
INSERT INTO contato_filial (
    empresa_id,
    filial_id,
    telefone_principal,
    telefone_secundario,
    email_principal,
    email_secundario,
    nome_responsavel,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, '11999990001', '1140000001', 'sbc@unicos.local', NULL, 'Mariana Souza', 1, NOW(), NULL, NULL, TRUE),
(1, 2, '11999990002', NULL,         'sa@unicos.local',  NULL, 'Carlos Lima',  1, NOW(), NULL, NULL, TRUE),
(1, 3, '11999990003', NULL,         'campinas@unicos.local', 'campinas2@unicos.local', 'Renata Alves', 1, NOW(), NULL, NULL, TRUE),
(2, 4, '21999990001', '2130000001', 'rj@unicos.local',  NULL, 'João Pereira',  2, NOW(), NULL, NULL, TRUE);

-- ============================================================
-- 4) VINCULAR endereco_filial_id e contato_filial_id NA TABELA FILIAL
--    (pois no INSERT inicial da filial deixamos NULL)
-- ============================================================
UPDATE filial SET endereco_filial_id = 1, contato_filial_id = 1 WHERE id = 1;
UPDATE filial SET endereco_filial_id = 2, contato_filial_id = 2 WHERE id = 2;
UPDATE filial SET endereco_filial_id = 3, contato_filial_id = 3 WHERE id = 3;
UPDATE filial SET endereco_filial_id = 4, contato_filial_id = 4 WHERE id = 4;

-- ============================================================
-- 5) HORÁRIOS DE FUNCIONAMENTO
-- ============================================================
-- Filial 1 (SBC) - Seg a Sex 08:00-18:00, Sáb 09:00-13:00, Dom fechado
INSERT INTO horario_funcionamento_filial (
    empresa_id,
    filial_id,
    dia_semana,
    hora_abertura,
    hora_fechamento,
    aberto,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, 'MONDAY',    '08:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 1, 'TUESDAY',   '08:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 1, 'WEDNESDAY', '08:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 1, 'THURSDAY',  '08:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 1, 'FRIDAY',    '08:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 1, 'SATURDAY',  '09:00:00', '13:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 1, 'SUNDAY',    NULL,       NULL,       FALSE, 1, NOW(), NULL, NULL, TRUE);

-- Filial 2 (Santo André) - Seg a Sex 09:00-18:00, Sáb/Dom fechado
INSERT INTO horario_funcionamento_filial (
    empresa_id,
    filial_id,
    dia_semana,
    hora_abertura,
    hora_fechamento,
    aberto,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 2, 'MONDAY',    '09:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 2, 'TUESDAY',   '09:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 2, 'WEDNESDAY', '09:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 2, 'THURSDAY',  '09:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 2, 'FRIDAY',    '09:00:00', '18:00:00', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 2, 'SATURDAY',  NULL,       NULL,       FALSE, 1, NOW(), NULL, NULL, TRUE),
(1, 2, 'SUNDAY',    NULL,       NULL,       FALSE, 1, NOW(), NULL, NULL, TRUE);

-- ============================================================
-- 6) PARÂMETROS DE FILIAL
-- ============================================================
INSERT INTO filial_parametro (
    empresa_id,
    filial_id,
    chave,
    valor,
    descricao,
    ativo_parametro,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, 'USA_NFE', 'true', 'Indica se a filial emite NFe.', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 1, 'PRAZO_PADRAO_ENTREGA_DIAS', '2', 'Prazo padrão de entrega (dias).', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 2, 'USA_NFE', 'true', 'Indica se a filial emite NFe.', TRUE, 1, NOW(), NULL, NULL, TRUE),
(1, 3, 'USA_NFE', 'false', 'Filial suspensa sem emissão fiscal.', TRUE, 1, NOW(), NULL, NULL, TRUE),
(2, 4, 'USA_NFE', 'true', 'Indica se a filial emite NFe.', TRUE, 2, NOW(), NULL, NULL, TRUE);

-- ============================================================
-- 7) HISTÓRICO DE STATUS
-- ============================================================
INSERT INTO filial_status_historico (
    empresa_id,
    filial_id,
    status_anterior,
    status_novo,
    data_alteracao,
    motivo,
    usuario_id,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, NULL,      'ATIVA',    NOW(), 'Cadastro inicial da filial.', 1, 1, NOW(), NULL, NULL, TRUE),
(1, 2, NULL,      'ATIVA',    NOW(), 'Cadastro inicial da filial.', 1, 1, NOW(), NULL, NULL, TRUE),
(1, 3, 'ATIVA',   'SUSPENSA', NOW(), 'Suspensão para manutenção operacional.', 1, 1, NOW(), NULL, NULL, TRUE),
(2, 4, NULL,      'ATIVA',    NOW(), 'Cadastro inicial da filial.', 2, 2, NOW(), NULL, NULL, TRUE);
