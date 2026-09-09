-- ============================================================
-- DADOS FICTÍCIOS: cliente_historico_status
-- ============================================================
INSERT INTO cliente_historico_status (
    empresa_id, cliente_id, status_anterior, status_novo, motivo,
    criado_por, criado_em, ativo
) VALUES
(1, 1, NULL, 'ATIVO', 'Cadastro inicial do cliente.', 1, NOW(), TRUE),
(1, 2, NULL, 'ATIVO', 'Cadastro inicial como cliente VIP.', 1, NOW(), TRUE),
(1, 3, NULL, 'ATIVO', 'Cadastro inicial como cliente corporativo.', 1, NOW(), TRUE),
(1, 4, NULL, 'ATIVO', 'Cadastro inicial do cliente.', 1, NOW(), TRUE),
(1, 4, 'ATIVO', 'INATIVO', 'Cliente desativado para teste de histórico de status.', 1, NOW(), TRUE),
(1, 5, NULL, 'ATIVO', 'Cadastro inicial como cliente potencial.', 1, NOW(), TRUE);
