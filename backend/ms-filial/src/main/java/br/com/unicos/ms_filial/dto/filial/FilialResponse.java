package br.com.unicos.ms_filial.dto.filial;

import br.com.unicos.ms_filial.enums.StatusFilial;

/**
 * DTO de resposta de Filial.
 */
public record FilialResponse(
        Long id,
        String codigo,
        String nome,
        String cnpj,
        StatusFilial statusFilial,
        Long empresaId,
        Long enderecoFilialId,
        Long contatoFilialId
) { }
