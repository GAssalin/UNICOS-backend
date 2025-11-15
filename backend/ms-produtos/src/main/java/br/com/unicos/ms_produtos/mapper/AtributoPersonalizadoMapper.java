package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtributoPersonalizadoMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO de resposta detalhada.
     */
    public AtributoPersonalizadoResponse toResponse(AtributoPersonalizado entity) {

        AtributoPersonalizadoResponse dto = mapper.map(entity, AtributoPersonalizadoResponse.class);

        // Ajustes que exigem navegação:
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
