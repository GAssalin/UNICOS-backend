-- =========================================================
-- MS-EMPRESA | SEED INICIAL
-- Ajustado conforme entidades e enums atuais
-- =========================================================

INSERT INTO empresa (
    id,
    matriz_id,
    razao_social,
    nome_fantasia,
    cnpj,
    tipo_empresa,
    status_empresa,
    regime_tributario,
    data_abertura,
    pessoa_juridica_id,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, NULL, 'Empresa Alpha Ltda', 'Alpha', '12345678000101', 'MATRIZ', 'ATIVA',     'SIMPLES_NACIONAL', '2020-01-10', 1001, 1, NOW(), 1, NOW(), TRUE),
(2, 1,    'Empresa Beta S.A.',  'Beta',  '98765432000199', 'FILIAL', 'ATIVA',     'LUCRO_PRESUMIDO',  '2021-05-20', 1002, 1, NOW(), 1, NOW(), TRUE),
(3, 1,    'Empresa Gamma Ltda', 'Gamma', '11122233000155', 'FILIAL', 'SUSPENSA',  'SIMPLES_NACIONAL', '2019-03-15', 1003, 1, NOW(), 1, NOW(), TRUE),
(4, NULL, 'Empresa Delta ME',   'Delta', '55566677000188', 'MATRIZ', 'ATIVA',     'LUCRO_REAL',       '2022-07-01', 1004, 1, NOW(), 1, NOW(), TRUE);

INSERT INTO empresa_configuracao (
    id,
    empresa_id,
    chave,
    valor,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, 'MOEDA_PADRAO', 'BRL', 1, NOW(), 1, NOW(), TRUE),
(2, 2, 'MOEDA_PADRAO', 'BRL', 1, NOW(), 1, NOW(), TRUE),
(3, 3, 'MOEDA_PADRAO', 'USD', 1, NOW(), 1, NOW(), TRUE),
(4, 4, 'MOEDA_PADRAO', 'BRL', 1, NOW(), 1, NOW(), TRUE);

INSERT INTO empresa_contato (
    id,
    empresa_id,
    tipo_contato,
    valor,
    principal,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, 'EMAIL',    'contato@alpha.com.br', TRUE, 1, NOW(), 1, NOW(), TRUE),
(2, 2, 'EMAIL',    'contato@beta.com.br',  TRUE, 1, NOW(), 1, NOW(), TRUE),
(3, 3, 'TELEFONE', '11999999999',          TRUE, 1, NOW(), 1, NOW(), TRUE),
(4, 4, 'EMAIL',    'contato@delta.com.br', TRUE, 1, NOW(), 1, NOW(), TRUE);

INSERT INTO empresa_endereco (
    id,
    empresa_id,
    tipo_endereco,
    logradouro,
    numero,
    complemento,
    bairro,
    municipio,
    uf,
    cep,
    principal,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, 'SEDE',   'Rua das Empresas',    '100',  'Sala 01',  'Centro',     'Sao Paulo',      'SP', '01001000', TRUE, 1, NOW(), 1, NOW(), TRUE),
(2, 2, 'FILIAL', 'Avenida Corporativa', '200',  'Bloco B',  'Jardins',    'Rio de Janeiro', 'RJ', '20020000', TRUE, 1, NOW(), 1, NOW(), TRUE),
(3, 3, 'FILIAL', 'Rua Industrial',      '300',  NULL,       'Distrito',   'Campinas',       'SP', '13000000', TRUE, 1, NOW(), 1, NOW(), TRUE),
(4, 4, 'SEDE',   'Av. Paulista',        '1500', 'Conj 101', 'Bela Vista', 'Sao Paulo',      'SP', '01310000', TRUE, 1, NOW(), 1, NOW(), TRUE);

INSERT INTO empresa_parametro (
    id,
    empresa_id,
    chave,
    valor,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, 'LIMITE_USUARIOS', '50',  1, NOW(), 1, NOW(), TRUE),
(2, 2, 'LIMITE_USUARIOS', '100', 1, NOW(), 1, NOW(), TRUE),
(3, 3, 'LIMITE_USUARIOS', '25',  1, NOW(), 1, NOW(), TRUE),
(4, 4, 'LIMITE_USUARIOS', '10',  1, NOW(), 1, NOW(), TRUE);

INSERT INTO empresa_usuario (
    id,
    empresa_id,
    usuario_id,
    perfil,
    criado_por,
    criado_em,
    atualizado_por,
    atualizado_em,
    ativo
) VALUES
(1, 1, 101, 'ADMIN',       1, NOW(), 1, NOW(), TRUE),
(2, 2, 102, 'FINANCEIRO',  1, NOW(), 1, NOW(), TRUE),
(3, 3, 103, 'OPERACIONAL', 1, NOW(), 1, NOW(), TRUE),
(4, 4, 104, 'ADMIN',       1, NOW(), 1, NOW(), TRUE);
