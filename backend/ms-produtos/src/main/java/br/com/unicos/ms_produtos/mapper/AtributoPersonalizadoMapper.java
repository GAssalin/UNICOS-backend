package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import org.springframework.stereotype.Component;

@Component
public class AtributoPersonalizadoMapper {

    /**
     * Converte a entidade para DTO de resposta detalhada.
     */
    public AtributoPersonalizadoResponse toResponse(AtributoPersonalizado entity) {
        return new AtributoPersonalizadoResponse(
                entity.getId(),
                entity.getCategoria().getId(),
                entity.getCategoria().getNome(),
                entity.getNome()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public AtributoPersonalizadoListDTO toListDTO(AtributoPersonalizado entity) {
        return new AtributoPersonalizadoListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCategoria().getNome()
        );
    }
}
