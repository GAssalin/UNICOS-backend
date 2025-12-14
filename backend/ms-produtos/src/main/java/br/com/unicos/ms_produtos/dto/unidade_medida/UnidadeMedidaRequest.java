package br.com.unicos.ms_produtos.dto.unidade_medida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de unidades de medida.
 *
 * <p>
 * A unidade de medida será criada ou atualizada
 * no contexto da empresa autenticada (tenant),
 * determinado automaticamente pelo backend.
 * </p>
 */
public record UnidadeMedidaRequest(

        @NotBlank(message = "O nome da unidade de medida é obrigatório.")
        @Size(max = 50, message = "O nome da unidade deve ter no máximo 50 caracteres.")
        String nome,

        @NotBlank(message = "A sigla da unidade de medida é obrigatória.")
        @Size(max = 10, message = "A sigla deve ter no máximo 10 caracteres.")
        String sigla,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao
) {}
