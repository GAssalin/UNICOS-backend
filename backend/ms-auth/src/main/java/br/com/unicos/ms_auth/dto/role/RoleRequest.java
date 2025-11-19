package br.com.unicos.ms_auth.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * DTO utilizado para criação ou atualização de papéis (roles)
 * atribuíveis aos usuários do sistema.
 */
public record RoleRequest(

        @NotBlank(message = "O código do papel é obrigatório.")
        @Size(max = 50, message = "O código deve ter no máximo 50 caracteres.")
        String codigo,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao,

        /**
         * Lista dos IDs das permissões vinculadas ao papel.
         * Usado somente para criação/atualização.
         */
        Set<Long> permissoesIds
) {}
