package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.PedidoCompraDto;
import br.com.unicos.ms_compras.model.CondicaoPagamento;
import br.com.unicos.ms_compras.model.Fornecedor;
import br.com.unicos.ms_compras.model.PedidoCompra;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Mapper da entidade PedidoCompra.
 *
 * <p>Observação: o DTO contém apenas IDs de relacionamentos (fornecedorId e condicaoPagamentoId).
 * Assim, para mapear DTO -> Entity com consistência, os relacionamentos devem ser resolvidos
 * na camada de service e passados para o mapper.</p>
 */
@Component
@RequiredArgsConstructor
public class PedidoCompraMapper {

    private final ModelMapper mapper;

    public PedidoCompraDto toResponse(PedidoCompra entity) {
        return new PedidoCompraDto(
                entity.getId(),
                entity.getCodigo(),
                entity.getFornecedor() != null ? entity.getFornecedor().getId() : null,
                entity.getDataEmissao(),
                entity.getDataPrevistaEntrega(),
                entity.getStatusPedidoCompra(),
                entity.getCondicaoPagamento() != null ? entity.getCondicaoPagamento().getId() : null,
                entity.getObservacao(),
                entity.getSubtotal(),
                entity.getDesconto(),
                entity.getFrete(),
                entity.getTotal(),
                entity.getAprovadoPor(),
                entity.getAprovadoEm(),
                entity.getMotivoCancelamentoCompra(),
                entity.getObservacaoCancelamento(),
                entity.getCanceladoPor(),
                entity.getCanceladoEm()
        );
    }

    /**
     * Cria uma entidade PedidoCompra a partir do DTO, com relacionamentos resolvidos.
     *
     * @param dto DTO de pedido de compra
     * @param fornecedor entidade Fornecedor já carregada (obrigatória)
     * @param condicaoPagamento entidade CondicaoPagamento já carregada (opcional)
     */
    public PedidoCompra toEntity(PedidoCompraDto dto, Fornecedor fornecedor, @Nullable CondicaoPagamento condicaoPagamento) {
        // Mapeia campos simples (sem relacionamentos e sem coleção de itens).
        PedidoCompra entity = mapper.map(dto, PedidoCompra.class);

        // Garante relacionamentos corretos
        entity.setFornecedor(fornecedor);
        entity.setCondicaoPagamento(condicaoPagamento);

        // Itens: não vêm no DTO. Mantém lista default do builder/entidade.
        // Se o ModelMapper setar null, normalize:
        if (entity.getItens() == null) {
            entity.setItens(new java.util.ArrayList<>());
        }

        return entity;
    }

    /**
     * Atualiza uma entidade existente com base no DTO, preservando itens e controlando relacionamentos.
     *
     * @param dto DTO de pedido de compra
     * @param entity entidade existente (gerenciada)
     * @param fornecedor entidade Fornecedor já carregada (obrigatória)
     * @param condicaoPagamento entidade CondicaoPagamento já carregada (opcional)
     */
    public void updateEntity(PedidoCompraDto dto, PedidoCompra entity, Fornecedor fornecedor, @Nullable CondicaoPagamento condicaoPagamento) {
        entity.setCodigo(dto.codigo());
        entity.setFornecedor(fornecedor);
        entity.setDataEmissao(dto.dataEmissao());
        entity.setDataPrevistaEntrega(dto.dataPrevistaEntrega());
        entity.setStatusPedidoCompra(dto.statusPedidoCompra());
        entity.setCondicaoPagamento(condicaoPagamento);
        entity.setObservacao(dto.observacao());

        entity.setSubtotal(dto.subtotal());
        entity.setDesconto(dto.desconto());
        entity.setFrete(dto.frete());
        entity.setTotal(dto.total());

        entity.setAprovadoPor(dto.aprovadoPor());
        entity.setAprovadoEm(dto.aprovadoEm());

        entity.setMotivoCancelamentoCompra(dto.motivoCancelamentoCompra());
        entity.setObservacaoCancelamento(dto.observacaoCancelamento());
        entity.setCanceladoPor(dto.canceladoPor());
        entity.setCanceladoEm(dto.canceladoEm());

        // Itens: não atualizar aqui porque não existe no DTO.
        // Atualização de itens deve ser feita por um fluxo específico (merge/orphanRemoval).
    }
}