package br.com.unicos.ms_estoque.dto.estoqueproduto;

import java.math.BigDecimal;

/**
 * DTO utilizado para retorno de dados de {@code EstoqueProduto}.
 *
 * @param id identificador do registro.
 * @param estoqueId identificador do estoque.
 * @param produtoId identificador do produto.
 * @param quantidadeAtual quantidade atual do produto no estoque.
 * @param quantidadeReservada quantidade reservada do produto no estoque.
 * @param quantidadeDisponivel quantidade disponível do produto no estoque.
 */
public record EstoqueProdutoResponseDto(
        Long id,
        Long estoqueId,
        Long produtoId,
        BigDecimal quantidadeAtual,
        BigDecimal quantidadeReservada,
        BigDecimal quantidadeDisponivel
) { }