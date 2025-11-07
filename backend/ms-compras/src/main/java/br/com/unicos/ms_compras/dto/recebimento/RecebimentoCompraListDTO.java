package br.com.unicos.ms_compras.dto.recebimento;

import br.com.unicos.ms_compras.enums.StatusRecebimentoCompra;
import br.com.unicos.ms_compras.enums.TipoRecebimento;

import java.time.LocalDate;

/**
 * DTO utilizado para listagem de processos de recebimento de compra.
 *
 * <p>Fornece uma visão resumida dos recebimentos para tabelas e consultas.</p>
 */
public record RecebimentoCompraListDTO(

        Long id,
        String codigo,
        TipoRecebimento tipoRecebimento,
        StatusRecebimentoCompra status,
        LocalDate dataRecebimento,
        LocalDate dataConclusao
) {}
