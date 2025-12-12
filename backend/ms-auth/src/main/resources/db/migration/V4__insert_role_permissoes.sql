-- ============================================================
--  V4 - INSERT INICIAIS: Relacionamento Role ↔ Permissão
-- ============================================================

-- ============================================================
--  ROLE ADMIN (id = 1)
--  Recebe TODAS as permissões do sistema
-- ============================================================

INSERT INTO role_permissao (role_id, permissao_id)
SELECT 1 AS role_id, id AS permissao_id
FROM permissao;


-- ============================================================
--  ROLE GERENTE (id = 2)
--  Gerencia usuários e papéis + leitura geral
-- ============================================================

INSERT INTO role_permissao (role_id, permissao_id) VALUES
(2, 1),  -- USUARIO_CRIAR
(2, 2),  -- USUARIO_EDITAR
(2, 3),  -- USUARIO_LISTAR
(2, 5),  -- ROLE_CRIAR
(2, 6),  -- ROLE_EDITAR
(2, 7),  -- ROLE_LISTAR

(2, 12), -- PERMISSAO_LISTAR

-- Permissões gerais de leitura em módulos
(2, 16), -- CONTATO_LISTAR
(2, 20), -- DOCUMENTO_LISTAR
(2, 24), -- ENDERECO_LISTAR
(2, 28), -- MUNICIPIO_LISTAR
(2, 29), -- PESSOA_LISTAR
(2, 33), -- PESSOA_FISICA_LISTAR
(2, 37), -- PESSOA_JURIDICA_LISTAR
(2, 41), -- PESSOA_RELACAO_LISTAR
(2, 45), -- TIPO_RELACAO_PESSOA_LISTAR

(2, 49), -- ATRIBUTO_PERSONALIZADO_LISTAR
(2, 53), -- CATEGORIA_LISTAR
(2, 57), -- FORNECEDOR_PRODUTO_LISTAR
(2, 61), -- HISTORICO_PRECO_LISTAR
(2, 65), -- MARCA_LISTAR
(2, 69), -- PRODUTO_LISTAR
(2, 73); -- UNIDADE_MEDIDA_LISTAR


-- ============================================================
--  ROLE OPERADOR (id = 3)
--  Apenas operações básicas e listagens
-- ============================================================

INSERT INTO role_permissao (role_id, permissao_id) VALUES
(3, 3),   -- USUARIO_LISTAR
(3, 7),   -- ROLE_LISTAR
(3, 12),  -- PERMISSAO_LISTAR

-- Listagens apenas
(3, 16),  -- CONTATO_LISTAR
(3, 20),  -- DOCUMENTO_LISTAR
(3, 24),  -- ENDERECO_LISTAR
(3, 28),  -- MUNICIPIO_LISTAR
(3, 29),  -- PESSOA_LISTAR

(3, 33),  -- PESSOA_FISICA_LISTAR
(3, 37),  -- PESSOA_JURIDICA_LISTAR
(3, 41),  -- PESSOA_RELACAO_LISTAR
(3, 45),  -- TIPO_RELACAO_PESSOA_LISTAR

(3, 49),  -- ATRIBUTO_PERSONALIZADO_LISTAR
(3, 53),  -- CATEGORIA_LISTAR
(3, 57),  -- FORNECEDOR_PRODUTO_LISTAR
(3, 61),  -- HISTORICO_PRECO_LISTAR
(3, 65),  -- MARCA_LISTAR
(3, 69),  -- PRODUTO_LISTAR
(3, 73);  -- UNIDADE_MEDIDA_LISTAR


-- ============================================================
--  ROLE SUPORTE (id = 4)
--  Similar ao OPERADOR, mas com foco diagnóstico
-- ============================================================

INSERT INTO role_permissao (role_id, permissao_id) VALUES
(4, 3),
(4, 7),
(4, 12),

(4, 16),
(4, 20),
(4, 24),
(4, 28),
(4, 29),

(4, 33),
(4, 37),
(4, 41),
(4, 45),

(4, 49),
(4, 53),
(4, 57),
(4, 61),
(4, 65),
(4, 69),
(4, 73);


-- ============================================================
--  ROLE LEITURA (id = 5)
--  Acesso mínimo: somente listagens essenciais
-- ============================================================

INSERT INTO role_permissao (role_id, permissao_id) VALUES
(5, 3),   -- listar usuários
(5, 7),   -- listar roles
(5, 12),  -- listar permissões

-- listagens básicas
(5, 29),  -- PESSOA_LISTAR
(5, 33),  -- PESSOA_FISICA_LISTAR
(5, 37),  -- PESSOA_JURIDICA_LISTAR
(5, 41),  -- PESSOA_RELACAO_LISTAR
(5, 45),  -- TIPO_RELACAO_PESSOA_LISTAR

-- produtos (somente visualização)
(5, 49),  -- ATRIBUTO_PERSONALIZADO_LISTAR
(5, 53),  -- CATEGORIA_LISTAR
(5, 57),  -- FORNECEDOR_PRODUTO_LISTAR
(5, 61),  -- HISTORICO_PRECO_LISTAR
(5, 65),  -- MARCA_LISTAR
(5, 69),  -- PRODUTO_LISTAR
(5, 73);  -- UNIDADE_MEDIDA_LISTAR
