package br.com.unicos.ms_estoque.dto.vinculo;

import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para atualização de {@code VinculoEstoqueFilial}.
 * <p>
 * Mantém o mesmo shape do create para simplificar o MVP.
 *
 * @param estoqueId                 Identificador do estoque.
 * @param filialId                       Identificador da filial.
 * @param tipoAtuacao                    Tipo de atuação do estoque na filial.
 * @param vigenciaInicio                 Início da vigência do vínculo.
 * @param vigenciaFim                    Fim da vigência (opcional).
 * @param statusVinculoEstoqueFilial Status do vínculo Estoque x Filial.
 */
public record VinculoEstoqueFilialUpdateRequestDto(
        @NotNull Long estoqueId,
        @NotNull Long filialId,
        @NotNull TipoAtuacaoEstoque tipoAtuacao,
        @NotNull LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        @NotNull StatusVinculoEstoqueFilial statusVinculoEstoqueFilial
) { }
