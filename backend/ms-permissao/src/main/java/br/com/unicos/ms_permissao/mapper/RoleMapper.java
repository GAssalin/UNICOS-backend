package br.com.unicos.ms_permissao.mapper;

import br.com.unicos.ms_permissao.dto.role.RoleListDTO;
import br.com.unicos.ms_permissao.dto.role.RoleRequest;
import br.com.unicos.ms_permissao.dto.role.RoleResponse;
import br.com.unicos.ms_permissao.model.Role;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link Role}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class RoleMapper {

    /**
     * Converte uma entidade {@link Role} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos do papel
     */
    public RoleResponse toResponse(Role entity) {
        if (entity == null)
            return null;

        return new RoleResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }

    /**
     * Converte uma entidade {@link Role} em um DTO de listagem.
     * <p>
     * Observação: como a entidade possui o campo {@code nome}, mas o DTO
     * de listagem possui o campo {@code codigo}, foi adotado o mapeamento
     * de {@code nome -> codigo}.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public RoleListDTO toListDTO(Role entity) {
        if (entity == null)
            return null;

        return new RoleListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link Role}.
     *
     * @param request DTO contendo os dados de entrada
     * @return nova entidade preenchida com os dados informados
     */
    public Role toEntity(RoleRequest request) {
        if (request == null)
            return null;

        return Role.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();
    }

    /**
     * Atualiza uma entidade {@link Role} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity  entidade a ser atualizada
     * @param request DTO com os novos dados
     */
    public void updateEntity(Role entity, RoleRequest request) {
        if (entity == null || request == null)
            return;

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
    }

    /**
     * Converte uma lista de entidades {@link Role} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<RoleResponse> toResponseList(List<Role> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link Role} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<RoleListDTO> toListDTOList(List<Role> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}