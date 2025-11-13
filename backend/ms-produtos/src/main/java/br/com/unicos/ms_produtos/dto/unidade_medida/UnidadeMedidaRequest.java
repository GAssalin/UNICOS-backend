package br.com.unicos.ms_produtos.dto.unidade_medida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de unidades de medida.
 *
 * <p>
 * Contém apenas os dados necessários para entrada,
 * sem informações de auditoria ou identificadores.
 * </p>
 */
public record UnidadeMedidaRequest(
        @NotBlank(message = "O nome da unidade é obrigatório.")
        @Size(max = 50)
        String nome,
        @NotBlank(message = "A sigla é obrigatória.")
        @Size(max = 10)
        String sigla,
        @Size(max = 255)
        String descricao
) {}
