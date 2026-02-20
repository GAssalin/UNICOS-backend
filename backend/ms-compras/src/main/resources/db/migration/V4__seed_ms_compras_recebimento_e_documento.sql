-- V4__seed_ms_compras_recebimento_e_documento.sql
-- Parte 3 do seed de dados fictícios do ms-compras

SET @TENANT_ID := 1;
SET @CRIADO_POR := 1;

-- =========================================================
-- RECEBIMENTO + ITENS RECEBIDOS + DIVERGÊNCIA
-- =========================================================
INSERT INTO recebimento_compra (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    pedido_compra_id, fornecedor_id, data_recebimento, status, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0001' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-001' LIMIT 1), '2025-02-17', 'CONCLUIDO', 'Recebimento sem divergências.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0002' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-002' LIMIT 1), '2025-02-18', 'CONCLUIDO_COM_DIVERGENCIA', 'Recebimento com diferença de quantidade.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0003' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-003' LIMIT 1), '2025-02-19', 'EM_CONFERENCIA', 'Recebimento em análise pelo almoxarifado.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0004' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-004' LIMIT 1), '2025-02-20', 'CONCLUIDO_COM_DIVERGENCIA', 'Produto com avaria na entrega.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0005' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-005' LIMIT 1), '2025-02-21', 'CANCELADO', 'Recebimento cancelado por erro de documentação.');

INSERT INTO item_recebimento_compra (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    recebimento_compra_id, item_pedido_compra_id, quantidade_recebida, quantidade_aprovada, quantidade_recusada, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0001' LIMIT 1) LIMIT 1), (SELECT id FROM item_pedido_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0001' LIMIT 1) AND produto_id = 101 LIMIT 1), 30.0000, 30.0000, 0.0000, 'Conferência ok.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0002' LIMIT 1) LIMIT 1), (SELECT id FROM item_pedido_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0002' LIMIT 1) AND produto_id = 102 LIMIT 1), 60.0000, 58.0000, 2.0000, 'Duas unidades danificadas.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0003' LIMIT 1) LIMIT 1), (SELECT id FROM item_pedido_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0003' LIMIT 1) AND produto_id = 103 LIMIT 1), 25.0000, 25.0000, 0.0000, 'Aguardando baixa final no sistema.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0004' LIMIT 1) LIMIT 1), (SELECT id FROM item_pedido_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0004' LIMIT 1) AND produto_id = 104 LIMIT 1), 40.0000, 39.0000, 1.0000, 'Uma unidade com vazamento.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0005' LIMIT 1) LIMIT 1), (SELECT id FROM item_pedido_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0005' LIMIT 1) AND produto_id = 105 LIMIT 1), 0.0000, 0.0000, 0.0000, 'Carga não recebida por cancelamento.');

INSERT INTO divergencia_recebimento (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    item_recebimento_compra_id, tipo, descricao, quantidade_divergente
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT irc.id FROM item_recebimento_compra irc JOIN item_pedido_compra ipc ON ipc.id = irc.item_pedido_compra_id WHERE irc.empresa_id = @TENANT_ID AND ipc.produto_id = 101 LIMIT 1), 'OUTROS', 'Registro de controle: sem impacto relevante.', 0.0000),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT irc.id FROM item_recebimento_compra irc JOIN item_pedido_compra ipc ON ipc.id = irc.item_pedido_compra_id WHERE irc.empresa_id = @TENANT_ID AND ipc.produto_id = 102 LIMIT 1), 'QUANTIDADE_MENOR', 'Recebido 2 unidades a menos.', 2.0000),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT irc.id FROM item_recebimento_compra irc JOIN item_pedido_compra ipc ON ipc.id = irc.item_pedido_compra_id WHERE irc.empresa_id = @TENANT_ID AND ipc.produto_id = 103 LIMIT 1), 'OUTROS', 'Item em conferência documental.', 0.0000),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT irc.id FROM item_recebimento_compra irc JOIN item_pedido_compra ipc ON ipc.id = irc.item_pedido_compra_id WHERE irc.empresa_id = @TENANT_ID AND ipc.produto_id = 104 LIMIT 1), 'AVARIA', 'Embalagem violada em 1 unidade.', 1.0000),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT irc.id FROM item_recebimento_compra irc JOIN item_pedido_compra ipc ON ipc.id = irc.item_pedido_compra_id WHERE irc.empresa_id = @TENANT_ID AND ipc.produto_id = 105 LIMIT 1), 'ITEM_ERRADO', 'Carga divergente antes do cancelamento.', 0.0000);

-- =========================================================
-- DOCUMENTO DE ENTRADA
-- =========================================================
INSERT INTO documento_entrada (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    recebimento_compra_id, tipo_documento, numero, serie, chave_acesso, data_emissao, arquivo_ref, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0001' LIMIT 1) LIMIT 1), 'NFE', '45321', '1', '35250212345678000190550010000453211000045321', '2025-02-17', 's3://documentos/nfe/2025/02/45321.xml', 'Documento fiscal do pedido 1.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0002' LIMIT 1) LIMIT 1), 'NFE', '45322', '1', '35250212345678000190550010000453221000045322', '2025-02-18', 's3://documentos/nfe/2025/02/45322.xml', 'Documento fiscal do pedido 2.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0003' LIMIT 1) LIMIT 1), 'CTE', '77881', '2', NULL, '2025-02-19', 's3://documentos/cte/2025/02/77881.xml', 'Conhecimento de transporte de periféricos.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0004' LIMIT 1) LIMIT 1), 'NF', '99231', '3', NULL, '2025-02-20', 's3://documentos/nf/2025/02/99231.pdf', 'Nota manual de recebimento parcial.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM recebimento_compra WHERE empresa_id = @TENANT_ID AND pedido_compra_id = (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0005' LIMIT 1) LIMIT 1), 'RECIBO', 'RC-1205', '1', NULL, '2025-02-21', 's3://documentos/recibo/2025/02/RC-1205.pdf', 'Recibo gerado para registro de cancelamento.');
