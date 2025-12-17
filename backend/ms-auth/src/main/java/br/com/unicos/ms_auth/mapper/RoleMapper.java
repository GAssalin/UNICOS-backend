package br.com.unicos.ms_auth.mapper;

import br.com.unicos.ms_auth.dto.role.RoleResponse;
import br.com.unicos.ms_auth.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleResponse toResponse(Role entity) {
        return new RoleResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }
}
