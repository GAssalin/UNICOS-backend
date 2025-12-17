package br.com.unicos.ms_auth.mapper;

import br.com.unicos.ms_auth.dto.auditoria.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.model.AuditoriaAcesso;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaAcessoMapper {

    public AuditoriaAcessoResponse toResponse(AuditoriaAcesso entity) {
        return new AuditoriaAcessoResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getAcao(),
                entity.getDetalhes(),
                entity.getDataEvento(),
                entity.getIp()
        );
    }
}
