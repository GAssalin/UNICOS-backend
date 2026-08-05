package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.estoque.EstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.model.Estoque;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link Estoque} e seus DTOs.
 *
 * <p>
 * O mapeamento é feito de forma explícita para garantir clareza,
 * previsibilidade e proteção de campos sensíveis do domínio.
 * </p>
 */
@Component
public class EstoqueMapper {

    /**
     * Converte a entidade {@link Estoque} para DTO de resposta.
     *
     * @param entity entidade de estoque
     * @return DTO de resposta
     */
    public EstoqueResponseDto toResponse(Estoque entity) {
        if (entity == null) {
            return null;
        }

        return new EstoqueResponseDto(
                entity.getId(),
                entity.getCodigo(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getStatusEstoque(),
                entity.getEstoquePaiId()
        );
    }

    /**
     * Converte DTO de criação para entidade {@link Estoque}.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId}, auditoria e controles internos
     * devem ser definidos pela camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return entidade preenchida com os dados do DTO
     */
    public Estoque toEntity(EstoqueCreateRequestDto request) {
        if (request == null) {
            return null;
        }

        return Estoque.builder()
                .codigo(request.codigo())
                .nome(request.nome())
                .descricao(request.descricao())
                .statusEstoque(request.statusEstoque())
                .estoquePaiId(request.estoquePaiId())
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
    public void updateEntity(EstoqueUpdateRequestDto request, Estoque entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setCodigo(request.codigo());
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setStatusEstoque(request.statusEstoque());
        entity.setEstoquePaiId(request.estoquePaiId());
    }
}