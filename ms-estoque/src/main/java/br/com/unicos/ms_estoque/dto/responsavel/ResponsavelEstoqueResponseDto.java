package br.com.unicos.ms_estoque.dto.responsavel;

import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;

import java.time.LocalDate;

/**
 * DTO utilizado para retorno de {@code ResponsavelEstoque}.
 *
 * @param id identificador do vínculo.
 * @param estoqueId identificador do estoque.
 * @param responsavelId identificador do responsável.
 * @param papel papel do responsável no estoque.
 * @param principal indica se o responsável é o principal.
 * @param vigenciaInicio data de início da vigência.
 * @param vigenciaFim data de fim da vigência.
 * @param statusResponsavelEstoque status do vínculo do responsável com o estoque.
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