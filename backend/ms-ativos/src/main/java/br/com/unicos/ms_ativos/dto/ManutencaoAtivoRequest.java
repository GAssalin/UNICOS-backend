package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de manutenções de ativos.
 * <p>
 * Contém as informações essenciais para registrar ou agendar uma manutenção.
 */
public record ManutencaoAtivoRequest(

        /** Identificador do ativo ao qual a manutenção está vinculada. */
        @NotNull(message = "O ID do ativo é obrigatório.")
        Long ativoId,

        /** Identificador do fornecedor responsável pela execução (opcional). */
        Long fornecedorId,

        /** Data em que a manutenção foi realizada ou programada. */
        @NotNull(message = "A data da manutenção é obrigatória.")
        @PastOrPresent(message = "A data da manutenção não pode estar no futuro.")
        LocalDate dataManutencao,

        /** Tipo de manutenção: PREVENTIVA ou CORRETIVA. */
        @NotNull(message = "O tipo de manutenção é obrigatório.")
        TipoManutencao tipo,

        /** Descrição do serviço realizado ou a realizar. */
        String descricaoServico,

        /** Custo total da manutenção. */
        @DecimalMin(value = "0.0", message = "O custo deve ser positivo.")
        BigDecimal custo,

        /** Status atual da manutenção. */
        @NotNull(message = "O status da manutenção é obrigatório.")
        StatusManutencao status
) { }
