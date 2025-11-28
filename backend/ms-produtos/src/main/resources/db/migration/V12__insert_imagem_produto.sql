-- ============================================================
--  V12 - INSERT INICIAIS: imagem_produto
--  Insere 5 imagens de produtos
-- ============================================================

INSERT INTO imagem_produto (
    id,
    produto_id,
    url,
    descricao_alt,
    principal,
    ordem_exibicao,
    ativo
)
VALUES
-- 1) Smartphone – imagem principal
(1, 1, 'https://cdn.unicos.com/produtos/galaxy-a54/main.jpg',
 'Smartphone Samsung Galaxy A54', TRUE, 1, TRUE),

-- 2) Mouse Logitech – imagem principal
(2, 2, 'https://cdn.unicos.com/produtos/m280/main.jpg',
 'Mouse Sem Fio Logitech M280', TRUE, 1, TRUE),

-- 3) Tênis Nike – imagem principal
(3, 3, 'https://cdn.unicos.com/produtos/nike-rev6/main.jpg',
 'Tênis Nike Revolution 6 Preto', TRUE, 1, TRUE),

-- 4) Monitor Dell – imagem principal
(4, 4, 'https://cdn.unicos.com/produtos/dell-24/main.jpg',
 'Monitor Dell 24" Full HD', TRUE, 1, TRUE),

-- 5) Poltrona Tok&Stok – imagem principal
(5, 5, 'https://cdn.unicos.com/produtos/poltrona-premium/main.jpg',
 'Poltrona Premium Tok&Stok', TRUE, 1, TRUE);
