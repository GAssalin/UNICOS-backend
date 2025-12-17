-- ============================================================
--  V3 - INSERT INICIAIS: Roles (Tenant 0)
-- ============================================================

INSERT INTO role (
    empresa_id,
    nome,
    descricao,
    criado_em,
    ativo
) VALUES
(0, 'UNICOS_ADMIN', 'Administrador da plataforma UNICOS', NOW(), 1),
(0, 'ADMIN',        'Administrador da empresa', NOW(), 1),
(0, 'GERENTE',      'Gerente da empresa', NOW(), 1),
(0, 'OPERADOR',     'Operador do sistema', NOW(), 1),
(0, 'SUPORTE',      'Suporte operacional', NOW(), 1),
(0, 'LEITURA',      'Acesso somente leitura', NOW(), 1);
