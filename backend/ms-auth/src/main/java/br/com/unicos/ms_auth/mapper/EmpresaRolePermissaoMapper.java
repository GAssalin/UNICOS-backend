package br.com.unicos.ms_auth.mapper;

import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoListDTO;
import br.com.unicos.ms_auth.dto.empresarolepermissao.EmpresaRolePermissaoResponse;
import br.com.unicos.ms_auth.model.EmpresaRolePermissao;
import org.springframework.stereotype.Component;

@Component
public class EmpresaRolePermissaoMapper {

    public EmpresaRolePermissaoResponse toResponse(EmpresaRolePermissao e) {
        return new EmpresaRolePermissaoResponse(
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

    public EmpresaRolePermissaoListDTO toListDTO(EmpresaRolePermissao e) {
        return new EmpresaRolePermissaoListDTO(
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
