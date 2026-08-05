package br.com.unicos.ms_cliente.mapper;

import br.com.unicos.ms_cliente.dto.ClienteHistoricoStatusResponseDTO;
import br.com.unicos.ms_cliente.model.ClienteHistoricoStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pelo histórico de status do cliente.
 */
@Component
public class ClienteHistoricoStatusMapper {

    public ClienteHistoricoStatusResponseDTO toResponse(ClienteHistoricoStatus entity) {
        if (entity == null) {
            return null;
        }

        ClienteHistoricoStatusResponseDTO dto = new ClienteHistoricoStatusResponseDTO();

        dto.setId(entity.getId());
        dto.setEmpresaId(entity.getEmpresaId());
        dto.setClienteId(entity.getClienteId());
        dto.setStatusAnterior(entity.getStatusAnterior());
        dto.setStatusNovo(entity.getStatusNovo());
        dto.setMotivo(entity.getMotivo());
        dto.setAtivo(entity.getAtivo());
        dto.setCriadoEm(entity.getCriadoEm());
        dto.setAtualizadoEm(entity.getAtualizadoEm());

        return dto;
    }
}