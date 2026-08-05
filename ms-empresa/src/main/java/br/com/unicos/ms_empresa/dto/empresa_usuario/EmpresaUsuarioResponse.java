package br.com.unicos.ms_empresa.dto.empresa_usuario;

import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;

import java.time.LocalDateTime;

/**
 * DTO de resposta para vínculos de usuários com empresas.
 */
public record EmpresaUsuarioResponse(
        Long id,
        Long usuarioId,
        PerfilEmpresaUsuario perfil,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) { }