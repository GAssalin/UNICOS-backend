package br.com.unicos.ms_compras.dto.requisicao;

import br.com.unicos.ms_compras.enums.TipoRequisicaoCompra;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de resposta detalhada de uma requisição de compra.
 *
 * <p>Inclui informações completas e os itens solicitados.</p>
 */
public record RequisicaoCompraResponse(

        Long id,
        String codigo,
        TipoRequisicaoCompra tipoRequisicao,
        LocalDate dataAbertura,
        LocalDate dataLimite,
        Long solicitanteId,
        String observacao,

        /**
         * Itens solicitados na requisição.
         */
        List<RequisicaoItemResumoDTO> itens
) {}
