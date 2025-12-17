-- ============================================================
--  V6 - INSERT INICIAL: Role x Permissão (Tenant 0)
-- ============================================================

INSERT INTO role_permissao (
    empresa_id,
    role_id,
    permissao_id,
    criado_em,
    ativo
)
SELECT
    0,
    r.id,
    p.id,
    NOW(),
    1
FROM role r
JOIN permissao p
WHERE r.nome = 'UNICOS_ADMIN';
