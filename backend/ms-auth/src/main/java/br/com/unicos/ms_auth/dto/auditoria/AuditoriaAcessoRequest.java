package br.com.unicos.ms_auth.dto.auditoria;

import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para registrar um novo evento de auditoria de acesso.
 * <p>
 * Contém apenas os dados necessários para criação de um registro,
 * sendo que a data/hora do evento é gerada automaticamente no backend.
 */
public record AuditoriaAcessoRequest(

        @Size(max = 100, message = "O username deve ter no máximo 100 caracteres.")
        String username,

        @NotNull(message = "O tipo de ação é obrigatório.")
        TipoAcaoAcesso acao,

        @Size(max = 255, message = "Os detalhes devem ter no máximo 255 caracteres.")
        String detalhes,

        @Size(max = 50, message = "O IP deve ter no máximo 50 caracteres.")
        String ip
) { }
