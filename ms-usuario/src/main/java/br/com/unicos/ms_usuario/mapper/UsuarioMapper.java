package br.com.unicos.ms_usuario.mapper;

import br.com.unicos.ms_usuario.dto.usuario.UsuarioListDTO;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_usuario.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario entity) {
        return toResponse(entity, null);
    }

    public UsuarioResponse toResponse(Usuario entity, String roleNome) {
        if (entity == null) {
            return null;
        }

        return new UsuarioResponse(
                entity.getId(),
                entity.getLogin(),
                entity.getPessoaId(),
                entity.getEmail(),
                entity.isEmailVerificado(),
                entity.getAtivo(),
                entity.getRoleId(),
                roleNome,
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public List<UsuarioResponse> toResponseList(List<Usuario> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toResponse).toList();
    }

    public Usuario toEntity(UsuarioRequest request) {
        if (request == null) {
            return null;
        }

        Usuario usuario = Usuario.builder()
                .login(request.login())
                .pessoaId(request.pessoaId())
                .password(request.password())
                .email(request.email())
                .roleId(request.roleId())
                .build();

        if (request.ativo() != null) {
            usuario.setAtivo(request.ativo());
        }

        return usuario;
    }

    public void updateEntityFromRequest(Usuario entity, UsuarioRequest request) {
        if (entity == null || request == null) {
            return;
        }
        if (request.login() != null) entity.setLogin(request.login());
        if (request.pessoaId() != null) entity.setPessoaId(request.pessoaId());
        if (request.password() != null) entity.setPassword(request.password());
        if (request.email() != null) entity.setEmail(request.email());
        if (request.ativo() != null) entity.setAtivo(request.ativo());
        if (request.roleId() != null) entity.setRoleId(request.roleId());
    }

    public UsuarioListDTO toListDTO(Usuario entity, String roleNome) {
        if (entity == null) {
            return null;
        }

        return new UsuarioListDTO(
                entity.getId(),
                entity.getLogin(),
                entity.getEmail(),
                entity.isEmailVerificado(),
                entity.getAtivo(),
                entity.getRoleId(),
                roleNome
        );
    }

    public List<UsuarioListDTO> toListDTOList(List<Usuario> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(entity -> toListDTO(entity, null)).toList();
    }
}
