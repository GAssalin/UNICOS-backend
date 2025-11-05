package br.com.unicos.ms_estoque.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * DTO utilizado para criação e atualização de lotes e séries de produtos.
 */
public record LoteSerieRequest(
        @NotBlank String codigo,
        LocalDate dataValidade,
        String observacao
) {}
