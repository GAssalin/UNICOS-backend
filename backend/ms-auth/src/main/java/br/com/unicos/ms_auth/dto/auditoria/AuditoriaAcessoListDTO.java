package br.com.unicos.ms_auth.dto.auditoria;

import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;

import java.time.LocalDateTime;

/**
 * DTO utilizado para listagens de auditoria de acesso,
 * apresentando apenas os dados essenciais da ação registrada.
 */
public record AuditoriaAcessoListDTO(
        Long id,
        String username,
        TipoAcaoAcesso acao,
        LocalDateTime dataEvento
) {}
