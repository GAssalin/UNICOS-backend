package br.com.unicos.ms_estoque.dto.responsavel;

import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para criação de {@code ResponsavelEstoque}.
 * <p>
 * Contém os campos necessários para cadastrar o responsável no MVP,
 * mantendo a integração por identificadores lógicos.
 *
 * @param estoqueId               Identificador do estoque.
 * @param responsavelId                Identificador do responsável (ms-pessoas/ms-rh).
 * @param papel                        Papel do responsável.
 * @param principal                    Indica se é responsável principal.
 * @param vigenciaInicio               Início da vigência.
 * @param vigenciaFim                  Fim da vigência (opcional).
 * @param statusResponsavelEstoque Status do vínculo.
 */
public record ResponsavelEstoqueCreateRequestDto(
        @NotNull Long estoqueId,
        @NotNull Long responsavelId,
        @NotNull PapelResponsavelEstoque papel,
        @NotNull Boolean principal,
        @NotNull LocalDate vigenciaInicio,
        LocalDate vigenciaFim,
        @NotNull StatusResponsavelEstoque statusResponsavelEstoque
) { }
