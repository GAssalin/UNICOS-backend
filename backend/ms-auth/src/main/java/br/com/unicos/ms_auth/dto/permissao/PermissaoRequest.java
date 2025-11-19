package br.com.unicos.ms_auth.dto.permissao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de permissões.
 * <p>
 * Representa os dados necessários para cadastrar uma nova permissão
 * granular ou atualizar uma existente no sistema.
 */
public record PermissaoRequest(

        @NotBlank(message = "O código da permissão é obrigatório.")
        @Size(max = 100, message = "O código deve ter no máximo 100 caracteres.")
        String codigo,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao
) {}
