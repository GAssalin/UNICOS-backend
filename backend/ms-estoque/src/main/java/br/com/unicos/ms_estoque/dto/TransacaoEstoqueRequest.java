package br.com.unicos.ms_estoque.dto;

import br.com.unicos.ms_estoque.enums.TipoAjusteEstoque;
import br.com.unicos.ms_estoque.enums.TipoTransacao;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO utilizado para criação e atualização de transações de estoque.
 */
public record TransacaoEstoqueRequest(
        @NotNull TipoTransacao tipo,
        TipoAjusteEstoque tipoAjuste,
        String observacao,
        String usuarioResponsavel,
        LocalDateTime data
) {}
