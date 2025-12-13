-- ============================================================
--  V3 - INSERT INICIAIS: Roles (Globais)
-- ============================================================

INSERT INTO role (id, nome, descricao) VALUES
-- ============================================================
-- Roles institucionais (plataforma UNICOS)
-- ============================================================
(1, 'UNICOS_ADMIN', 'Administrador da plataforma UNICOS com acesso institucional e técnico'),

-- ============================================================
-- Roles administrativas (empresa)
-- ============================================================
(2, 'ADMIN', 'Administrador da empresa com acesso total às funcionalidades do sistema'),
(3, 'GERENTE', 'Gerencia usuários, equipes e configurações operacionais da empresa'),

-- ============================================================
-- Roles operacionais
-- ============================================================
(4, 'OPERADOR', 'Executa operações do dia a dia conforme permissões concedidas'),
(5, 'SUPORTE', 'Acesso restrito para suporte operacional e consultas'),
(6, 'LEITURA', 'Acesso somente leitura, sem capacidade de alteração de dados');