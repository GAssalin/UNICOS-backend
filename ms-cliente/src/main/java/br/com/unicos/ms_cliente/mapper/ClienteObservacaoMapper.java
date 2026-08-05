package br.com.unicos.ms_cliente.mapper;

import br.com.unicos.ms_cliente.dto.ClienteObservacaoRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteObservacaoResponseDTO;
import br.com.unicos.ms_cliente.model.ClienteObservacao;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão de observações de cliente.
 */
@Component
public class ClienteObservacaoMapper {

    public ClienteObservacaoResponseDTO toResponse(ClienteObservacao entity) {
        if (entity == null) {
            return null;
        }

        ClienteObservacaoResponseDTO dto = new ClienteObservacaoResponseDTO();

        dto.setId(entity.getId());
        dto.setEmpresaId(entity.getEmpresaId());
        dto.setClienteId(entity.getClienteId());
        dto.setTitulo(entity.getTitulo());
        dto.setDescricao(entity.getDescricao());
        dto.setTipo(entity.getTipo());
        dto.setAtivo(entity.getAtivo());
        dto.setCriadoEm(entity.getCriadoEm());
        dto.setAtualizadoEm(entity.getAtualizadoEm());

        return dto;
    }

    public ClienteObservacao toEntity(ClienteObservacaoRequestDTO request) {
        if (request == null) {
            return null;
        }

        return ClienteObservacao.builder()
                .clienteId(request.getClienteId())
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .tipo(request.getTipo())
                .build();
    }

    public void updateEntity(ClienteObservacaoRequestDTO request, ClienteObservacao entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setTitulo(request.getTitulo());
        entity.setDescricao(request.getDescricao());
        entity.setTipo(request.getTipo());
    }
}