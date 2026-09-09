package br.com.unicos.ms_empresa.dto.empresa_usuario;

import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;

/**
 * DTO simplificado para listagem de usuários vinculados à empresa.
 */
public record EmpresaUsuarioListDTO(
        Long usuarioId,
        PerfilEmpresaUsuario perfil
) { }
