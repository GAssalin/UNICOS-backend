package br.com.unicos.ms_empresa.dto;

import br.com.unicos.ms_empresa.enums.TipoAmbiente;

/**
 * DTO usado para listagem simplificada das configurações das empresas.
 */
public record ConfiguracaoEmpresaListDTO(
        Long id,
        String empresaRazaoSocial,
        String regimeTributario,
        TipoAmbiente tipoAmbiente,
        String descricaoAmbiente
) {}