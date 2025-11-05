package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que representa o status atual de uma manutenção de ativo.
 */
@Getter
public enum StatusManutencao {

    /**
     * Manutenção registrada, aguardando execução.
     */
    ABERTA("Manutenção registrada e aguardando execução"),

    /**
     * Manutenção em execução.
     */
    EM_EXECUCAO("Manutenção em andamento"),

    /**
     * Manutenção concluída com sucesso.
     */
    CONCLUIDA("Manutenção finalizada com sucesso"),

    /**
     * Manutenção cancelada antes da execução.
     */
    CANCELADA("Manutenção cancelada pelo responsável"),

    /**
     * Manutenção adiada para data posterior.
     */
    ADIADA("Manutenção reagendada para nova data");

    private final String descricao;

    StatusManutencao(String descricao) {
        this.descricao = descricao;
    }
}
