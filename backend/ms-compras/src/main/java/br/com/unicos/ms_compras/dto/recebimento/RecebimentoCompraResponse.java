package br.com.unicos.ms_compras.dto.recebimento;

import br.com.unicos.ms_compras.enums.StatusRecebimentoCompra;
import br.com.unicos.ms_compras.enums.TipoRecebimento;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de resposta detalhada do processo de recebimento de compra.
 *
 * <p>Inclui informações completas do recebimento e seus itens vinculados.</p>
 */
public record RecebimentoCompraResponse(

        Long id,
        String codigo,
        TipoRecebimento tipoRecebimento,
        StatusRecebimentoCompra status,
        LocalDate dataRecebimento,
        LocalDate dataConclusao,
        String observacao,
        Long pedidoCompraId,
        Long notaFiscalCompraId,

        /**
         * Itens recebidos neste processo.
         */
        List<RecebimentoItemResumoDTO> itens
) {}
