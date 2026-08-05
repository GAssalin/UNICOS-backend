package br.com.unicos.ms_cliente.mapper;

import br.com.unicos.ms_cliente.dto.ClienteCategoriaRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteCategoriaResponseDTO;
import br.com.unicos.ms_cliente.model.ClienteCategoria;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão de ClienteCategoria.
 */
@Component
public class ClienteCategoriaMapper {

    public ClienteCategoriaResponseDTO toResponse(ClienteCategoria entity) {
        if (entity == null) {
            return null;
        }

        ClienteCategoriaResponseDTO dto = new ClienteCategoriaResponseDTO();

        dto.setId(entity.getId());
        dto.setEmpresaId(entity.getEmpresaId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setAtivo(entity.getAtivo());
        dto.setCriadoEm(entity.getCriadoEm());
        dto.setAtualizadoEm(entity.getAtualizadoEm());

        return dto;
    }

    public ClienteCategoria toEntity(ClienteCategoriaRequestDTO request) {
        if (request == null) {
            return null;
        }

        return ClienteCategoria.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .build();
    }

    public void updateEntity(ClienteCategoriaRequestDTO request, ClienteCategoria entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setNome(request.getNome());
        entity.setDescricao(request.getDescricao());
    }
}