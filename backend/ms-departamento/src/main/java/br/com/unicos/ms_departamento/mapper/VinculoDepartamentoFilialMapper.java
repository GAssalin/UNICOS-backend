package br.com.unicos.ms_departamento.mapper;

import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialCreateRequestDto;
import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialResponseDto;
import br.com.unicos.ms_departamento.dto.vinculo.VinculoDepartamentoFilialUpdateRequestDto;
import br.com.unicos.ms_departamento.model.VinculoDepartamentoFilial;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link VinculoDepartamentoFilial} e seus DTOs.
 *
 * <p>
 * Segue o padrão de mapeamento manual adotado no UniCoS,
 * utilizando ModelMapper apenas para conversões diretas
 * e protegendo campos sensíveis no domínio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class VinculoDepartamentoFilialMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade VinculoDepartamentoFilial para DTO de resposta.
     */
    public VinculoDepartamentoFilialResponseDto toResponse(VinculoDepartamentoFilial entity) {
        return new VinculoDepartamentoFilialResponseDto(
                entity.getId(),
                entity.getDepartamentoId(),
                entity.getFilialId(),
                entity.getTipoAtuacao(),
                entity.getVigenciaInicio(),
                entity.getVigenciaFim(),
                entity.getStatusVinculoDepartamentoFilial()
        );
    }

    /**
     * Converte DTO de criação para entidade VinculoDepartamentoFilial.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId} (tenant), auditoria e
     * controles internos devem ser definidos no service.
     * </p>
     */
    public VinculoDepartamentoFilial toEntity(VinculoDepartamentoFilialCreateRequestDto request) {
        return mapper.map(request, VinculoDepartamentoFilial.class);
    }

    /**
     * Atualiza uma entidade VinculoDepartamentoFilial existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e campos de auditoria não devem ser alterados.
     * Ajuste regras adicionais conforme sua política:
     * <ul>
     *     <li>Evitar múltiplos vínculos ATIVOS para o mesmo par (departamentoId, filialId)</li>
     *     <li>Controlar vigências para manter histórico</li>
     * </ul>
     * </p>
     */
    public void updateEntity(VinculoDepartamentoFilialUpdateRequestDto request, VinculoDepartamentoFilial entity) {
        entity.setDepartamentoId(request.departamentoId());
        entity.setFilialId(request.filialId());
        entity.setTipoAtuacao(request.tipoAtuacao());
        entity.setVigenciaInicio(request.vigenciaInicio());
        entity.setVigenciaFim(request.vigenciaFim());
        entity.setStatusVinculoDepartamentoFilial(request.statusVinculoDepartamentoFilial());
    }
}
