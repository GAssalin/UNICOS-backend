package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.TipoTransferencia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de registros de transferência de ativos.
 * <p>
 * Representa o movimento de um ativo entre unidades, setores ou filiais.
 */
public record TransferenciaAtivoRequest(

        /** Identificador do ativo que está sendo transferido. */
        @NotNull(message = "O ID do ativo é obrigatório.")
        Long ativoId,

        /** Identificador da filial/unidade de origem. */
        @NotNull(message = "O ID da origem é obrigatório.")
        Long origemId,

        /** Identificador da filial/unidade de destino. */
        @NotNull(message = "O ID do destino é obrigatório.")
        Long destinoId,

        /** Tipo da transferência (INTERNA, ENTRE_FILIAIS, BAIXA, OUTROS). */
        @NotNull(message = "O tipo de transferência é obrigatório.")
        TipoTransferencia tipo,

        /** Data em que a transferência foi realizada ou registrada. */
        @NotNull(message = "A data da transferência é obrigatória.")
        @PastOrPresent(message = "A data da transferência não pode estar no futuro.")
        LocalDate dataTransferencia,

        /** Usuário responsável pela transferência (referência ao ms-pessoas). */
        Long responsavelId,

        /** Motivo ou observação da transferência. */
        String motivo
) { }
