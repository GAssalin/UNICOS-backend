package br.com.unicos.ms_compras.dto.cotacao;

import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagem de propostas de fornecedores em uma cotação.
 */
public record CotacaoFornecedorListDTO(

        Long id,
        Long fornecedorId,
        BigDecimal valorTotal,
        Integer prazoEntrega,
        StatusFornecedorCotacao status
) {}
