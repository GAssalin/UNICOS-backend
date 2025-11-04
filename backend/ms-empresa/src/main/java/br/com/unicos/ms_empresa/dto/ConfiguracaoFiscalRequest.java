package br.com.unicos.ms_empresa.dto;

import br.com.unicos.ms_empresa.enums.TipoAmbiente;

public record ConfiguracaoFiscalRequest(
        String regimeTributario,
        String certificadoDigital,
        TipoAmbiente tipoAmbiente,
        Long empresaId,
        Boolean ativo
) {}