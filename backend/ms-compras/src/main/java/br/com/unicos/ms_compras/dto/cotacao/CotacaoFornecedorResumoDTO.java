package br.com.unicos.ms_compras.dto.cotacao;

import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;

import java.math.BigDecimal;

/**
 * DTO resumido do fornecedor vinculado à cotação.
 *
 * <p>Utilizado apenas em listagens e respostas detalhadas de cotação.</p>
 */
public record CotacaoFornecedorResumoDTO(

        Long id,
        Long fornecedorId,
        BigDecimal valorTotal,
        Integer prazoEntrega,
        StatusFornecedorCotacao status,
        String observacao
) {}
