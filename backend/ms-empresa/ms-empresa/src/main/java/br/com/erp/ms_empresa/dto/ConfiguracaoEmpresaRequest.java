package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.enums.TipoAmbiente;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para criação e atualização da configuração fiscal e administrativa da empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoEmpresaRequest {

    @NotNull(message = "O ID da empresa é obrigatório.")
    private Long empresaId;

    @Size(max = 255, message = "O regime tributário deve ter no máximo 255 caracteres.")
    private String regimeTributario;

    @Size(max = 255, message = "O certificado digital deve ter no máximo 255 caracteres.")
    private String certificadoDigital;

    @NotNull(message = "O tipo de ambiente é obrigatório.")
    private TipoAmbiente tipoAmbiente;
}