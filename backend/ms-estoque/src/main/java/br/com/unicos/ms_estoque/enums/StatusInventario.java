package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa o status atual de um processo de inventário de estoque.
 * <p>
 * Permite o controle do ciclo de vida do inventário físico,
 * desde sua abertura até o encerramento ou cancelamento.
 */
@Getter
public enum StatusInventario {

    /**
     * Inventário criado, mas ainda não iniciado.
     */
    ABERTO("Inventário aberto e aguardando início da contagem"),

    /**
     * Inventário em andamento, com contagens sendo registradas.
     */
    EM_ANDAMENTO("Inventário em processo de contagem"),

    /**
     * Inventário finalizado, contagens concluídas e saldos ajustados.
     */
    FINALIZADO("Inventário concluído e encerrado"),

    /**
     * Inventário cancelado antes da finalização.
     */
    CANCELADO("Inventário cancelado sem ajuste de saldos");

    private final String descricao;

    StatusInventario(String descricao) {
        this.descricao = descricao;
    }
}
