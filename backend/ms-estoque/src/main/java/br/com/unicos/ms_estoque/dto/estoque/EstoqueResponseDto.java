package br.com.unicos.ms_estoque.dto.estoque;

import br.com.unicos.ms_estoque.enums.StatusEstoque;

/**
 * DTO utilizado para retorno (leitura) de {@code Estoque}.
 * <p>
 * Exposto em listagens e detalhamento, refletindo os dados persistidos
 * e facilitando integrações com outros microserviços.
 *
 * @param id                 Identificador do estoque.
 * @param codigo             Código interno do estoque.
 * @param nome               Nome do estoque.
 * @param descricao          Descrição livre do estoque.
 * @param statusEstoque Status do estoque.
 * @param estoquePaiId  Identificador lógico do estoque pai (opcional).
 */
public record EstoqueResponseDto(
        Long id,
        String codigo,
        String nome,
        String descricao,
        StatusEstoque statusEstoque,
        Long estoquePaiId
) { }
