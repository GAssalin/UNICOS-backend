package br.com.unicos.ms_estoque.dto.estoque;

import br.com.unicos.ms_estoque.enums.StatusEstoque;

/**
 * DTO de filtro/pesquisa para listagem de {@code Estoque}.
 * <p>
 * Útil para endpoints de consulta com paginação.
 *
 * @param codigo             Código interno (opcional, busca exata ou por prefixo conforme implementação).
 * @param nome               Nome (opcional, busca por contains/like conforme implementação).
 * @param statusEstoque Status do estoque (opcional).
 * @param estoquePaiId  Filtra por estoque pai (opcional).
 * @param apenasRaiz         Quando {@code true}, retorna somente estoques sem pai.
 */
public record EstoqueSearchRequestDto(
        String codigo,
        String nome,
        StatusEstoque statusEstoque,
        Long estoquePaiId,
        Boolean apenasRaiz
) { }
