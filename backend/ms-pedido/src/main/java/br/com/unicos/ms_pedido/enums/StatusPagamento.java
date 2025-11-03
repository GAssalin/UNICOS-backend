package br.com.unicos.ms_pedido.enums;

/**
 * Enum que representa o status de um pagamento de pedido.
 *
 * Usado para controlar o andamento do processo financeiro do pedido.
 */
public enum StatusPagamento {

    /**
     * Pagamento ainda não realizado ou aguardando confirmação.
     */
    PENDENTE("Pendente"),

    /**
     * Pagamento concluído com sucesso.
     */
    PAGO("Pago"),

    /**
     * Pagamento que apresentou falha ou foi recusado.
     */
    FALHOU("Falhou"),

    /**
     * Pagamento estornado após conclusão.
     */
    ESTORNADO("Estornado");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
