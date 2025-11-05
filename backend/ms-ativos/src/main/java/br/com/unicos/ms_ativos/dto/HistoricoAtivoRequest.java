package br.com.unicos.ms_ativos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

/**
 * DTO utilizado para criação ou atualização de registros de histórico de ativo.
 * <p>
 * Representa os eventos ocorridos no ciclo de vida de um ativo,
 * como transferências, manutenções, alterações de status, entre outros.
 */
public record HistoricoAtivoRequest(

        /** Identificador do ativo ao qual o histórico pertence. */
        @NotNull(message = "O ID do ativo é obrigatório.")
        Long ativoId,

        /** Data e hora em que o evento ocorreu. */
        @NotNull(message = "A data do evento é obrigatória.")
        @PastOrPresent(message = "A data do evento não pode estar no futuro.")
        LocalDateTime dataEvento,

        /** Descrição do evento (ex: 'Transferido para Filial B'). */
        @NotBlank(message = "A descrição do evento é obrigatória.")
        String descricaoEvento,

        /** Identificador do usuário responsável pelo evento (referência ao ms-pessoas). */
        Long usuarioResponsavelId
) { }
