package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.model.Municipio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MunicipioMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO detalhado.
     */
    public MunicipioResponse toResponse(Municipio entity) {
        return new MunicipioResponse(
                entity.getId(),
                entity.getNome(),
                entity.getUf(),
                entity.getCodigoIbge()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public MunicipioListDTO toListDTO(Municipio entity) {
        return new MunicipioListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getUf()
        );
    }

    /**
     * Converte o DTO de requisição para entidade.
     */
    public Municipio toEntity(MunicipioRequest request) {
        return mapper.map(request, Municipio.class);
    }
}
