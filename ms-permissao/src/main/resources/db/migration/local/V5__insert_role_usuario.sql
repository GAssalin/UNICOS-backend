-- ============================================================
--  VINCULAR USUÁRIOS ÀS ROLES INICIAIS
-- ============================================================

INSERT INTO role_usuario (
    empresa_id,
    usuario_id,
    role_nome,
    criado_em,
    ativo
) VALUES
(1, 1, 'UNICOS_ADMIN', NOW(), TRUE),
(1, 2, 'GERENTE',      NOW(), TRUE),
(1, 3, 'OPERADOR',     NOW(), TRUE),
(1, 4, 'SUPORTE',      NOW(), TRUE),
(1, 5, 'LEITURA',      NOW(), TRUE);