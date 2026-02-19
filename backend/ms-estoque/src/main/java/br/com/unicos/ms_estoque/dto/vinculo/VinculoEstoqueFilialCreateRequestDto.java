package br.com.unicos.ms_estoque.dto.vinculo;

import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para criação de {@code VinculoEstoqueFilial}.
 * <p>
 * Contém os campos necessários para vincular um estoque a uma filial no MVP,
 * mantendo integrações por id lógico.
 *
 * @param estoqueId                 Identificador do estoque.
 * @param filialId                       Identificador da filial.
 * @param tipoAtuacao                    Tipo de atuação do estoque na filial.
 * @param vigenciaInicio                 Início da vigência do vínculo.
 * @param vigenciaFim                    Fim da vigência (opcional).
 * @param statusVinculoEstoqueFilial Status do vínculo Estoque x Filial.
 */
public record VinculoEstoqueFilialCreateRequestDto(
        @NotNull Long estoqueId,
        @NotNull Long filialId,
        @NotNull TipoAtuacaoEstoque tipoAtuacao,
        @NotNull LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        @NotNull StatusVinculoEstoqueFilial statusVinculoEstoqueFilial
) { }
