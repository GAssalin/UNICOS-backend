package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que representa o status de um pagamento de pedido.
 * <p>
 * Usado para controlar o andamento do processo financeiro do pedido.
 */
@Getter
public enum StatusPagamento {

    /**
     * Pagamento ainda não realizado ou aguardando confirmação.
     */
    PENDENTE("Pendente"),

    /**
     * Pagamento comum em pagamentos de fornecedores (adiantamentos, parcelas, etc).
     */
    PARCIAL("Parcial"),

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

}
