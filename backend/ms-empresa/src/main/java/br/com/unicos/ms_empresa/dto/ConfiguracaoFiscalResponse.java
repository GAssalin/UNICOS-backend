package br.com.unicos.ms_empresa.dto;

import br.com.unicos.ms_empresa.enums.TipoAmbiente;

public record ConfiguracaoFiscalResponse(
        Long id,
        String regimeTributario,
        String certificadoDigital,
        TipoAmbiente tipoAmbiente,
        Boolean ativo
) {}