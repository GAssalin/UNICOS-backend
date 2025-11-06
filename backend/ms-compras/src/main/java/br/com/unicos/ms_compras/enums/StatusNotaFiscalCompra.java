package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que representa os possíveis status da Nota Fiscal de Compra.
 *
 * <p>Permite acompanhar o ciclo completo da nota, desde a emissão até o encerramento.</p>
 */
@Getter
public enum StatusNotaFiscalCompra {

    /**
     * Nota fiscal emitida, aguardando recebimento dos produtos.
     */
    EMITIDA("Nota fiscal emitida e aguardando entrada no estoque."),

    /**
     * Nota fiscal recebida fisicamente, mas ainda não conferida.
     */
    PENDENTE_CONFERENCIA("Nota recebida, pendente de conferência e validação fiscal."),

    /**
     * Nota fiscal conferida e validada com o pedido de compra.
     */
    CONFERIDA("Nota fiscal conferida e validada com sucesso."),

    /**
     * Nota fiscal cancelada pelo fornecedor ou internamente.
     */
    CANCELADA("Nota fiscal cancelada, sem efeitos contábeis ou fiscais."),

    /**
     * Nota fiscal encerrada e contabilizada.
     */
    FINALIZADA("Nota fiscal finalizada e integrada ao sistema contábil.");

    private final String descricao;

    StatusNotaFiscalCompra(String descricao) {
        this.descricao = descricao;
    }
}
