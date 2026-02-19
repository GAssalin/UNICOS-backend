package br.com.unicos.ms_estoque.dto.responsavel;

import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;

import java.time.LocalDate;

/**
 * DTO de filtro/pesquisa para listagem de {@code ResponsavelEstoque}.
 * <p>
 * Útil para endpoints de consulta com paginação.
 *
 * @param estoqueId               Filtra por estoque (opcional).
 * @param responsavelId                Filtra por responsável (opcional).
 * @param papel                        Filtra por papel (opcional).
 * @param principal                    Filtra por principal (opcional).
 * @param statusResponsavelEstoque Filtra por status (opcional).
 * @param vigenteEm                    Quando informado, filtra vínculos vigentes na data.
 */
public record ResponsavelEstoqueSearchRequestDto(
        Long estoqueId,
        Long responsavelId,
        PapelResponsavelEstoque papel,
        Boolean principal,
        StatusResponsavelEstoque statusResponsavelEstoque,
        LocalDate vigenteEm
) { }
