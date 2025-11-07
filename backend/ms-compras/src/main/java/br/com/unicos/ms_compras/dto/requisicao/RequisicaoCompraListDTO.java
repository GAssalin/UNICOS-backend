package br.com.unicos.ms_compras.dto.requisicao;

import br.com.unicos.ms_compras.enums.TipoRequisicaoCompra;

import java.time.LocalDate;

/**
 * DTO utilizado para listagem de requisições de compra.
 *
 * <p>Fornece uma visão resumida das requisições para consultas e exibição em tabelas.</p>
 */
public record RequisicaoCompraListDTO(

        Long id,
        String codigo,
        TipoRequisicaoCompra tipoRequisicao,
        LocalDate dataAbertura,
        LocalDate dataLimite,
        Long solicitanteId
) {}
