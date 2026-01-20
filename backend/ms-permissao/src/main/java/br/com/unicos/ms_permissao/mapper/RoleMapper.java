package br.com.unicos.ms_permissao.mapper;

import br.com.unicos.ms_permissao.dto.role.RoleResponse;
import br.com.unicos.ms_permissao.model.Role;
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
