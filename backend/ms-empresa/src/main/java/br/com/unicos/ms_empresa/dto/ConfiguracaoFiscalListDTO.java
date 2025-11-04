package br.com.unicos.ms_empresa.dto;

import br.com.unicos.ms_empresa.enums.TipoAmbiente;

public record ConfiguracaoFiscalListDTO(
        Long id,
        String regimeTributario,
        TipoAmbiente tipoAmbiente,
        Boolean ativo
) {}