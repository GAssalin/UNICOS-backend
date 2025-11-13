package br.com.unicos.ms_produtos.dto.marca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de marcas de produtos.
 *
 * <p>
 * Contém apenas os campos necessários para entrada de dados,
 * sem informações de auditoria ou identificação.
 * </p>
 */
public record MarcaRequest(
        @NotBlank(message = "O nome da marca é obrigatório.")
        @Size(max = 100)
        String nome,
        @Size(max = 255)
        String descricao,
        @Size(max = 100)
        String paisOrigem
) {}
