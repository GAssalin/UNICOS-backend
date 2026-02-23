-- V3__seed_ms_compras_pedido_e_cotacao.sql
-- Parte 2 do seed de dados fictícios do ms-compras

SET @TENANT_ID := 1;
SET @CRIADO_POR := 1;

-- =========================================================
-- PEDIDO DE COMPRA + ITENS
-- produto_id é integração lógica com ms-produto
-- =========================================================
INSERT INTO pedido_compra (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    codigo, fornecedor_id, data_emissao, data_prevista_entrega, status_pedido_compra,
    condicao_pagamento_id, observacao, subtotal, desconto, frete, total,
    aprovado_por, aprovado_em, motivo_cancelamento, observacao_cancelamento, cancelado_por, cancelado_em
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'PC-2025-0001', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-001' LIMIT 1), '2025-02-10', '2025-02-18', 'APROVADO', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30-60' LIMIT 1), 'Pedido para reposição de materiais administrativos.', 930.00, 30.00, 40.00, 940.00, 1, NOW(6), NULL, NULL, NULL, NULL),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'PC-2025-0002', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-002' LIMIT 1), '2025-02-11', '2025-02-19', 'APROVADO', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30' LIMIT 1), 'Compra de alimentos para copa.', 780.00, 0.00, 25.00, 805.00, 1, NOW(6), NULL, NULL, NULL, NULL),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'PC-2025-0003', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-003' LIMIT 1), '2025-02-12', '2025-02-21', 'EM_ANALISE', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-AV' LIMIT 1), 'Aquisição de periféricos de TI.', 1250.00, 50.00, 35.00, 1235.00, NULL, NULL, NULL, NULL, NULL, NULL),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'PC-2025-0004', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-004' LIMIT 1), '2025-02-13', '2025-02-22', 'ENVIADO_FORNECEDOR', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-15' LIMIT 1), 'Itens de limpeza para filiais.', 640.00, 20.00, 18.00, 638.00, 1, NOW(6), NULL, NULL, NULL, NULL),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'PC-2025-0005', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-005' LIMIT 1), '2025-02-14', '2025-02-24', 'RASCUNHO', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30-60-90' LIMIT 1), 'Reforço de estoque de embalagens.', 510.00, 0.00, 22.00, 532.00, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO item_pedido_compra (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    pedido_compra_id, produto_id, produto_descricao_snapshot, unidade_snapshot,
    quantidade, preco_unitario, desconto_item, total_item, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0001' LIMIT 1), 101, 'Papel A4 75g pacote c/500 folhas', 'PC', 30.0000, 32.00, 30.00, 930.00, 'Lote promocional.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0002' LIMIT 1), 102, 'Café torrado 500g', 'UN', 60.0000, 13.00, 0.00, 780.00, 'Consumo interno.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0003' LIMIT 1), 103, 'Mouse óptico USB', 'UN', 25.0000, 50.00, 0.00, 1250.00, 'Padrão corporativo.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0004' LIMIT 1), 104, 'Detergente neutro 5L', 'UN', 40.0000, 16.00, 0.00, 640.00, 'Uso operacional.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0005' LIMIT 1), 105, 'Caixa de papelão 40x30x30', 'UN', 85.0000, 6.00, 0.00, 510.00, 'Embalagem logística.');

-- =========================================================
-- COTAÇÃO + ITENS COTAÇÃO
-- =========================================================
INSERT INTO cotacao_compra (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    codigo, data_abertura, data_validade, status, observacao, pedido_compra_id
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'COT-2025-0001', '2025-02-05', '2025-02-12', 'ENCERRADA', 'Cotação encerrada após seleção de melhor proposta.', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0001' LIMIT 1)),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'COT-2025-0002', '2025-02-06', '2025-02-13', 'ABERTA', 'Cotação de itens de copa.', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0002' LIMIT 1)),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'COT-2025-0003', '2025-02-07', '2025-02-14', 'EM_COLETA', 'Cotação de periféricos.', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0003' LIMIT 1)),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'COT-2025-0004', '2025-02-08', '2025-02-15', 'CANCELADA', 'Cotação cancelada por revisão de orçamento.', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0004' LIMIT 1)),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'COT-2025-0005', '2025-02-09', '2025-02-16', 'VENCIDA', 'Cotação vencida sem proposta válida.', (SELECT id FROM pedido_compra WHERE empresa_id = @TENANT_ID AND codigo = 'PC-2025-0005' LIMIT 1));

INSERT INTO item_cotacao (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    cotacao_compra_id, produto_id, produto_descricao_snapshot, unidade_snapshot, quantidade, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0001' LIMIT 1), 101, 'Papel A4 75g pacote c/500 folhas', 'PC', 30.0000, 'Item prioritário'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0002' LIMIT 1), 102, 'Café torrado 500g', 'UN', 60.0000, 'Item copa'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0003' LIMIT 1), 103, 'Mouse óptico USB', 'UN', 25.0000, 'Item TI'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0004' LIMIT 1), 104, 'Detergente neutro 5L', 'UN', 40.0000, 'Item limpeza'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0005' LIMIT 1), 105, 'Caixa de papelão 40x30x30', 'UN', 85.0000, 'Item embalagem');

-- =========================================================
-- RESPOSTAS DOS FORNECEDORES + ITENS DAS RESPOSTAS
-- =========================================================
INSERT INTO resposta_cotacao_fornecedor (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    cotacao_compra_id, fornecedor_id, status, respondido_em, condicao_pagamento_id,
    total_proposto, frete, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0001' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-001' LIMIT 1), 'ENVIADA', NOW(6), (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30-60' LIMIT 1), 940.00, 40.00, 'Proposta selecionada.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0001' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-002' LIMIT 1), 'ENVIADA', NOW(6), (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30' LIMIT 1), 955.00, 35.00, 'Proposta alternativa.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0001' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-003' LIMIT 1), 'ENVIADA', NOW(6), (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-AV' LIMIT 1), 980.00, 30.00, 'Entrega rápida.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0001' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-004' LIMIT 1), 'RECUSADA', NOW(6), NULL, 0.00, 0.00, 'Sem disponibilidade no período.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM cotacao_compra WHERE empresa_id = @TENANT_ID AND codigo = 'COT-2025-0001' LIMIT 1), (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-005' LIMIT 1), 'PENDENTE', NULL, NULL, 0.00, 0.00, 'Aguardando retorno comercial.');

INSERT INTO item_resposta_cotacao_fornecedor (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    resposta_cotacao_fornecedor_id, item_cotacao_id, preco_unitario, desconto_item, total_item, prazo_entrega_dias, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT r.id FROM resposta_cotacao_fornecedor r JOIN fornecedor f ON f.id = r.fornecedor_id WHERE r.empresa_id = @TENANT_ID AND f.codigo = 'FORN-001' LIMIT 1), (SELECT ic.id FROM item_cotacao ic JOIN cotacao_compra c ON c.id = ic.cotacao_compra_id WHERE c.empresa_id = @TENANT_ID AND c.codigo = 'COT-2025-0001' LIMIT 1), 32.00, 20.00, 940.00, 5, 'Melhor custo total.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT r.id FROM resposta_cotacao_fornecedor r JOIN fornecedor f ON f.id = r.fornecedor_id WHERE r.empresa_id = @TENANT_ID AND f.codigo = 'FORN-002' LIMIT 1), (SELECT ic.id FROM item_cotacao ic JOIN cotacao_compra c ON c.id = ic.cotacao_compra_id WHERE c.empresa_id = @TENANT_ID AND c.codigo = 'COT-2025-0001' LIMIT 1), 31.80, 0.00, 954.00, 7, 'Preço bom com prazo maior.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT r.id FROM resposta_cotacao_fornecedor r JOIN fornecedor f ON f.id = r.fornecedor_id WHERE r.empresa_id = @TENANT_ID AND f.codigo = 'FORN-003' LIMIT 1), (SELECT ic.id FROM item_cotacao ic JOIN cotacao_compra c ON c.id = ic.cotacao_compra_id WHERE c.empresa_id = @TENANT_ID AND c.codigo = 'COT-2025-0001' LIMIT 1), 32.50, 0.00, 975.00, 3, 'Entrega mais rápida.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT r.id FROM resposta_cotacao_fornecedor r JOIN fornecedor f ON f.id = r.fornecedor_id WHERE r.empresa_id = @TENANT_ID AND f.codigo = 'FORN-004' LIMIT 1), (SELECT ic.id FROM item_cotacao ic JOIN cotacao_compra c ON c.id = ic.cotacao_compra_id WHERE c.empresa_id = @TENANT_ID AND c.codigo = 'COT-2025-0001' LIMIT 1), 0.00, 0.00, 0.00, NULL, 'Fornecedor recusou participar.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT r.id FROM resposta_cotacao_fornecedor r JOIN fornecedor f ON f.id = r.fornecedor_id WHERE r.empresa_id = @TENANT_ID AND f.codigo = 'FORN-005' LIMIT 1), (SELECT ic.id FROM item_cotacao ic JOIN cotacao_compra c ON c.id = ic.cotacao_compra_id WHERE c.empresa_id = @TENANT_ID AND c.codigo = 'COT-2025-0001' LIMIT 1), 0.00, 0.00, 0.00, NULL, 'Proposta ainda pendente.');
