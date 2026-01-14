package br.com.unicos.ms_filial.enums;

import lombok.Getter;

/**
 * Enum que representa o status operacional da filial.
 * <p>
 * Controla a disponibilidade da filial dentro do sistema,
 * impactando o acesso a rotinas e operações relacionadas à unidade.
 */
@Getter
public enum StatusFilial {

    /**
     * Filial ativa e plenamente operacional.
     */
    ATIVA("Ativa"),

    /**
     * Filial temporariamente suspensa (operações bloqueadas ou limitadas).
     */
    SUSPENSA("Suspensa"),

    /**
     * Filial desativada/encerrada definitivamente.
     */
    ENCERRADA("Encerrada");

    private final String descricao;

    StatusFilial(String descricao) {
        this.descricao = descricao;
    }
}
