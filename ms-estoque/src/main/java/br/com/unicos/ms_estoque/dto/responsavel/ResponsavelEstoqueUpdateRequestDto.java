package br.com.unicos.ms_estoque.dto.responsavel;

import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para atualização de {@code ResponsavelEstoque}.
 *
 * @param estoqueId identificador do estoque.
 * @param responsavelId identificador do responsável.
 * @param papel papel do responsável no estoque.
 * @param principal indica se o responsável é o principal.
 * @param vigenciaInicio data de início da vigência.
 * @param vigenciaFim data de fim da vigência.
 * @param statusResponsavelEstoque status do vínculo do responsável com o estoque.
 */
public record ResponsavelEstoqueUpdateRequestDto(

        @NotNull(message = "O identificador do estoque é obrigatório.")
        Long estoqueId,

        @NotNull(message = "O identificador do responsável é obrigatório.")
        Long responsavelId,

        @NotNull(message = "O papel do responsável é obrigatório.")
        PapelResponsavelEstoque papel,

        @NotNull(message = "A informação de principal é obrigatória.")
        Boolean principal,

        @NotNull(message = "A vigência inicial é obrigatória.")
        LocalDate vigenciaInicio,

        LocalDate vigenciaFim,

        @NotNull(message = "O status do responsável do estoque é obrigatório.")
        StatusResponsavelEstoque statusResponsavelEstoque
) { }