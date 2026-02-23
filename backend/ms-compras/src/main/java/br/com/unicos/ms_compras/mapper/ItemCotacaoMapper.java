package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.ItemCotacaoDto;
import br.com.unicos.ms_compras.model.ItemCotacao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link ItemCotacao} e {@link ItemCotacaoDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamento automaticamente (CotacaoCompra é resolvido no service)
 * - Não altera tenant/auditoria
 * - Sem regras de negócio
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ItemCotacaoMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     */
    public ItemCotacaoDto toResponse(ItemCotacao entity) {
        if (entity == null) return null;

        Long cotacaoCompraId = null;
        if (entity.getCotacaoCompra() != null) {
            cotacaoCompraId = entity.getCotacaoCompra().getId();
        }

        return new ItemCotacaoDto(
                entity.getId(),
                cotacaoCompraId,
                entity.getProdutoId(),
                entity.getProdutoDescricaoSnapshot(),
                entity.getUnidadeSnapshot(),
                entity.getQuantidade(),
                entity.getObservacao()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code cotacaoCompra} aqui.
     * O service deve buscar a CotacaoCompra por id e associar corretamente.
     * </p>
     */
    public ItemCotacao toEntity(ItemCotacaoDto dto) {
        if (dto == null) return null;

        ItemCotacao entity = mapper.map(dto, ItemCotacao.class);
        entity.setCotacaoCompra(null); // relacionamento resolvido no service
        return entity;
    }

    /**
     * Atualiza entidade existente com base no DTO.
     *
     * <p>
     * Não permite trocar o vínculo com {@code cotacaoCompra}.
     * Se precisar mover item de cotação, trate como caso de uso no service.
     * </p>
     */
    public void updateEntity(ItemCotacaoDto dto, ItemCotacao entity) {
        if (dto == null || entity == null) return;

        entity.setProdutoId(dto.produtoId());
        entity.setProdutoDescricaoSnapshot(dto.produtoDescricaoSnapshot());
        entity.setUnidadeSnapshot(dto.unidadeSnapshot());
        entity.setQuantidade(dto.quantidade());
        entity.setObservacao(dto.observacao());
    }
}