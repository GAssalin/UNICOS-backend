package br.com.unicos.ms_usuario.mapper;

import br.com.unicos.ms_usuario.dto.usuario.UsuarioListDTO;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_usuario.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class UsuarioMapper {

    /**
     * Converte a entidade {@link Usuario} para o DTO de resposta detalhada.
     *
     * @param entity entidade de usuário
     * @return DTO de resposta detalhada
     */
    public UsuarioResponse toResponse(Usuario entity) {
        if (entity == null)
            return null;

        return new UsuarioResponse(
                entity.getId(),
                entity.getLogin(),
                entity.getPessoaId(),
                entity.getEmail(),
                entity.isEmailVerificado(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte uma lista de entidades {@link Usuario} para uma lista de DTOs {@link UsuarioResponse}.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<UsuarioResponse> toResponseList(List<Usuario> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte o DTO {@link UsuarioRequest} para a entidade {@link Usuario}.
     *
     * <p>
     * Este método não trata papéis (roles), pois a entidade enviada
     * não possui relacionamento com roles.
     * </p>
     *
     * @param request DTO de entrada
     * @return entidade preenchida
     */
    public Usuario toEntity(UsuarioRequest request) {
        if (request == null)
            return null;

        Usuario usuario = Usuario.builder()
                .login(request.login())
                .pessoaId(request.pessoaId())
                .password(request.password())
                .email(request.email())
                .build();

        if (request.ativo() != null)
            usuario.setAtivo(request.ativo());

        return usuario;
    }

    /**
     * Atualiza uma entidade {@link Usuario} com base nos dados do {@link UsuarioRequest}.
     *
     * <p>
     * Atualiza apenas campos não nulos, permitindo uso em cenários de update parcial.
     * </p>
     *
     * @param entity  entidade que será atualizada
     * @param request DTO com novos dados
     */
    public void updateEntityFromRequest(Usuario entity, UsuarioRequest request) {
        if (entity == null || request == null)
            return;
        if (request.login() != null)
            entity.setLogin(request.login());
        if (request.pessoaId() != null)
            entity.setPessoaId(request.pessoaId());
        if (request.password() != null)
            entity.setPassword(request.password());
        if (request.email() != null)
            entity.setEmail(request.email());
        if (request.ativo() != null)
            entity.setAtivo(request.ativo());
    }

    /**
     * Converte a entidade {@link Usuario} para o DTO de listagem.
     *
     * <p>
     * Como a entidade enviada não possui roles, elas devem ser informadas externamente.
     * </p>
     *
     * @param entity entidade de usuário
     * @param roles conjunto de nomes de papéis do usuário
     * @return DTO de listagem
     */
    public UsuarioListDTO toListDTO(Usuario entity, Set<String> roles) {
        if (entity == null)
            return null;

        return new UsuarioListDTO(
                entity.getId(),
                entity.getLogin(),
                entity.getEmail(),
                entity.isEmailVerificado(),
                entity.getAtivo(),
                roles != null ? roles : Collections.emptySet()
        );
    }

    /**
     * Converte uma lista de entidades {@link Usuario} para DTOs de listagem.
     *
     * <p>
     * Neste overload, como não há fonte de roles, o DTO será retornado com conjunto vazio.
     * </p>
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<UsuarioListDTO> toListDTOList(List<Usuario> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .map(entity -> toListDTO(entity, Collections.emptySet()))
                .toList();
    }
}