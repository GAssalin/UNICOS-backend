package br.com.unicos.ms_estoque.dto.vinculo;

import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;

import java.time.LocalDate;

/**
 * DTO utilizado para retorno de {@code VinculoEstoqueFilial}.
 *
 * @param id identificador do vínculo.
 * @param estoqueId identificador do estoque.
 * @param filialId identificador da filial.
 * @param tipoAtuacao tipo de atuação do estoque na filial.
 * @param vigenciaInicio data de início da vigência do vínculo.
 * @param vigenciaFim data de fim da vigência do vínculo.
 * @param statusVinculoEstoqueFilial status do vínculo entre estoque e filial.
 */
public record VinculoEstoqueFilialResponseDto(
        Long id,
        Long estoqueId,
        Long filialId,
        TipoAtuacaoEstoque tipoAtuacao,
        LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        StatusVinculoEstoqueFilial statusVinculoEstoqueFilial
) { }