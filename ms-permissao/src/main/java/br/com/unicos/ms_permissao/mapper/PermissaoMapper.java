package br.com.unicos.ms_permissao.mapper;

import br.com.unicos.ms_permissao.dto.permissao.PermissaoListDTO;
import br.com.unicos.ms_permissao.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_permissao.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_permissao.model.Permissao;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link Permissao}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class PermissaoMapper {

    /**
     * Converte uma entidade {@link Permissao} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos da permissão
     */
    public PermissaoResponse toResponse(Permissao entity) {
        if (entity == null)
            return null;

        return new PermissaoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }

    /**
     * Converte uma entidade {@link Permissao} em um DTO de listagem.
     * <p>
     * Observação: como a entidade possui o campo {@code nome}, mas o DTO
     * de listagem possui o campo {@code codigo}, foi adotado o mapeamento
     * de {@code nome -> codigo}.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public PermissaoListDTO toListDTO(Permissao entity) {
        if (entity == null)
            return null;

        return new PermissaoListDTO(
                entity.getId(),
                entity.getNome()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link Permissao}.
     *
     * @param request DTO contendo os dados de entrada
     * @return nova entidade preenchida com os dados informados
     */
    public Permissao toEntity(PermissaoRequest request) {
        if (request == null)
            return null;

        return Permissao.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();
    }

    /**
     * Atualiza uma entidade {@link Permissao} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity  entidade a ser atualizada
     * @param request DTO com os novos dados
     */
    public void updateEntity(Permissao entity, PermissaoRequest request) {
        if (entity == null || request == null)
            return;

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
    }

    /**
     * Converte uma lista de entidades {@link Permissao} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<PermissaoResponse> toResponseList(List<Permissao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link Permissao} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<PermissaoListDTO> toListDTOList(List<Permissao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}