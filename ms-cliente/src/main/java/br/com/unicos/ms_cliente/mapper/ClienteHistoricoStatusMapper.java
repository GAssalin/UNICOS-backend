package br.com.unicos.ms_cliente.mapper;

import br.com.unicos.ms_cliente.dto.cliente_historico_status.ClienteHistoricoStatusResponse;
import br.com.unicos.ms_cliente.model.ClienteHistoricoStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pelo histórico de status do cliente.
 */
@Component
public class ClienteHistoricoStatusMapper {

    public ClienteHistoricoStatusResponse toResponse(ClienteHistoricoStatus entity) {
        if (entity == null) {
            return null;
        }

        return new ClienteHistoricoStatusResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getClienteId(),
                entity.getStatusAnterior(),
                entity.getStatusNovo(),
                entity.getMotivo(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}