package br.com.unicos.ms_compras.dto.cotacao;

import br.com.unicos.ms_compras.enums.StatusCotacao;
import br.com.unicos.ms_compras.enums.TipoCotacao;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de resposta detalhada da cotação de compra.
 *
 * <p>Inclui as informações principais da cotação e os fornecedores participantes.</p>
 */
public record CotacaoCompraResponse(

        Long id,
        String codigo,
        TipoCotacao tipoCotacao,
        StatusCotacao status,
        LocalDate dataAbertura,
        LocalDate dataValidade,
        String observacao,

        /**
         * Lista de fornecedores vinculados à cotação.
         */
        List<CotacaoFornecedorResumoDTO> fornecedores
) { }
