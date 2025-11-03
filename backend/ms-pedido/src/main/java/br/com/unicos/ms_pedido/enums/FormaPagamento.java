package br.com.unicos.ms_pedido.enums;

/**
 * Enum que representa as formas de pagamento disponíveis para o pedido.
 *
 * Define o método utilizado pelo cliente para efetuar o pagamento.
 */
public enum FormaPagamento {

    /**
     * Pagamento realizado via cartão de crédito ou débito.
     */
    CARTAO("Cartão"),

    /**
     * Pagamento realizado via PIX.
     */
    PIX("PIX"),

    /**
     * Pagamento realizado por boleto bancário.
     */
    BOLETO("Boleto"),

    /**
     * Pagamento em dinheiro no ato da entrega ou retirada.
     */
    DINHEIRO("Dinheiro");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
