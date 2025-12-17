-- ============================================================
--  V3 - INSERT INICIAIS: Roles
-- ============================================================

INSERT INTO role (
    id, nome, descricao,
    criado_em, ativo
) VALUES

-- Institucional
(1, 'UNICOS_ADMIN', 'Administrador da plataforma UNICOS', NOW(), 1),

-- Empresa
(2, 'ADMIN',   'Administrador da empresa', NOW(), 1),
(3, 'GERENTE', 'Gerente da empresa', NOW(), 1),

-- Operacionais
(4, 'OPERADOR', 'Operador do sistema', NOW(), 1),
(5, 'SUPORTE',  'Suporte operacional', NOW(), 1),
(6, 'LEITURA',  'Acesso somente leitura', NOW(), 1);
