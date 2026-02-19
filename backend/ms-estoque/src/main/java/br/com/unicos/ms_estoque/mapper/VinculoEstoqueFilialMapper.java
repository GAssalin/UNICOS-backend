package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialCreateRequestDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialResponseDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialUpdateRequestDto;
import br.com.unicos.ms_estoque.model.VinculoEstoqueFilial;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link VinculoEstoqueFilial} e seus DTOs.
 *
 * <p>
 * Segue o padrão de mapeamento manual adotado no UniCoS,
 * utilizando ModelMapper apenas para conversões diretas
 * e protegendo campos sensíveis no domínio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class VinculoEstoqueFilialMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade VinculoEstoqueFilial para DTO de resposta.
     */
    public VinculoEstoqueFilialResponseDto toResponse(VinculoEstoqueFilial entity) {
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
     * Converte DTO de criação para entidade VinculoEstoqueFilial.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId} (tenant), auditoria e
     * controles internos devem ser definidos no service.
     * </p>
     */
    public VinculoEstoqueFilial toEntity(VinculoEstoqueFilialCreateRequestDto request) {
        return mapper.map(request, VinculoEstoqueFilial.class);
    }

    /**
     * Atualiza uma entidade VinculoEstoqueFilial existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e campos de auditoria não devem ser alterados.
     * Ajuste regras adicionais conforme sua política:
     * <ul>
     *     <li>Evitar múltiplos vínculos ATIVOS para o mesmo par (estoqueId, filialId)</li>
     *     <li>Controlar vigências para manter histórico</li>
     * </ul>
     * </p>
     */
    public void updateEntity(VinculoEstoqueFilialUpdateRequestDto request, VinculoEstoqueFilial entity) {
        entity.setEstoqueId(request.estoqueId());
        entity.setFilialId(request.filialId());
        entity.setTipoAtuacao(request.tipoAtuacao());
        entity.setVigenciaInicio(request.vigenciaInicio());
        entity.setVigenciaFim(request.vigenciaFim());
        entity.setStatusVinculoEstoqueFilial(request.statusVinculoEstoqueFilial());
    }
}
