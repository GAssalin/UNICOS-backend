package br.com.unicos.ms_auth.mapper;

import br.com.unicos.ms_auth.dto.role_permissao.RolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.role_permissao.RolePermissaoResponse;
import br.com.unicos.ms_auth.model.RolePermissao;
import org.springframework.stereotype.Component;

@Component
public class RolePermissaoMapper {

    public RolePermissaoResponse toResponse(RolePermissao e) {
        return new RolePermissaoResponse(
                e.getId(),
                e.getEmpresaId(),
                e.getRole().getId(),
                e.getRole().getNome(),
                e.getPermissao().getId(),
                e.getPermissao().getNome(),
                e.getAtivo(),
                e.getCriadoEm(),
                e.getAtualizadoEm()
        );
    }

    public RolePermissaoListDTO toListDTO(RolePermissao e) {
        return new RolePermissaoListDTO(
                e.getId(),
                e.getEmpresaId(),
                e.getRole().getId(),
                e.getRole().getNome(),
                e.getPermissao().getId(),
                e.getPermissao().getNome(),
                e.getAtivo()
        );
    }
}
