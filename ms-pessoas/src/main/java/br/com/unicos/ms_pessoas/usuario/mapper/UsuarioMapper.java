package br.com.unicos.ms_pessoas.usuario.mapper;

import br.com.unicos.ms_pessoas.usuario.dto.usuario.UsuarioListDTO;
import br.com.unicos.ms_pessoas.usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_pessoas.usuario.model.Usuario;
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
                Boolean.TRUE.equals(entity.getAtivo()),
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

    public UsuarioListDTO toListDTO(Usuario entity, String roleNome) {
        if (entity == null) {
            return null;
        }

        return new UsuarioListDTO(
                entity.getId(),
                entity.getLogin(),
                entity.getEmail(),
                entity.isEmailVerificado(),
                Boolean.TRUE.equals(entity.getAtivo()),
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
