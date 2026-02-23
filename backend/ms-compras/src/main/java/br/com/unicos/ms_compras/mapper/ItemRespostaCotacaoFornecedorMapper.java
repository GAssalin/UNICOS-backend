package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.ItemRespostaCotacaoFornecedorDto;
import br.com.unicos.ms_compras.model.ItemRespostaCotacaoFornecedor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link ItemRespostaCotacaoFornecedor}
 * e {@link ItemRespostaCotacaoFornecedorDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamentos automaticamente (RespostaCotacaoFornecedor e ItemCotacao são resolvidos no service)
 * - Não altera tenant/auditoria
 * - Sem regras de negócio (ex.: cálculo de totalItem)
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ItemRespostaCotacaoFornecedorMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     */
    public ItemRespostaCotacaoFornecedorDto toResponse(ItemRespostaCotacaoFornecedor entity) {
        if (entity == null) return null;

        Long respostaCotacaoFornecedorId = null;
        if (entity.getRespostaCotacaoFornecedor() != null) {
            respostaCotacaoFornecedorId = entity.getRespostaCotacaoFornecedor().getId();
        }

        Long itemCotacaoId = null;
        if (entity.getItemCotacao() != null) {
            itemCotacaoId = entity.getItemCotacao().getId();
        }

        return new ItemRespostaCotacaoFornecedorDto(
                entity.getId(),
                respostaCotacaoFornecedorId,
                itemCotacaoId,
                entity.getPrecoUnitario(),
                entity.getDescontoItem(),
                entity.getTotalItem(),
                entity.getPrazoEntregaDias(),
                entity.getObservacao()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code respostaCotacaoFornecedor} e {@code itemCotacao}.
     * O service deve buscar as entidades por id e associar corretamente.
     * </p>
     */
    public ItemRespostaCotacaoFornecedor toEntity(ItemRespostaCotacaoFornecedorDto dto) {
        if (dto == null) return null;

        ItemRespostaCotacaoFornecedor entity = mapper.map(dto, ItemRespostaCotacaoFornecedor.class);
        entity.setRespostaCotacaoFornecedor(null); // relacionamento resolvido no service
        entity.setItemCotacao(null);               // relacionamento resolvido no service
        return entity;
    }

    /**
     * Atualiza entidade existente com base no DTO.
     *
     * <p>
     * Não permite trocar os vínculos com {@code respostaCotacaoFornecedor} e {@code itemCotacao}.
     * Caso necessário, trate como caso de uso no service.
     * </p>
     */
    public void updateEntity(ItemRespostaCotacaoFornecedorDto dto, ItemRespostaCotacaoFornecedor entity) {
        if (dto == null || entity == null) return;

        entity.setPrecoUnitario(dto.precoUnitario());
        entity.setDescontoItem(dto.descontoItem());
        entity.setTotalItem(dto.totalItem());
        entity.setPrazoEntregaDias(dto.prazoEntregaDias());
        entity.setObservacao(dto.observacao());
    }
}