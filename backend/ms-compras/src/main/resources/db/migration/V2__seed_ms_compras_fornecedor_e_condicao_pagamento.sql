-- V2__seed_ms_compras_fornecedor_e_condicao_pagamento.sql
-- Parte 1 do seed de dados fictícios do ms-compras

SET @TENANT_ID := 1;
SET @CRIADO_POR := 1;

-- =========================================================
-- FORNECEDORES
-- =========================================================
INSERT INTO fornecedor (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    codigo, razao_social, nome_fantasia, cnpj, inscricao_estadual, email, telefone, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'FORN-001', 'Alpha Papelaria e Suprimentos LTDA', 'Alpha Suprimentos', '12.345.678/0001-90', '123.456.789.112', 'contato@alphasuprimentos.com.br', '(11) 3333-1111', 'Fornecedor focado em materiais de escritório.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'FORN-002', 'Beta Alimentos Distribuidora S/A', 'Beta Alimentos', '23.456.789/0001-01', '223.456.789.113', 'vendas@betaalimentos.com.br', '(11) 3333-2222', 'Distribuidor regional de alimentos não perecíveis.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'FORN-003', 'Gamma Tecnologia Industrial ME', 'Gamma Tech', '34.567.890/0001-12', '323.456.789.114', 'comercial@gammatech.com.br', '(11) 3333-3333', 'Fornecedor de equipamentos e periféricos.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'FORN-004', 'Delta Limpeza Profissional EIRELI', 'Delta Limpeza', '45.678.901/0001-23', '423.456.789.115', 'atendimento@deltalimpeza.com.br', '(11) 3333-4444', 'Produtos de higiene e limpeza corporativa.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'FORN-005', 'Epsilon Embalagens Industriais LTDA', 'Epsilon Embalagens', '56.789.012/0001-34', '523.456.789.116', 'comercial@epsilonembalagens.com.br', '(11) 3333-5555', 'Fornecedor de caixas, fitas e insumos logísticos.');

-- =========================================================
-- ENDEREÇOS DOS FORNECEDORES
-- =========================================================
INSERT INTO endereco_fornecedor (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    fornecedor_id, tipo, cep, logradouro, numero, complemento, bairro, cidade, uf, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-001' LIMIT 1), 'COMERCIAL', '01001-000', 'Rua das Flores', '100', 'Conjunto 12', 'Centro', 'São Paulo', 'SP', 'Matriz administrativa.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-002' LIMIT 1), 'FATURAMENTO', '30110-020', 'Av. Brasil', '2500', NULL, 'Funcionários', 'Belo Horizonte', 'MG', 'Unidade fiscal e logística.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-003' LIMIT 1), 'ENTREGA', '80010-040', 'Rua da Indústria', '900', 'Galpão B', 'Rebouças', 'Curitiba', 'PR', 'Centro de distribuição.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-004' LIMIT 1), 'COMERCIAL', '40020-060', 'Ladeira do Carmo', '88', 'Andar 3', 'Santo Antônio', 'Salvador', 'BA', 'Base regional Nordeste.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-005' LIMIT 1), 'ENTREGA', '20040-010', 'Rua do Mercado', '455', 'Galpão 2', 'Centro', 'Rio de Janeiro', 'RJ', 'Centro de distribuição Sudeste.');

-- =========================================================
-- CONTATOS DOS FORNECEDORES
-- =========================================================
INSERT INTO contato_fornecedor (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    fornecedor_id, nome, cargo, telefone, celular, email, principal, observacao
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-001' LIMIT 1), 'Carlos Mendes', 'Executivo de Contas', '(11) 3333-1111', '(11) 98888-1111', 'carlos.mendes@alphasuprimentos.com.br', b'1', 'Contato principal de negociação.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-002' LIMIT 1), 'Fernanda Lima', 'Analista Comercial', '(11) 3333-2222', '(11) 97777-2222', 'fernanda.lima@betaalimentos.com.br', b'1', 'Responsável por cotações de alimentos.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-003' LIMIT 1), 'João Ribeiro', 'Coordenador de Vendas', '(11) 3333-3333', '(11) 96666-3333', 'joao.ribeiro@gammatech.com.br', b'1', 'Especialista em itens de tecnologia.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-004' LIMIT 1), 'Paula Nascimento', 'Consultora B2B', '(11) 3333-4444', '(11) 95555-4444', 'paula.nascimento@deltalimpeza.com.br', b'1', 'Carteira higiene/limpeza.'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM fornecedor WHERE empresa_id = @TENANT_ID AND codigo = 'FORN-005' LIMIT 1), 'Ricardo Alves', 'Gerente Comercial', '(11) 3333-5555', '(11) 94444-5555', 'ricardo.alves@epsilonembalagens.com.br', b'1', 'Negocia contratos de volume.');

-- =========================================================
-- CONDIÇÕES DE PAGAMENTO + PARCELAS
-- =========================================================
INSERT INTO condicao_pagamento (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    codigo, nome, descricao, parcelado
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'CP-AV', 'À Vista', 'Pagamento integral no ato da emissão.', b'0'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'CP-15', '15 dias', 'Pagamento único em 15 dias.', b'0'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'CP-30', '30 dias', 'Pagamento único em 30 dias.', b'0'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'CP-30-60', '30/60 dias', 'Pagamento em duas parcelas de 50%.', b'1'),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', 'CP-30-60-90', '30/60/90 dias', 'Pagamento em três parcelas.', b'1');

INSERT INTO condicao_pagamento_parcela (
    empresa_id, criado_por, criado_em, atualizado_por, atualizado_em, ativo,
    condicao_pagamento_id, ordem, dias_apos_emissao, percentual
) VALUES
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-AV' LIMIT 1), 1, 0, 100.00),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30-60' LIMIT 1), 1, 30, 50.00),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30-60' LIMIT 1), 2, 60, 50.00),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30-60-90' LIMIT 1), 1, 30, 33.34),
(@TENANT_ID, @CRIADO_POR, NOW(6), NULL, NULL, b'1', (SELECT id FROM condicao_pagamento WHERE empresa_id = @TENANT_ID AND codigo = 'CP-30-60-90' LIMIT 1), 2, 60, 33.33);
