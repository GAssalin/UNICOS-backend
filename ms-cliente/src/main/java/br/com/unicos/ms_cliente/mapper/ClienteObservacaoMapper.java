package br.com.unicos.ms_cliente.mapper;

import br.com.unicos.ms_cliente.dto.cliente_observacao.ClienteObservacaoRequest;
import br.com.unicos.ms_cliente.dto.cliente_observacao.ClienteObservacaoResponse;
import br.com.unicos.ms_cliente.model.ClienteObservacao;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão de observações de cliente.
 */
@Component
public class ClienteObservacaoMapper {

    public ClienteObservacaoResponse toResponse(ClienteObservacao entity) {
        if (entity == null) {
            return null;
        }

        return new ClienteObservacaoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getClienteId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getTipo(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public ClienteObservacao toEntity(ClienteObservacaoRequest request) {
        if (request == null) {
            return null;
        }

        return ClienteObservacao.builder()
                .clienteId(request.clienteId())
                .titulo(request.titulo())
                .descricao(request.descricao())
                .tipo(request.tipo())
                .build();
    }

    public void updateEntity(ClienteObservacao entity, ClienteObservacaoRequest request) {
        if (request == null || entity == null) {
            return;
        }

        entity.setTitulo(request.titulo());
        entity.setDescricao(request.descricao());
        entity.setTipo(request.tipo());
    }
}