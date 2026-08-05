package br.com.unicos.ms_empresa.dto.empresa_contato;

import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação de um contato institucional da empresa.
 */
public record EmpresaContatoCreateRequest(
        @NotNull
        TipoContatoEmpresa tipoContato,
        @NotBlank
        String valor,
        boolean principal
) { }