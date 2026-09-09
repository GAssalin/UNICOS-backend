package br.com.unicos.ms_cliente.mapper;

import br.com.unicos.ms_cliente.dto.cliente_categoria.ClienteCategoriaRequest;
import br.com.unicos.ms_cliente.dto.cliente_categoria.ClienteCategoriaResponse;
import br.com.unicos.ms_cliente.model.ClienteCategoria;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão de ClienteCategoria.
 */
@Component
public class ClienteCategoriaMapper {

    public ClienteCategoriaResponse toResponse(ClienteCategoria entity) {
        if (entity == null) {
            return null;
        }

        return new ClienteCategoriaResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public ClienteCategoria toEntity(ClienteCategoriaRequest request) {
        if (request == null) {
            return null;
        }

        return ClienteCategoria.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();
    }

    public void updateEntity(ClienteCategoria entity, ClienteCategoriaRequest request) {
        if (request == null || entity == null) {
            return;
        }

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
    }
}