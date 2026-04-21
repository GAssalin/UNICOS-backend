package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialCreateRequestDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialResponseDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialUpdateRequestDto;
import br.com.unicos.ms_estoque.model.VinculoEstoqueFilial;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link VinculoEstoqueFilial} e seus DTOs.
 *
 * <p>
 * O mapeamento é feito de forma explícita para garantir clareza,
 * previsibilidade e proteção de campos sensíveis do domínio.
 * </p>
 */
@Component
public class VinculoEstoqueFilialMapper {

    /**
     * Converte a entidade {@link VinculoEstoqueFilial} para DTO de resposta.
     *
     * @param entity entidade de vínculo entre estoque e filial
     * @return DTO de resposta
     */
    public VinculoEstoqueFilialResponseDto toResponse(VinculoEstoqueFilial entity) {
        if (entity == null) {
            return null;
        }

        return new VinculoEstoqueFilialResponseDto(
                entity.getId(),
                entity.getEstoqueId(),
                entity.getFilialId(),
                entity.getTipoAtuacao(),
                entity.getVigenciaInicio(),
                entity.getVigenciaFim(),
                entity.getStatusVinculoEstoqueFilial()
        );
    }

    /**
     * Converte DTO de criação para entidade {@link VinculoEstoqueFilial}.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId}, auditoria e controles internos
     * devem ser definidos pela camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return entidade preenchida com os dados do DTO
     */
    public VinculoEstoqueFilial toEntity(VinculoEstoqueFilialCreateRequestDto request) {
        if (request == null) {
            return null;
        }

        return VinculoEstoqueFilial.builder()
                .estoqueId(request.estoqueId())
                .filialId(request.filialId())
                .tipoAtuacao(request.tipoAtuacao())
                .vigenciaInicio(request.vigenciaInicio())
                .vigenciaFim(request.vigenciaFim())
                .statusVinculoEstoqueFilial(request.statusVinculoEstoqueFilial())
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
    public void updateEntity(VinculoEstoqueFilialUpdateRequestDto request, VinculoEstoqueFilial entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setEstoqueId(request.estoqueId());
        entity.setFilialId(request.filialId());
        entity.setTipoAtuacao(request.tipoAtuacao());
        entity.setVigenciaInicio(request.vigenciaInicio());
        entity.setVigenciaFim(request.vigenciaFim());
        entity.setStatusVinculoEstoqueFilial(request.statusVinculoEstoqueFilial());
    }
}