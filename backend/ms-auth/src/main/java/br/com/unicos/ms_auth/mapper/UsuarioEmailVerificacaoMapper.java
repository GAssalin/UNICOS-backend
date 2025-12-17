package br.com.unicos.ms_auth.mapper;

import br.com.unicos.ms_auth.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_auth.model.UsuarioEmailVerificacao;
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
