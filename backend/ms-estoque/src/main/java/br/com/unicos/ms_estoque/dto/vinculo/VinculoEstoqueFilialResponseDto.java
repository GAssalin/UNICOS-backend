package br.com.unicos.ms_estoque.dto.vinculo;

import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;

import java.time.LocalDate;

/**
 * DTO utilizado para retorno (leitura) de {@code VinculoEstoqueFilial}.
 * <p>
 * Exposto em listagens e detalhamento, refletindo os dados persistidos
 * e permitindo integrações por identificadores lógicos (sem FK física).
 *
 * @param id                             Identificador do vínculo.
 * @param estoqueId                  Identificador do estoque (ms-estoque).
 * @param filialId                        Identificador da filial (ms-filial).
 * @param tipoAtuacao                     Tipo de atuação do estoque na filial.
 * @param vigenciaInicio                  Início da vigência do vínculo.
 * @param vigenciaFim                     Fim da vigência (opcional).
 * @param statusVinculoEstoqueFilial  Status do vínculo Estoque x Filial.
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
