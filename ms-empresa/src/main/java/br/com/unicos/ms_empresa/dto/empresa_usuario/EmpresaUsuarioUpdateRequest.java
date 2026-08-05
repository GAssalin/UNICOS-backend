package br.com.unicos.ms_empresa.dto.empresa_usuario;

import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para atualização do perfil do usuário na empresa.
 */
public record EmpresaUsuarioUpdateRequest(
        @NotNull
        PerfilEmpresaUsuario perfil
) { }
