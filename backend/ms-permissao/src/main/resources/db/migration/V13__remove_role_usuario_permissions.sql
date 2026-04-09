DELETE rp
FROM role_permissao rp
JOIN permissao p ON p.id = rp.permissao_id
WHERE p.nome LIKE 'ROLE_USUARIO_%';

DELETE FROM permissao
WHERE nome LIKE 'ROLE_USUARIO_%';
