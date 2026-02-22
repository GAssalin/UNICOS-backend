package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.ItemPedidoCompraDto;
import br.com.unicos.ms_compras.model.ItemPedidoCompra;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link ItemPedidoCompra} e {@link ItemPedidoCompraDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamento automaticamente (PedidoCompra é resolvido no service)
 * - Não altera tenant/auditoria
 * - Sem regras de negócio (ex.: cálculo de totalItem)
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ItemPedidoCompraMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     */
    public ItemPedidoCompraDto toResponse(ItemPedidoCompra entity) {
        if (entity == null) return null;

        Long pedidoCompraId = null;
        if (entity.getPedidoCompra() != null) {
            pedidoCompraId = entity.getPedidoCompra().getId();
        }

        return new ItemPedidoCompraDto(
                entity.getId(),
                pedidoCompraId,
                entity.getProdutoId(),
                entity.getProdutoDescricaoSnapshot(),
                entity.getUnidadeSnapshot(),
                entity.getQuantidade(),
                entity.getPrecoUnitario(),
                entity.getDescontoItem(),
                entity.getTotalItem(),
                entity.getObservacao()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code pedidoCompra} aqui.
     * O service deve buscar o PedidoCompra por id e associar corretamente.
     * </p>
     */
    public ItemPedidoCompra toEntity(ItemPedidoCompraDto dto) {
        if (dto == null) return null;

        ItemPedidoCompra entity = mapper.map(dto, ItemPedidoCompra.class);
        entity.setPedidoCompra(null); // relacionamento resolvido no service
        return entity;
    }

    /**
     * Atualiza entidade existente com base no DTO.
     *
     * <p>
     * Não permite trocar o vínculo com {@code pedidoCompra}.
     * Cálculo de {@code totalItem} idealmente deve ser feito no service/domínio.
     * </p>
     */
    public void updateEntity(ItemPedidoCompraDto dto, ItemPedidoCompra entity) {
        if (dto == null || entity == null) return;

        entity.setProdutoId(dto.produtoId());
        entity.setProdutoDescricaoSnapshot(dto.produtoDescricaoSnapshot());
        entity.setUnidadeSnapshot(dto.unidadeSnapshot());
        entity.setQuantidade(dto.quantidade());
        entity.setPrecoUnitario(dto.precoUnitario());
        entity.setDescontoItem(dto.descontoItem());
        entity.setTotalItem(dto.totalItem());
        entity.setObservacao(dto.observacao());
    }
}