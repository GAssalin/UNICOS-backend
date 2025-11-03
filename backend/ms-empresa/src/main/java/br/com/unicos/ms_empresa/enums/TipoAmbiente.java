package br.com.unicos.ms_empresa.enums;

import lombok.Getter;

/**
 * Enum que representa o ambiente de operação fiscal da empresa.
 * <p>
 * Usado para definir se a empresa está operando no ambiente de
 * HOMOLOGAÇÃO (testes) ou PRODUÇÃO (real) junto aos órgãos fiscais.
 */
@Getter
public enum TipoAmbiente {

    /**
     * Ambiente de testes, utilizado para validação de integrações
     * e simulações de emissão de notas fiscais sem valor jurídico.
     */
    HOMOLOGACAO("Homologação"),

    /**
     * Ambiente real da SEFAZ, utilizado para emissão de documentos
     * fiscais válidos e operações efetivas da empresa.
     */
    PRODUCAO("Produção");

    private final String descricao;

    TipoAmbiente(String descricao) {
        this.descricao = descricao;
    }
}
