package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que representa os possíveis status de uma cotação.
 */
@Getter
public enum StatusCotacao {

    /**
     * Cotação foi criada e ainda não enviada aos fornecedores.
     */
    RASCUNHO("Cotação em elaboração, ainda não enviada aos fornecedores."),

    /**
     * Cotação enviada aos fornecedores e aguardando propostas.
     */
    AGUARDANDO_RESPOSTAS("Aguardando retorno dos fornecedores."),

    /**
     * Todas as propostas foram recebidas e estão em análise.
     */
    EM_ANALISE("Propostas recebidas, em análise para seleção."),

    /**
     * Cotação finalizada com um fornecedor escolhido.
     */
    FINALIZADA("Cotação encerrada e fornecedor definido."),

    /**
     * Cotação foi cancelada antes da conclusão.
     */
    CANCELADA("Cotação cancelada e sem validade comercial.");

    private final String descricao;

    StatusCotacao(String descricao) {
        this.descricao = descricao;
    }
}
