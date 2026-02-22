package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.ItemRecebimentoCompraDto;
import br.com.unicos.ms_compras.model.ItemRecebimentoCompra;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link ItemRecebimentoCompra} e {@link ItemRecebimentoCompraDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamentos automaticamente (RecebimentoCompra e ItemPedidoCompra são resolvidos no service)
 * - Não manipula coleções filhas (divergencias)
 * - Não altera tenant/auditoria
 * - Sem regras de negócio
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ItemRecebimentoCompraMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     *
     * <p>
     * Divergências NÃO são retornadas aqui (use DTO detalhado/endpoint específico).
     * </p>
     */
    public ItemRecebimentoCompraDto toResponse(ItemRecebimentoCompra entity) {
        if (entity == null) return null;

        Long recebimentoCompraId = null;
        if (entity.getRecebimentoCompra() != null) {
            recebimentoCompraId = entity.getRecebimentoCompra().getId();
        }

        Long itemPedidoCompraId = null;
        if (entity.getItemPedidoCompra() != null) {
            itemPedidoCompraId = entity.getItemPedidoCompra().getId();
        }

        return new ItemRecebimentoCompraDto(
                entity.getId(),
                recebimentoCompraId,
                itemPedidoCompraId,
                entity.getQuantidadeRecebida(),
                entity.getQuantidadeAprovada(),
                entity.getQuantidadeRecusada(),
                entity.getObservacao()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code recebimentoCompra} e {@code itemPedidoCompra}.
     * O service deve buscar as entidades por id e associar corretamente.
     * </p>
     */
    public ItemRecebimentoCompra toEntity(ItemRecebimentoCompraDto dto) {
        if (dto == null) return null;

        ItemRecebimentoCompra entity = mapper.map(dto, ItemRecebimentoCompra.class);
        entity.setRecebimentoCompra(null);   // relacionamento resolvido no service
        entity.setItemPedidoCompra(null);    // relacionamento resolvido no service
        // divergencias: responsabilidade do service (montagem/sync)
        return entity;
    }

    /**
     * Atualiza entidade existente com base no DTO.
     *
     * <p>
     * Não permite trocar os vínculos com {@code recebimentoCompra} e {@code itemPedidoCompra}.
     * Divergências são tratadas no service.
     * </p>
     */
    public void updateEntity(ItemRecebimentoCompraDto dto, ItemRecebimentoCompra entity) {
        if (dto == null || entity == null) return;

        entity.setQuantidadeRecebida(dto.quantidadeRecebida());
        entity.setQuantidadeAprovada(dto.quantidadeAprovada());
        entity.setQuantidadeRecusada(dto.quantidadeRecusada());
        entity.setObservacao(dto.observacao());
    }
}