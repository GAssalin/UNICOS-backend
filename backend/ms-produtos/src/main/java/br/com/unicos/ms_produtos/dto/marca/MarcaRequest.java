package br.com.unicos.ms_produtos.dto.marca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de marcas de produtos.
 *
 * <p>
 * A marca será criada ou atualizada no contexto da empresa autenticada (tenant),
 * definido automaticamente pelo backend.
 * </p>
 */
public record MarcaRequest(

        @NotBlank(message = "O nome da marca é obrigatório.")
        @Size(max = 100, message = "O nome da marca deve ter no máximo 100 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao,

        @Size(max = 100, message = "O país de origem deve ter no máximo 100 caracteres.")
        String paisOrigem
) {}
