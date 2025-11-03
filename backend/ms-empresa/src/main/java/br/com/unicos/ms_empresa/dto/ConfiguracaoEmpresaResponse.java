package br.com.unicos.ms_empresa.dto;

import br.com.unicos.ms_empresa.enums.TipoAmbiente;

/**
 * DTO usado para retorno detalhado da configuração fiscal e administrativa da empresa.
 */
public record ConfiguracaoEmpresaResponse(
        Long id,
        Long empresaId,
        String empresaRazaoSocial,
        String regimeTributario,
        String certificadoDigital,
        TipoAmbiente tipoAmbiente,
        String descricaoAmbiente
) {}