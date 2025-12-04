package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaResponse;
import br.com.unicos.ms_produtos.model.UnidadeMedida;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnidadeMedidaMapper {

    private final ModelMapper mapper;

    public UnidadeMedidaResponse toResponse(UnidadeMedida entity) {
        return mapper.map(entity, UnidadeMedidaResponse.class);
    }

    public UnidadeMedidaListDTO toListDTO(UnidadeMedida entity) {
        return mapper.map(entity, UnidadeMedidaListDTO.class);
    }
}
