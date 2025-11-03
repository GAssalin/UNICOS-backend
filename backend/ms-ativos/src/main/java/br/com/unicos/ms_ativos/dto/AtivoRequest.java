package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusAtivo;
import br.com.unicos.ms_ativos.enums.TipoAtivo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO usado para criação e atualização de ativos.
 */
public record AtivoRequest(
        @NotBlank @Size(max = 100)
        String nome,

        @NotBlank @Size(max = 30)
        String codigoPatrimonial,

        @Size(max = 255)
        String descricao,

        @NotNull
        TipoAtivo tipo,

        @NotNull
        StatusAtivo status,

        @NotNull @PastOrPresent
        LocalDate dataAquisicao,

        @NotNull @DecimalMin("0.0")
        BigDecimal valorAquisicao,

        @DecimalMin("0.0")
        BigDecimal valorAtual,

        @NotNull
        Long empresaId,

        @NotNull
        Long filialId,

        Long responsavelId,
        Long localizacaoId
) {}