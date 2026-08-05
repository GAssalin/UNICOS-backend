package br.com.unicos.ms_permissao.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de papéis (roles)
 * atribuíveis aos usuários do sistema.
 */
public record RoleRequest(

        @NotBlank(message = "O nome do papel é obrigatório.")
        @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao
) { }