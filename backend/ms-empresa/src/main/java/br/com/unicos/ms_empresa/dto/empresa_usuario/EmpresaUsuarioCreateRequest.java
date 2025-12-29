package br.com.unicos.ms_empresa.dto.empresa_usuario;

import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação do vínculo entre usuário e empresa.
 */
public record EmpresaUsuarioCreateRequest(
        @NotNull
        Long empresaId,
        @NotNull
        Long empresaRefId,
        @NotNull
        Long usuarioId,
        @NotNull
        PerfilEmpresaUsuario perfil
) { }
