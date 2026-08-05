-- ============================================================
-- DADOS FICTÍCIOS: cliente_observacoes
--
-- Ajuste os valores da coluna tipo caso o enum TipoObservacaoCliente
-- utilize nomes diferentes no projeto.
-- ============================================================
INSERT INTO cliente_observacoes (
    empresa_id, cliente_id, titulo, descricao, tipo,
    criado_por, criado_em, ativo
) VALUES
(1, 1, 'Primeiro contato', 'Cliente demonstrou interesse em conhecer os produtos disponíveis.', 'GERAL', 1, NOW(), TRUE),
(1, 2, 'Condição especial', 'Cliente VIP elegível para negociação diferenciada conforme política comercial.', 'COMERCIAL', 1, NOW(), TRUE),
(1, 3, 'Atendimento corporativo', 'Cliente corporativo solicita atendimento por vendedor dedicado.', 'COMERCIAL', 1, NOW(), TRUE),
(1, 4, 'Restrição temporária', 'Cliente foi marcado como inativo para simulação de bloqueio operacional.', 'GERAL', 1, NOW(), TRUE),
(1, 5, 'Prospecção', 'Cliente potencial ainda não realizou a primeira compra.', 'GERAL', 1, NOW(), TRUE),
(1, 2, 'Limite aprovado', 'Limite de crédito fictício aprovado para validação de venda a prazo.', 'FINANCEIRO', 1, NOW(), TRUE);
