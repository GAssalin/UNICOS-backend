package br.com.unicos.ms_auth.dto.auditoria;

import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;

import java.time.LocalDateTime;

/**
 * DTO utilizado para retornar informações completas sobre
 * um evento de auditoria de acesso.
 */
public record AuditoriaAcessoResponse(
        Long id,
        String username,
        TipoAcaoAcesso acao,
        String detalhes,
        LocalDateTime dataEvento,
        String ip
) {}
