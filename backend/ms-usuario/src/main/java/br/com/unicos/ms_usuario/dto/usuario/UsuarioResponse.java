package br.com.unicos.ms_usuario.dto.usuario;

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
        boolean emailVerificado,
        boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
