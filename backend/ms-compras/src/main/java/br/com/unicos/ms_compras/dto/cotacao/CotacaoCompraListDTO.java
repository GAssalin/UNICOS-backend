package br.com.unicos.ms_compras.dto.cotacao;

import br.com.unicos.ms_compras.enums.StatusCotacao;
import br.com.unicos.ms_compras.enums.TipoCotacao;

import java.time.LocalDate;

/**
 * DTO utilizado para listagem de cotações de compra.
 *
 * <p>Fornece uma visão resumida para uso em tabelas e listagens.</p>
 */
public record CotacaoCompraListDTO(

        Long id,
        String codigo,
        TipoCotacao tipoCotacao,
        StatusCotacao status,
        LocalDate dataAbertura,
        LocalDate dataValidade
) {}
