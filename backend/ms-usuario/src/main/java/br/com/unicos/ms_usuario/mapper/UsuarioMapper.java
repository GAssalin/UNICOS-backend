package br.com.unicos.ms_usuario.mapper;

import br.com.unicos.ms_usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_usuario.model.Usuario;
import org.springframework.stereotype.Component;

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
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}
