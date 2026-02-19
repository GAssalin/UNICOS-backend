package br.com.unicos.ms_estoque.dto.responsavel;

import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;

import java.time.LocalDate;

/**
 * DTO utilizado para retorno (leitura) de {@code ResponsavelEstoque}.
 * <p>
 * Exposto em listagens e detalhamento, refletindo os dados persistidos
 * e permitindo que outros microserviços consumam a informação por id lógico.
 *
 * @param id                         Identificador do vínculo.
 * @param estoqueId              Identificador do estoque.
 * @param responsavelId               Identificador do responsável (ms-pessoas/ms-rh).
 * @param papel                       Papel do responsável no estoque.
 * @param principal                   Indica se é o responsável principal do estoque.
 * @param vigenciaInicio              Início da vigência.
 * @param vigenciaFim                 Fim da vigência (opcional).
 * @param statusResponsavelEstoque Status do vínculo do responsável com o estoque.
 */
public record ResponsavelEstoqueResponseDto(
        Long id,
        Long estoqueId,
        Long responsavelId,
        PapelResponsavelEstoque papel,
        Boolean principal,
        LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        StatusResponsavelEstoque statusResponsavelEstoque
) { }
