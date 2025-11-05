package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.TipoDepreciacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para requisições de criação ou atualização
 * de depreciações aplicadas a um ativo.
 */
public record DepreciacaoAtivoRequest(

        /** Identificador do ativo ao qual a depreciação está vinculada. */
        @NotNull(message = "O ID do ativo é obrigatório.")
        Long ativoId,

        /** Tipo de depreciação (LINEAR, ACELERADA, REAVALIACAO, etc.). */
        @NotNull(message = "O tipo de depreciação é obrigatório.")
        TipoDepreciacao tipo,

        /** Data de competência da depreciação. */
        @NotNull(message = "A data de competência é obrigatória.")
        @PastOrPresent(message = "A data de competência não pode estar no futuro.")
        LocalDate dataCompetencia,

        /** Valor depreciado no período informado. */
        @NotNull(message = "O valor depreciado é obrigatório.")
        @DecimalMin(value = "0.0", message = "O valor depreciado deve ser positivo.")
        BigDecimal valorDepreciado,

        /** Saldo contábil após aplicação da depreciação. */
        @DecimalMin(value = "0.0", message = "O saldo contábil deve ser positivo.")
        BigDecimal saldoContabil
) { }
