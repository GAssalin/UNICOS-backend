ALTER TABLE usuario
    ADD COLUMN role_id BIGINT NULL AFTER email;

UPDATE usuario SET role_id = 1 WHERE id = 1;
UPDATE usuario SET role_id = 3 WHERE id = 2;
UPDATE usuario SET role_id = 4 WHERE id = 3;
UPDATE usuario SET role_id = 5 WHERE id = 4;
UPDATE usuario SET role_id = 6 WHERE id = 5;

ALTER TABLE usuario
    MODIFY COLUMN role_id BIGINT NOT NULL;

CREATE INDEX idx_usuario_role_id ON usuario (role_id);
