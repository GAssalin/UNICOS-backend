package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoCreateRequestDto;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoResponseDto;
import br.com.unicos.ms_estoque.dto.estoqueproduto.EstoqueProdutoUpdateRequestDto;
import br.com.unicos.ms_estoque.model.EstoqueProduto;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link EstoqueProduto} e seus DTOs.
 *
 * <p>
 * O mapeamento é feito de forma explícita para garantir clareza,
 * previsibilidade e proteção de campos sensíveis do domínio.
 * </p>
 */
@Component
public class EstoqueProdutoMapper {

    /**
     * Converte a entidade {@link EstoqueProduto} para DTO de resposta.
     *
     * @param entity entidade de estoque produto
     * @return DTO de resposta
     */
    public EstoqueProdutoResponseDto toResponse(EstoqueProduto entity) {
        if (entity == null) {
            return null;
        }

        return new EstoqueProdutoResponseDto(
                entity.getId(),
                entity.getEstoqueId(),
                entity.getProdutoId(),
                entity.getQuantidadeAtual(),
                entity.getQuantidadeReservada(),
                entity.getQuantidadeDisponivel()
        );
    }

    /**
     * Converte DTO de criação para entidade {@link EstoqueProduto}.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId}, auditoria e controles internos
     * devem ser definidos pela camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return entidade preenchida com os dados do DTO
     */
    public EstoqueProduto toEntity(EstoqueProdutoCreateRequestDto request) {
        if (request == null) {
            return null;
        }

        return EstoqueProduto.builder()
                .estoqueId(request.estoqueId())
                .produtoId(request.produtoId())
                .quantidadeAtual(request.quantidadeAtual())
                .quantidadeReservada(request.quantidadeReservada())
                .quantidadeDisponivel(request.quantidadeDisponivel())
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
    public void updateEntity(EstoqueProdutoUpdateRequestDto request, EstoqueProduto entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setEstoqueId(request.estoqueId());
        entity.setProdutoId(request.produtoId());
        entity.setQuantidadeAtual(request.quantidadeAtual());
        entity.setQuantidadeReservada(request.quantidadeReservada());
        entity.setQuantidadeDisponivel(request.quantidadeDisponivel());
    }
}