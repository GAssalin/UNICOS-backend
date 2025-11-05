package br.com.unicos.ms_estoque.dto;

import br.com.unicos.ms_estoque.enums.TipoAjusteEstoque;
import br.com.unicos.ms_estoque.enums.TipoTransacao;
import java.time.LocalDateTime;

/**
 * DTO de retorno com os dados completos de uma transação de estoque.
 */
public record TransacaoEstoqueResponse(
        Long id,
        TipoTransacao tipo,
        TipoAjusteEstoque tipoAjuste,
        String observacao,
        LocalDateTime data,
        String usuarioResponsavel
) {}
