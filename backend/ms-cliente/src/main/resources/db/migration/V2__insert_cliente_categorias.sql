-- ============================================================
-- DADOS FICTÍCIOS: cliente_categorias
-- ============================================================
INSERT INTO cliente_categorias (
    empresa_id, nome, descricao,
    criado_por, criado_em, ativo
) VALUES
(1, 'Cliente Padrão', 'Categoria padrão para clientes sem classificação específica.', 1, NOW(), TRUE),
(1, 'Cliente VIP', 'Clientes com relacionamento recorrente e prioridade comercial.', 1, NOW(), TRUE),
(1, 'Cliente Corporativo', 'Clientes pessoa jurídica ou com negociação empresarial.', 1, NOW(), TRUE),
(1, 'Cliente Bloqueado', 'Clientes com restrição temporária para novas vendas.', 1, NOW(), TRUE),
(1, 'Cliente Potencial', 'Clientes em fase de prospecção ou primeira negociação.', 1, NOW(), TRUE);
