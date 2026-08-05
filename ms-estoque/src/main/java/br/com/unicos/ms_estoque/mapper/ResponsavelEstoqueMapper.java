package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.model.ResponsavelEstoque;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link ResponsavelEstoque} e seus DTOs.
 *
 * <p>
 * O mapeamento é feito de forma explícita para garantir clareza,
 * previsibilidade e proteção de campos sensíveis do domínio.
 * </p>
 */
@Component
public class ResponsavelEstoqueMapper {

    /**
     * Converte a entidade {@link ResponsavelEstoque} para DTO de resposta.
     *
     * @param entity entidade de responsável do estoque
     * @return DTO de resposta
     */
    public ResponsavelEstoqueResponseDto toResponse(ResponsavelEstoque entity) {
        if (entity == null) {
            return null;
        }

        return new ResponsavelEstoqueResponseDto(
                entity.getId(),
                entity.getEstoqueId(),
                entity.getResponsavelId(),
                entity.getPapel(),
                entity.getPrincipal(),
                entity.getVigenciaInicio(),
                entity.getVigenciaFim(),
                entity.getStatusResponsavelEstoque()
        );
    }

    /**
     * Converte DTO de criação para entidade {@link ResponsavelEstoque}.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId}, auditoria e controles internos
     * devem ser definidos pela camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return entidade preenchida com os dados do DTO
     */
    public ResponsavelEstoque toEntity(ResponsavelEstoqueCreateRequestDto request) {
        if (request == null) {
            return null;
        }

        return ResponsavelEstoque.builder()
                .estoqueId(request.estoqueId())
                .responsavelId(request.responsavelId())
                .papel(request.papel())
                .principal(request.principal())
                .vigenciaInicio(request.vigenciaInicio())
                .vigenciaFim(request.vigenciaFim())
                .statusResponsavelEstoque(request.statusResponsavelEstoque())
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
    public void updateEntity(ResponsavelEstoqueUpdateRequestDto request, ResponsavelEstoque entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setEstoqueId(request.estoqueId());
        entity.setResponsavelId(request.responsavelId());
        entity.setPapel(request.papel());
        entity.setPrincipal(request.principal());
        entity.setVigenciaInicio(request.vigenciaInicio());
        entity.setVigenciaFim(request.vigenciaFim());
        entity.setStatusResponsavelEstoque(request.statusResponsavelEstoque());
    }
}