-- ============================================================
--  V11 - INSERT INICIAIS: historico_preco
--  Insere registros de histórico de preços
--  Arquitetura: MULTI-TENANT (empresa_id)
-- ============================================================

INSERT INTO historico_preco (
    id,
    empresa_id,
    produto_id,
    preco_anterior,
    novo_preco,
    data_alteracao,
    motivo
)
VALUES

-- 1) Smartphone – reajuste por aumento de custo
(1, 1, 1, 2399.90, 2499.90, NOW(), 'Reajuste devido a aumento no custo do fornecedor'),

-- 2) Mouse Logitech – promoção encerrada
(2, 1, 2, 119.90, 129.90, NOW(), 'Fim da promoção do mês'),

-- 3) Tênis Nike – ajuste por nova coleção
(3, 1, 3, 329.90, 349.90, NOW(), 'Atualização de preço devido à nova coleção'),

-- 4) Monitor Dell – reajuste de mercado
(4, 1, 4, 849.90, 899.90, NOW(), 'Correção de preço por alta de componentes'),

-- 5) Poltrona Tok&Stok – preço promocional encerrado
(5, 1, 5, 1699.90, 1799.90, NOW(), 'Encerramento da campanha de descontos de outono');
