package br.com.unicos.ms_usuario.mapper;

import br.com.unicos.ms_usuario.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_usuario.model.UsuarioEmailVerificacao;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEmailVerificacaoMapper {
    public UsuarioEmailVerificacaoListDTO toListDTO(UsuarioEmailVerificacao entity) {
        return new UsuarioEmailVerificacaoListDTO(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.isUtilizado(),
                entity.getExpiracao()
        );
    }
}
