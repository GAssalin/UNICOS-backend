package br.com.unicos.ms_estoque.dto.vinculo;

import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para criação de {@code VinculoEstoqueFilial}.
 *
 * @param estoqueId identificador do estoque.
 * @param filialId identificador da filial.
 * @param tipoAtuacao tipo de atuação do estoque na filial.
 * @param vigenciaInicio data de início da vigência do vínculo.
 * @param vigenciaFim data de fim da vigência do vínculo.
 * @param statusVinculoEstoqueFilial status do vínculo entre estoque e filial.
 */
public record VinculoEstoqueFilialCreateRequestDto(

        @NotNull(message = "O identificador do estoque é obrigatório.")
        Long estoqueId,

        @NotNull(message = "O identificador da filial é obrigatório.")
        Long filialId,

        @NotNull(message = "O tipo de atuação é obrigatório.")
        TipoAtuacaoEstoque tipoAtuacao,

        @NotNull(message = "A vigência inicial é obrigatória.")
        LocalDate vigenciaInicio,

        LocalDate vigenciaFim,

        @NotNull(message = "O status do vínculo é obrigatório.")
        StatusVinculoEstoqueFilial statusVinculoEstoqueFilial
) { }