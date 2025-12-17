package br.com.unicos.ms_auth.mapper;

import br.com.unicos.ms_auth.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_auth.model.Role;
import br.com.unicos.ms_auth.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UsuarioMapper {
    public UsuarioResponse toResponse(Usuario entity) {
        return new UsuarioResponse(
                entity.getId(),
                entity.getLogin(),
                entity.getPessoaId(),
                entity.getEmail(),
                entity.isEmailVerificado(),
                entity.getAtivo(),
                entity.getRoles()
                        .stream()
                        .map(Role::getNome)
                        .collect(Collectors.toSet()),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}
