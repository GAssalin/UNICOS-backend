package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.movimentacao.MovimentacaoEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.model.MovimentacaoEstoque;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link MovimentacaoEstoque} e seus DTOs.
 *
 * <p>
 * O mapeamento é feito de forma explícita para garantir clareza,
 * previsibilidade e proteção de campos sensíveis do domínio.
 * </p>
 */
@Component
public class MovimentacaoEstoqueMapper {

    /**
     * Converte a entidade {@link MovimentacaoEstoque} para DTO de resposta.
     *
     * @param entity entidade de movimentação de estoque
     * @return DTO de resposta
     */
    public MovimentacaoEstoqueResponseDto toResponse(MovimentacaoEstoque entity) {
        if (entity == null) {
            return null;
        }

        return new MovimentacaoEstoqueResponseDto(
                entity.getId(),
                entity.getTipoMovimentacao(),
                entity.getEstoqueOrigemId(),
                entity.getEstoqueDestinoId(),
                entity.getDataMovimentacao(),
                entity.getObservacao(),
                entity.getDocumentoReferencia(),
                entity.getUsuarioResponsavelId(),
                entity.getStatusMovimentacao()
        );
    }

    /**
     * Converte DTO de criação para entidade {@link MovimentacaoEstoque}.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId}, auditoria e controles internos
     * devem ser definidos pela camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return entidade preenchida com os dados do DTO
     */
    public MovimentacaoEstoque toEntity(MovimentacaoEstoqueCreateRequestDto request) {
        if (request == null) {
            return null;
        }

        return MovimentacaoEstoque.builder()
                .tipoMovimentacao(request.tipoMovimentacao())
                .estoqueOrigemId(request.estoqueOrigemId())
                .estoqueDestinoId(request.estoqueDestinoId())
                .dataMovimentacao(request.dataMovimentacao())
                .observacao(request.observacao())
                .documentoReferencia(request.documentoReferencia())
                .usuarioResponsavelId(request.usuarioResponsavelId())
                .statusMovimentacao(request.statusMovimentacao())
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
    public void updateEntity(MovimentacaoEstoqueUpdateRequestDto request, MovimentacaoEstoque entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setTipoMovimentacao(request.tipoMovimentacao());
        entity.setEstoqueOrigemId(request.estoqueOrigemId());
        entity.setEstoqueDestinoId(request.estoqueDestinoId());
        entity.setDataMovimentacao(request.dataMovimentacao());
        entity.setObservacao(request.observacao());
        entity.setDocumentoReferencia(request.documentoReferencia());
        entity.setUsuarioResponsavelId(request.usuarioResponsavelId());
        entity.setStatusMovimentacao(request.statusMovimentacao());
    }
}