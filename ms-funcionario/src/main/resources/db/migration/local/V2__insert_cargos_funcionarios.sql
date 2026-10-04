-- ============================================================
-- DADOS FICTÍCIOS: cargos
-- ============================================================
INSERT INTO cargos (
    empresa_id, nome, descricao, papel,
    criado_por, criado_em, ativo
) VALUES
(1, 'Diretor Comercial', 'Responsável pela área comercial da empresa.', 'DIRETOR', 1, NOW(), TRUE),
(1, 'Gerente Comercial', 'Gestão da equipe de vendas e de toda a carteira de clientes.', 'GERENTE', 1, NOW(), TRUE),
(1, 'Supervisor de Vendas', 'Acompanhamento diário da equipe de vendedores.', 'SUPERVISOR', 1, NOW(), TRUE),
(1, 'Vendedor', 'Atendimento da própria carteira de clientes.', 'VENDEDOR', 1, NOW(), TRUE),
(1, 'Assistente Administrativo', 'Apoio administrativo, fora da hierarquia comercial.', 'ADMINISTRATIVO', 1, NOW(), TRUE);

-- ============================================================
-- DADOS FICTÍCIOS: funcionarios
--
-- Referências ao ms-pessoas (dados de desenvolvimento):
--   pessoa 1 / usuário 2 (gerente)  -> Gerente Comercial
--   pessoa 2 / usuário 3 (operador) -> Vendedor, subordinado ao gerente
--
-- Com isso, o usuário "operador" passa a acessar apenas os clientes da
-- própria carteira no ms-cliente, enquanto o "gerente" acessa todos.
-- ============================================================
INSERT INTO funcionarios (
    empresa_id, pessoa_id, usuario_id, matricula, cargo_id, superior_id, filial_id,
    data_admissao, data_desligamento, status,
    criado_por, criado_em, ativo
)
SELECT 1, 1, 2, 'F-0001', c.id, NULL, 1,
       '2020-01-15', NULL, 'ATIVO',
       1, NOW(), TRUE
FROM cargos c
WHERE c.empresa_id = 1 AND c.nome = 'Gerente Comercial';

INSERT INTO funcionarios (
    empresa_id, pessoa_id, usuario_id, matricula, cargo_id, superior_id, filial_id,
    data_admissao, data_desligamento, status,
    criado_por, criado_em, ativo
)
SELECT 1, 2, 3, 'F-0002', c.id, g.id, 1,
       '2022-03-01', NULL, 'ATIVO',
       1, NOW(), TRUE
FROM cargos c
JOIN funcionarios g ON g.empresa_id = 1 AND g.matricula = 'F-0001'
WHERE c.empresa_id = 1 AND c.nome = 'Vendedor';
