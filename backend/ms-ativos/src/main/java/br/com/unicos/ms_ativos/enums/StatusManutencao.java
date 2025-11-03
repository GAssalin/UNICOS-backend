package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que representa os possíveis status de uma manutenção de ativo.
 * <p>
 * Define o estágio atual do processo de manutenção.
 */
@Getter
public enum StatusManutencao {

    /**
     * Manutenção programada e aguardando execução.
     */
    AGENDADA("Agendada"),

    /**
     * Manutenção concluída com sucesso.
     */
    CONCLUIDA("Concluída"),

    /**
     * Manutenção cancelada antes ou durante sua execução.
     */
    CANCELADA("Cancelada");

    private final String descricao;

    StatusManutencao(String descricao) {
        this.descricao = descricao;
    }
}
