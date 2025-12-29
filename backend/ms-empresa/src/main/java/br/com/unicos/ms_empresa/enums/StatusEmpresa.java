package br.com.unicos.ms_empresa.enums;

import lombok.Getter;

/**
 * Enum que representa o status operacional da empresa.
 * <p>
 * Controla a disponibilidade da empresa dentro do sistema,
 * impactando o acesso a funcionalidades e integrações.
 */
@Getter
public enum StatusEmpresa {

    /**
     * Empresa ativa e plenamente operacional.
     */
    ATIVA("Ativa"),

    /**
     * Empresa temporariamente suspensa.
     */
    SUSPENSA("Suspensa"),

    /**
     * Empresa encerrada ou desativada definitivamente.
     */
    ENCERRADA("Encerrada");

    private final String descricao;

    StatusEmpresa(String descricao) {
        this.descricao = descricao;
    }
}
