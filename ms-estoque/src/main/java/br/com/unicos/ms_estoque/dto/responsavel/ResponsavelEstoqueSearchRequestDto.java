package br.com.unicos.ms_estoque.dto.responsavel;

import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;

import java.time.LocalDate;

/**
 * DTO utilizado para filtros de pesquisa de {@code ResponsavelEstoque}.
 *
 * @param estoqueId filtra por estoque.
 * @param responsavelId filtra por responsável.
 * @param papel filtra por papel.
 * @param principal filtra por principal.
 * @param statusResponsavelEstoque filtra por status.
 * @param vigenteEm filtra vínculos vigentes em determinada data.
 */
public record ResponsavelEstoqueSearchRequestDto(
        Long estoqueId,
        Long responsavelId,
        PapelResponsavelEstoque papel,
        Boolean principal,
        StatusResponsavelEstoque statusResponsavelEstoque,
        LocalDate vigenteEm
) { }