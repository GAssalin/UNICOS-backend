package br.com.unicos.ms_permissao.mapper;

import br.com.unicos.ms_permissao.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_permissao.model.Permissao;
import org.springframework.stereotype.Component;

@Component
public class PermissaoMapper {
    public PermissaoResponse toResponse(Permissao entity) {
        return new PermissaoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }
}
