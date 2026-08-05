package br.com.unicos.ms_permissao.mapper;

import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoListDTO;
import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoRequest;
import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoResponse;
import br.com.unicos.ms_permissao.dto.role_permissao.RolePermissaoResumoDTO;
import br.com.unicos.ms_permissao.model.Permissao;
import br.com.unicos.ms_permissao.model.Role;
import br.com.unicos.ms_permissao.model.RolePermissao;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link RolePermissao}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class RolePermissaoMapper {

    /**
     * Converte uma entidade {@link RolePermissao} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos da associação entre role e permissão
     */
    public RolePermissaoResponse toResponse(RolePermissao entity) {
        if (entity == null)
            return null;

        return new RolePermissaoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getRole() != null ? entity.getRole().getId() : null,
                entity.getRole() != null ? entity.getRole().getNome() : null,
                entity.getPermissao() != null ? entity.getPermissao().getId() : null,
                entity.getPermissao() != null ? entity.getPermissao().getNome() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte uma entidade {@link RolePermissao} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public RolePermissaoListDTO toListDTO(RolePermissao entity) {
        if (entity == null)
            return null;

        return new RolePermissaoListDTO(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getRole() != null ? entity.getRole().getId() : null,
                entity.getRole() != null ? entity.getRole().getNome() : null,
                entity.getPermissao() != null ? entity.getPermissao().getId() : null,
                entity.getPermissao() != null ? entity.getPermissao().getNome() : null,
                entity.getAtivo()
        );
    }

    /**
     * Converte uma entidade {@link RolePermissao} em um DTO resumido
     * para validações de autorização.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido com empresa, role e permissão
     */
    public RolePermissaoResumoDTO toResumoDTO(RolePermissao entity) {
        if (entity == null)
            return null;

        return new RolePermissaoResumoDTO(
                entity.getEmpresaId(),
                entity.getRole() != null ? entity.getRole().getNome() : null,
                entity.getPermissao() != null ? entity.getPermissao().getNome() : null
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link RolePermissao}.
     * <p>
     * Como o request contém apenas os IDs de role e permissão, é necessário
     * receber as entidades já carregadas para compor o relacionamento.
     *
     * @param request    DTO contendo os dados de entrada
     * @param role       entidade Role já carregada
     * @param permissao  entidade Permissao já carregada
     * @return nova entidade preenchida com os dados informados
     */
    public RolePermissao toEntity(RolePermissaoRequest request, Role role, Permissao permissao) {
        if (request == null)
            return null;

        return RolePermissao.builder()
                .empresaId(request.empresaId())
                .role(role)
                .permissao(permissao)
                .ativo(request.ativo())
                .build();
    }

    /**
     * Atualiza uma entidade {@link RolePermissao} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity      entidade a ser atualizada
     * @param request     DTO com os novos dados
     * @param role        entidade Role já carregada
     * @param permissao   entidade Permissao já carregada
     */
    public void updateEntity(RolePermissao entity, RolePermissaoRequest request, Role role, Permissao permissao) {
        if (entity == null || request == null)
            return;

        entity.setEmpresaId(request.empresaId());
        entity.setRole(role);
        entity.setPermissao(permissao);
        entity.setAtivo(request.ativo());
    }

    /**
     * Converte uma lista de entidades {@link RolePermissao} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<RolePermissaoResponse> toResponseList(List<RolePermissao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link RolePermissao} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<RolePermissaoListDTO> toListDTOList(List<RolePermissao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link RolePermissao} em uma lista
     * de DTOs resumidos para autorização.
     *
     * @param entities lista de entidades
     * @return lista de DTOs resumidos
     */
    public List<RolePermissaoResumoDTO> toResumoDTOList(List<RolePermissao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResumoDTO)
                .toList();
    }
}