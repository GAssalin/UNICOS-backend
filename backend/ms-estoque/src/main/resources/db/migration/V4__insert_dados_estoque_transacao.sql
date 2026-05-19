-- V4__insert_dados_estoque_transacao.sql
-- Dados fictícios alternados de saldo e movimentação.
-- Compatível com 9 produtos e 8 estoques.

-- =========================================================
-- SALDOS POR PRODUTO EM ESTOQUE
-- Nem todo produto está em todo estoque.
-- =========================================================
INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 1, 1, 120.0000, 8.0000, 112.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 1, 2, 80.0000, 5.0000, 75.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 1, 5, 35.0000, 2.0000, 33.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 2, 2, 140.0000, 12.0000, 128.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 2, 3, 60.0000, 4.0000, 56.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 2, 6, 18.0000, 1.0000, 17.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 3, 1, 45.0000, 3.0000, 42.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 3, 4, 95.0000, 10.0000, 85.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 3, 7, 12.0000, 0.0000, 12.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 4, 3, 75.0000, 6.0000, 69.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 4, 5, 110.0000, 15.0000, 95.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 4, 8, 8.0000, 0.0000, 8.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 5, 4, 65.0000, 5.0000, 60.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 5, 6, 90.0000, 9.0000, 81.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 5, 9, 20.0000, 2.0000, 18.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 6, 1, 30.0000, 2.0000, 28.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 6, 7, 55.0000, 6.0000, 49.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 6, 8, 15.0000, 1.0000, 14.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 7, 2, 70.0000, 7.0000, 63.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 7, 5, 40.0000, 4.0000, 36.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 7, 9, 10.0000, 0.0000, 10.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 8, 3, 25.0000, 2.0000, 23.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 8, 6, 45.0000, 5.0000, 40.0000);

INSERT INTO estoque_produto (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    estoque_id, produto_id, quantidade_atual, quantidade_reservada, quantidade_disponivel
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 8, 8, 6.0000, 0.0000, 6.0000);

-- =========================================================
-- MOVIMENTAÇÕES DE ESTOQUE
-- =========================================================
INSERT INTO movimentacao_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    tipo_movimentacao, estoque_origem_id, estoque_destino_id, data_movimentacao, observacao, documento_referencia, usuario_responsavel_id, status_movimentacao
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 'ENTRADA', NULL, 1, '2025-05-01 09:00:00', 'Entrada inicial de produtos no estoque administrativo', 'NF-EST-001', 1, 'PROCESSADA');

INSERT INTO movimentacao_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    tipo_movimentacao, estoque_origem_id, estoque_destino_id, data_movimentacao, observacao, documento_referencia, usuario_responsavel_id, status_movimentacao
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 'ENTRADA', NULL, 2, '2025-05-03 10:30:00', 'Reposição de arroz e notebook no financeiro', 'NF-EST-002', 1, 'PROCESSADA');

INSERT INTO movimentacao_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    tipo_movimentacao, estoque_origem_id, estoque_destino_id, data_movimentacao, observacao, documento_referencia, usuario_responsavel_id, status_movimentacao
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 'SAIDA', 1, NULL, '2025-05-06 14:15:00', 'Baixa por consumo interno', 'REQ-EST-003', 2, 'PROCESSADA');

INSERT INTO movimentacao_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    tipo_movimentacao, estoque_origem_id, estoque_destino_id, data_movimentacao, observacao, documento_referencia, usuario_responsavel_id, status_movimentacao
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 'TRANSFERENCIA', 4, 3, '2025-05-08 11:00:00', 'Transferência de café para estoque comercial', 'TRF-EST-004', 3, 'PROCESSADA');

INSERT INTO movimentacao_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    tipo_movimentacao, estoque_origem_id, estoque_destino_id, data_movimentacao, observacao, documento_referencia, usuario_responsavel_id, status_movimentacao
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 'AJUSTE_ENTRADA', NULL, 5, '2025-05-10 16:40:00', 'Ajuste positivo por inventário', 'AJU-EST-005', 1, 'PROCESSADA');

INSERT INTO movimentacao_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    tipo_movimentacao, estoque_origem_id, estoque_destino_id, data_movimentacao, observacao, documento_referencia, usuario_responsavel_id, status_movimentacao
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 'TRANSFERENCIA', 5, 7, '2025-05-12 13:20:00', 'Transferência para estoque de suporte', 'TRF-EST-006', 2, 'PROCESSADA');

INSERT INTO movimentacao_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    tipo_movimentacao, estoque_origem_id, estoque_destino_id, data_movimentacao, observacao, documento_referencia, usuario_responsavel_id, status_movimentacao
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 'SAIDA', 6, NULL, '2025-05-14 17:00:00', 'Saída de licença digital', 'REQ-EST-007', 4, 'PROCESSADA');

INSERT INTO movimentacao_estoque (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    tipo_movimentacao, estoque_origem_id, estoque_destino_id, data_movimentacao, observacao, documento_referencia, usuario_responsavel_id, status_movimentacao
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 'AJUSTE_SAIDA', 8, NULL, '2025-05-16 09:45:00', 'Ajuste negativo por divergência de contagem', 'AJU-EST-008', 1, 'PENDENTE');

-- =========================================================
-- ITENS DAS MOVIMENTAÇÕES
-- =========================================================
INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 1, 1, 30.0000, 4.5000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 1, 2, 20.0000, 28.9000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 2, 2, 50.0000, 28.9000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 2, 3, 5.0000, 3500.0000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 3, 1, 10.0000, 4.5000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 3, 5, 5.0000, 2.2000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 4, 4, 15.0000, 18.0000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 4, 5, 20.0000, 2.2000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 5, 6, 10.0000, 150.0000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 5, 9, 4.0000, 99.9000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 6, 6, 8.0000, 150.0000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 6, 9, 3.0000, 99.9000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 7, 8, 2.0000, 250.0000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 8, 3, 1.0000, 3500.0000);

INSERT INTO movimentacao_estoque_item (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    movimentacao_id, produto_id, quantidade, valor_unitario
)
VALUES (1, 1, NOW(6), NULL, NULL, b'1', 8, 8, 1.0000, 250.0000);

