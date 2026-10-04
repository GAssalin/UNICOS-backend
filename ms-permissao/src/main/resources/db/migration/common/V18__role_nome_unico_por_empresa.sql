-- ============================================================
--  Roles pertencem a uma empresa (tenant): o nome passa a ser
--  único por empresa, permitindo que cada empresa tenha, por
--  exemplo, sua própria role "ADMIN".
-- ============================================================

ALTER TABLE role
    DROP INDEX nome,
    ADD CONSTRAINT uk_role_empresa_nome UNIQUE (empresa_id, nome);
