package br.com.erp.ms_empresa.enums;

/**
 * Enum que representa o ambiente de operação fiscal da empresa.
 *
 * Usado para definir se a empresa está operando no ambiente de
 * HOMOLOGAÇÃO (testes) ou PRODUÇÃO (real) junto aos órgãos fiscais.
 */
public enum TipoAmbiente {
    /**
     * Ambiente de testes, utilizado para validação de integrações
     * e simulações de emissão de notas fiscais sem valor jurídico.
     */
    HOMOLOGACAO,

    /**
     * Ambiente real da SEFAZ, utilizado para emissão de documentos
     * fiscais válidos e operações efetivas da empresa.
     */
    PRODUCAO
}
