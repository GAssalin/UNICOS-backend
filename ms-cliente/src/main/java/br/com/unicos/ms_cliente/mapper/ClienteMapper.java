package br.com.unicos.ms_cliente.mapper;

import br.com.unicos.ms_cliente.dto.cliente.ClienteRequest;
import br.com.unicos.ms_cliente.dto.cliente.ClienteResponse;
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

    public ClienteResponse toResponse(Cliente entity) {
        if (entity == null)
            return null;

        return new ClienteResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getPessoaId(),
                entity.getVendedorId(),
                entity.getVendedorId() == null ? null : pessoasService.buscarPorId(entity.getVendedorId()).nome(),
                entity.getFilialId(),
                entity.getCodigoInterno(),
                entity.getStatus(),
                entity.getCategoria() == null ? null : entity.getCategoria().getId(),
                entity.getCategoria() == null ? null : entity.getCategoria().getNome(),
                entity.getObservacaoGeral(),
                entity.getPermiteVendaAPrazo(),
                entity.getLimiteCredito(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public Cliente toEntity(ClienteRequest request) {
        if (request == null)
            return null;

        return Cliente.builder()
                .pessoaId(request.pessoaId())
                .vendedorId(request.vendedorId())
                .filialId(request.filialId())
                .codigoInterno(request.codigoInterno())
                .status(request.status())
                .observacaoGeral(request.observacaoGeral())
                .permiteVendaAPrazo(request.permiteVendaAPrazo())
                .limiteCredito(request.limiteCredito())
                .build();
    }

    public void updateEntity(Cliente entity, ClienteRequest request) {
        if (request == null || entity == null)
            return;

        entity.setPessoaId(request.pessoaId());
        if (request.vendedorId() != null)
            entity.setVendedorId(request.vendedorId());
        entity.setFilialId(request.filialId());
        entity.setCodigoInterno(request.codigoInterno());
        entity.setStatus(request.status());
        entity.setObservacaoGeral(request.observacaoGeral());
        entity.setPermiteVendaAPrazo(request.permiteVendaAPrazo());
        entity.setLimiteCredito(request.limiteCredito());
    }
}