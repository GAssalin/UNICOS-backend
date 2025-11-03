package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.enums.TipoAmbiente;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização da configuração fiscal e administrativa da empresa.
 */
public record ConfiguracaoEmpresaRequest(

        @NotNull(message = "O ID da empresa é obrigatório.")
        Long empresaId,

        @Size(max = 255, message = "O regime tributário deve ter no máximo 255 caracteres.")
        String regimeTributario,

        @Size(max = 255, message = "O certificado digital deve ter no máximo 255 caracteres.")
        String certificadoDigital,

        @NotNull(message = "O tipo de ambiente é obrigatório.")
        TipoAmbiente tipoAmbiente
) {}