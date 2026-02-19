-- =========================================================
-- SEED - PRODUTOS PADRÃO DO SISTEMA (EMPRESA 1)
-- =========================================================

-- Observação:
-- criado_por = 1 (system)
-- ativo = true
-- criado_em = NOW()

INSERT INTO produto (
    empresa_id,
    codigo,
    nome,
    descricao,
    tipo_produto,
    unidade_medida,
    categoria_id,
    marca_id,
    codigo_barras,
    preco_base,
    peso,
    volume,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES

-- ---------------------------------------------------------
-- PRODUTOS FÍSICOS
-- ---------------------------------------------------------
(1, 'SKU-0001', 'Coca-Cola Lata 350ml', 'Refrigerante lata 350ml', 'PRODUTO', 'UN', NULL, NULL, '7894900011517', 5.9900, 0.3500, 0.0004, 1, NOW(), NULL, NULL, TRUE),

(1, 'SKU-0002', 'Arroz Branco 5kg', 'Arroz tipo 1 pacote 5kg', 'PRODUTO', 'UN', NULL, NULL, '7896006716110', 28.9000, 5.0000, 0.0060, 1, NOW(), NULL, NULL, TRUE),

(1, 'SKU-0003', 'Notebook Core i5 8GB', 'Notebook padrão escritório', 'PRODUTO', 'UN', NULL, NULL, '7891234567890', 3499.9000, 1.8000, 0.0045, 1, NOW(), NULL, NULL, TRUE),

(1, 'SKU-0004', 'Cabo de Rede CAT6 1m', 'Cabo ethernet patch cord 1 metro', 'PRODUTO', 'UN', NULL, NULL, '7899876543210', 12.5000, 0.0800, 0.0001, 1, NOW(), NULL, NULL, TRUE),

(1, 'SKU-0005', 'Água Mineral 500ml', 'Garrafa de água mineral sem gás', 'PRODUTO', 'UN', NULL, NULL, '7891000100103', 2.5000, 0.5000, 0.0006, 1, NOW(), NULL, NULL, TRUE),


-- ---------------------------------------------------------
-- SERVIÇOS
-- ---------------------------------------------------------
(1, 'SRV-0001', 'Instalação de Equipamento', 'Serviço de instalação técnica', 'SERVICO', 'UN', NULL, NULL, NULL, 120.0000, NULL, NULL, 1, NOW(), NULL, NULL, TRUE),

(1, 'SRV-0002', 'Formatação de Computador', 'Formatação e instalação de sistema operacional', 'SERVICO', 'UN', NULL, NULL, NULL, 180.0000, NULL, NULL, 1, NOW(), NULL, NULL, TRUE),


-- ---------------------------------------------------------
-- DIGITAL
-- ---------------------------------------------------------
(1, 'DIG-0001', 'Licença Software Antivírus', 'Licença anual digital', 'DIGITAL', 'UN', NULL, NULL, NULL, 89.9000, NULL, NULL, 1, NOW(), NULL, NULL, TRUE),


-- ---------------------------------------------------------
-- ASSINATURA
-- ---------------------------------------------------------
(1, 'SUB-0001', 'Plano Mensal Suporte TI', 'Contrato recorrente de suporte técnico', 'ASSINATURA', 'UN', NULL, NULL, NULL, 250.0000, NULL, NULL, 1, NOW(), NULL, NULL, TRUE);
