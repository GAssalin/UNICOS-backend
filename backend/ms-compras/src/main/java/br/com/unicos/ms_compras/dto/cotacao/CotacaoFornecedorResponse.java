package br.com.unicos.ms_compras.dto.cotacao;

import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de resposta detalhada da proposta de um fornecedor dentro de uma cotação.
 */
public record CotacaoFornecedorResponse(

        Long id,
        Long fornecedorId,
        BigDecimal valorTotal,
        Integer prazoEntrega,
        StatusFornecedorCotacao status,
        String observacao,

        /**
         * Lista de itens ofertados pelo fornecedor.
         */
        List<CotacaoItemResumoDTO> itens
) {}
