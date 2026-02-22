package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.RecebimentoCompraDto;
import br.com.unicos.ms_compras.model.Fornecedor;
import br.com.unicos.ms_compras.model.PedidoCompra;
import br.com.unicos.ms_compras.model.RecebimentoCompra;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper da entidade RecebimentoCompra.
 *
 * <p>Observação: o DTO contém apenas IDs de relacionamentos (pedidoCompraId e fornecedorId).
 * Portanto, a camada de service deve carregar as entidades e passá-las ao mapper.</p>
 */
@Component
@RequiredArgsConstructor
public class RecebimentoCompraMapper {

    private final ModelMapper mapper;

    public RecebimentoCompraDto toResponse(RecebimentoCompra entity) {
        return new RecebimentoCompraDto(
                entity.getId(),
                entity.getPedidoCompra() != null ? entity.getPedidoCompra().getId() : null,
                entity.getFornecedor() != null ? entity.getFornecedor().getId() : null,
                entity.getDataRecebimento(),
                entity.getStatus(),
                entity.getObservacao()
        );
    }

    /**
     * Cria uma entidade RecebimentoCompra a partir do DTO, com relacionamentos resolvidos.
     *
     * @param dto DTO de recebimento
     * @param pedidoCompra PedidoCompra já carregado (obrigatório)
     * @param fornecedor Fornecedor já carregado (obrigatório)
     */
    public RecebimentoCompra toEntity(RecebimentoCompraDto dto, PedidoCompra pedidoCompra, Fornecedor fornecedor) {
        RecebimentoCompra entity = mapper.map(dto, RecebimentoCompra.class);

        entity.setPedidoCompra(pedidoCompra);
        entity.setFornecedor(fornecedor);

        // Coleções não vêm no DTO; normalize caso o ModelMapper tenha setado null.
        if (entity.getItens() == null) {
            entity.setItens(new java.util.ArrayList<>());
        }
        if (entity.getDocumentosEntrada() == null) {
            entity.setDocumentosEntrada(new java.util.ArrayList<>());
        }

        return entity;
    }

    /**
     * Atualiza uma entidade existente a partir do DTO, preservando itens e documentos.
     *
     * @param dto DTO de recebimento
     * @param entity entidade existente (gerenciada)
     * @param pedidoCompra PedidoCompra já carregado (obrigatório)
     * @param fornecedor Fornecedor já carregado (obrigatório)
     */
    public void updateEntity(RecebimentoCompraDto dto, RecebimentoCompra entity, PedidoCompra pedidoCompra, Fornecedor fornecedor) {
        entity.setPedidoCompra(pedidoCompra);
        entity.setFornecedor(fornecedor);
        entity.setDataRecebimento(dto.dataRecebimento());
        entity.setStatus(dto.status());
        entity.setObservacao(dto.observacao());

        // Não atualizar itens/documentos aqui (DTO não tem).
        // Atualização dessas coleções deve ser feita em fluxo específico (merge/orphanRemoval).
    }
}