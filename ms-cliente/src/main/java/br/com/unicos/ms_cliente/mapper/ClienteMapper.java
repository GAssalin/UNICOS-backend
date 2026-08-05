package br.com.unicos.ms_cliente.mapper;

import br.com.unicos.ms_cliente.dto.ClienteRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteResponseDTO;
import br.com.unicos.ms_cliente.model.Cliente;
import br.com.unicos.ms_cliente.client.PessoasService;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link Cliente} e seus DTOs.
 */
@Component
public class ClienteMapper {

    private final PessoasService pessoasService;

    public ClienteMapper(PessoasService pessoasService) {
        this.pessoasService = pessoasService;
    }

    public ClienteResponseDTO toResponse(Cliente entity) {
        if (entity == null)
            return null;

        ClienteResponseDTO dto = new ClienteResponseDTO();

        dto.setId(entity.getId());
        dto.setEmpresaId(entity.getEmpresaId());
        dto.setPessoaId(entity.getPessoaId());
        dto.setVendedorId(entity.getVendedorId());
        dto.setNomeVendedor(pessoasService.buscarPorId(entity.getVendedorId()).nome());
        dto.setFilialId(entity.getFilialId());
        dto.setCodigoInterno(entity.getCodigoInterno());
        dto.setStatus(entity.getStatus());

        if (entity.getCategoria() != null) {
            dto.setCategoriaId(entity.getCategoria().getId());
            dto.setCategoriaNome(entity.getCategoria().getNome());
        }

        dto.setObservacaoGeral(entity.getObservacaoGeral());
        dto.setPermiteVendaAPrazo(entity.getPermiteVendaAPrazo());
        dto.setLimiteCredito(entity.getLimiteCredito());
        dto.setAtivo(entity.getAtivo());
        dto.setCriadoEm(entity.getCriadoEm());
        dto.setAtualizadoEm(entity.getAtualizadoEm());

        return dto;
    }

    public Cliente toEntity(ClienteRequestDTO request) {
        if (request == null)
            return null;

        return Cliente.builder()
                .pessoaId(request.getPessoaId())
                .vendedorId(request.getVendedorId())
                .filialId(request.getFilialId())
                .codigoInterno(request.getCodigoInterno())
                .status(request.getStatus())
                .observacaoGeral(request.getObservacaoGeral())
                .permiteVendaAPrazo(request.getPermiteVendaAPrazo())
                .limiteCredito(request.getLimiteCredito())
                .build();
    }

    public void updateEntity(ClienteRequestDTO request, Cliente entity) {
        if (request == null || entity == null)
            return;

        entity.setPessoaId(request.getPessoaId());
        entity.setVendedorId(request.getVendedorId());
        entity.setFilialId(request.getFilialId());
        entity.setCodigoInterno(request.getCodigoInterno());
        entity.setStatus(request.getStatus());
        entity.setObservacaoGeral(request.getObservacaoGeral());
        entity.setPermiteVendaAPrazo(request.getPermiteVendaAPrazo());
        entity.setLimiteCredito(request.getLimiteCredito());
    }
}