package br.com.unicos.ms_auth.dto.usuario;

import br.com.unicos.ms_auth.dto.role.RoleResponse;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO utilizado para retornar informações completas sobre um usuário,
 * incluindo seus papéis e metadados de auditoria.
 */
public record UsuarioResponse(
        Long id,
        String login,
        Long pessoaId,
        String email,
        boolean ativo,
        Set<RoleResponse> roles,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
