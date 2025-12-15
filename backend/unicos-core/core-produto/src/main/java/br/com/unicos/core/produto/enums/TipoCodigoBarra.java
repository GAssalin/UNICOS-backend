package br.com.unicos.core.produto.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos mais comuns de códigos de barras
 * utilizados para identificação de produtos.
 *
 * <p>
 * Determina o padrão de código aceito e auxilia na validação
 * e integração com sistemas externos.
 * </p>
 */
@Getter
public enum TipoCodigoBarra {

    /**
     * Código EAN-13 (padrão mais comum no varejo).
     */
    EAN13("EAN-13"),

    /**
     * Código EAN-8 (versão reduzida do EAN-13).
     */
    EAN8("EAN-8"),

    /**
     * Código GTIN global para rastreamento.
     */
    GTIN("GTIN"),

    /**
     * Código interno gerado pelo próprio sistema.
     */
    INTERNO("Código Interno");

    private final String descricao;

    TipoCodigoBarra(String descricao) {
        this.descricao = descricao;
    }
}
