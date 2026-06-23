-- ============================================================
-- DADOS FICTÍCIOS: clientes
--
-- Observação:
-- Este script assume que já existem registros na tabela pessoa
-- com os IDs utilizados abaixo.
-- ============================================================
INSERT INTO clientes (
    empresa_id, pessoa_id, vendedor_id, filial_id,
    codigo_interno, status, categoria_id, observacao_geral,
    permite_venda_a_prazo, limite_credito,
    criado_por, criado_em, ativo
) VALUES
(1, 1, 1, 1, 'CLI-0001', 'ATIVO', 1, 'Cliente cadastrado para testes gerais do sistema.', TRUE, 1500.00, 1, NOW(), TRUE),
(1, 2, 1, 1, 'CLI-0002', 'ATIVO', 2, 'Cliente VIP com limite de crédito ampliado.', TRUE, 7500.00, 1, NOW(), TRUE),
(1, 3, 2, 1, 'CLI-0003', 'ATIVO', 3, 'Cliente corporativo com histórico de compras recorrentes.', TRUE, 15000.00, 1, NOW(), TRUE),
(1, 4, 2, 2, 'CLI-0004', 'INATIVO', 1, 'Cliente inativo para validação de filtros por status.', FALSE, 0.00, 1, NOW(), TRUE),
(1, 5, 3, 2, 'CLI-0005', 'ATIVO', 5, 'Cliente potencial em fase de prospecção.', FALSE, 500.00, 1, NOW(), TRUE);
