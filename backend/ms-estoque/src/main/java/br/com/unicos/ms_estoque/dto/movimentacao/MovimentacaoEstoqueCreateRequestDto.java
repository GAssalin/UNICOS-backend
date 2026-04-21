package br.com.unicos.ms_estoque.dto.movimentacao;

import br.com.unicos.ms_estoque.enums.StatusMovimentacaoEstoque;
import br.com.unicos.ms_estoque.enums.TipoMovimentacaoEstoque;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO utilizado para criação de {@code MovimentacaoEstoque}.
 *
 * @param tipoMovimentacao tipo da movimentação.
 * @param estoqueOrigemId identificador do estoque de origem.
 * @param estoqueDestinoId identificador do estoque de destino.
 * @param dataMovimentacao data e hora da movimentação.
 * @param observacao observação da movimentação.
 * @param documentoReferencia documento de referência da movimentação.
 * @param usuarioResponsavelId identificador do usuário responsável.
 * @param statusMovimentacao status da movimentação.
 */
public record MovimentacaoEstoqueCreateRequestDto(

        @NotNull(message = "O tipo da movimentação é obrigatório.")
        TipoMovimentacaoEstoque tipoMovimentacao,

        Long estoqueOrigemId,

        Long estoqueDestinoId,

        @NotNull(message = "A data da movimentação é obrigatória.")
        LocalDateTime dataMovimentacao,

        @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres.")
        String observacao,

        @Size(max = 100, message = "O documento de referência deve ter no máximo 100 caracteres.")
        String documentoReferencia,

        Long usuarioResponsavelId,

        @NotNull(message = "O status da movimentação é obrigatório.")
        StatusMovimentacaoEstoque statusMovimentacao
) { }