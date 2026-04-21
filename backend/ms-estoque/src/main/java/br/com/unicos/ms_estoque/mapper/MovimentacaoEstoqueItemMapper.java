package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemCreateRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemResponseDto;
import br.com.unicos.ms_estoque.dto.movimentacaoitem.MovimentacaoEstoqueItemUpdateRequestDto;
import br.com.unicos.ms_estoque.model.MovimentacaoEstoqueItem;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link MovimentacaoEstoqueItem} e seus DTOs.
 *
 * <p>
 * O mapeamento é feito de forma explícita para garantir clareza,
 * previsibilidade e proteção de campos sensíveis do domínio.
 * </p>
 */
@Component
public class MovimentacaoEstoqueItemMapper {

    /**
     * Converte a entidade {@link MovimentacaoEstoqueItem} para DTO de resposta.
     *
     * @param entity entidade de movimentação de estoque item
     * @return DTO de resposta
     */
    public MovimentacaoEstoqueItemResponseDto toResponse(MovimentacaoEstoqueItem entity) {
        if (entity == null) {
            return null;
        }

        return new MovimentacaoEstoqueItemResponseDto(
                entity.getId(),
                entity.getMovimentacaoId(),
                entity.getProdutoId(),
                entity.getQuantidade(),
                entity.getValorUnitario()
        );
    }

    /**
     * Converte DTO de criação para entidade {@link MovimentacaoEstoqueItem}.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId}, auditoria e controles internos
     * devem ser definidos pela camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return entidade preenchida com os dados do DTO
     */
    public MovimentacaoEstoqueItem toEntity(MovimentacaoEstoqueItemCreateRequestDto request) {
        if (request == null) {
            return null;
        }

        return MovimentacaoEstoqueItem.builder()
                .movimentacaoId(request.movimentacaoId())
                .produtoId(request.produtoId())
                .quantidade(request.quantidade())
                .valorUnitario(request.valorUnitario())
                .build();
    }

    /**
     * Atualiza uma entidade existente com os dados do DTO de atualização.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId} e auditoria não são alterados aqui.
     * </p>
     *
     * @param request DTO de atualização
     * @param entity entidade existente a ser atualizada
     */
    public void updateEntity(MovimentacaoEstoqueItemUpdateRequestDto request, MovimentacaoEstoqueItem entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setMovimentacaoId(request.movimentacaoId());
        entity.setProdutoId(request.produtoId());
        entity.setQuantidade(request.quantidade());
        entity.setValorUnitario(request.valorUnitario());
    }
}