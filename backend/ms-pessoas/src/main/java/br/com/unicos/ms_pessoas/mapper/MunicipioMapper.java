package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.model.Municipio;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link Municipio}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class MunicipioMapper {

    /**
     * Converte uma entidade {@link Municipio} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos do município
     */
    public MunicipioResponse toResponse(Municipio entity) {
        if (entity == null)
            return null;

        return new MunicipioResponse(
                entity.getId(),
                entity.getNome(),
                entity.getUf(),
                entity.getCodigoIbge()
        );
    }

    /**
     * Converte uma entidade {@link Municipio} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public MunicipioListDTO toListDTO(Municipio entity) {
        if (entity == null)
            return null;

        return new MunicipioListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getUf()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link Municipio}.
     *
     * @param request DTO contendo os dados de entrada
     * @return nova entidade preenchida com os dados informados
     */
    public Municipio toEntity(MunicipioRequest request) {
        if (request == null)
            return null;

        return Municipio.builder()
                .nome(request.nome())
                .uf(request.uf())
                .codigoIbge(request.codigoIbge())
                .build();
    }

    /**
     * Atualiza uma entidade {@link Municipio} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity  entidade a ser atualizada
     * @param request DTO com os novos dados
     */
    public void updateEntity(Municipio entity, MunicipioRequest request) {
        if (entity == null || request == null)
            return;

        entity.setNome(request.nome());
        entity.setUf(request.uf());
        entity.setCodigoIbge(request.codigoIbge());
    }

    /**
     * Converte uma lista de entidades {@link Municipio} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<MunicipioResponse> toResponseList(List<Municipio> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link Municipio} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<MunicipioListDTO> toListDTOList(List<Municipio> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}