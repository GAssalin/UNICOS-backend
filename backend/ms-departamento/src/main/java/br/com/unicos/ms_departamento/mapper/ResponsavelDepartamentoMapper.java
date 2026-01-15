package br.com.unicos.ms_departamento.mapper;

import br.com.unicos.ms_departamento.dto.ResponsavelDepartamentoCreateRequestDto;
import br.com.unicos.ms_departamento.dto.ResponsavelDepartamentoUpdateRequestDto;
import br.com.unicos.ms_departamento.dto.responsavel.ResponsavelDepartamentoResponseDto;
import br.com.unicos.ms_departamento.model.ResponsavelDepartamento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link ResponsavelDepartamento} e seus DTOs.
 *
 * <p>
 * Segue o padrão de mapeamento manual adotado no UniCoS,
 * utilizando ModelMapper apenas para conversões diretas
 * e protegendo campos sensíveis no domínio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ResponsavelDepartamentoMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade ResponsavelDepartamento para DTO de resposta.
     */
    public ResponsavelDepartamentoResponseDto toResponse(ResponsavelDepartamento entity) {
        return new ResponsavelDepartamentoResponseDto(
                entity.getId(),
                entity.getDepartamentoId(),
                entity.getResponsavelId(),
                entity.getPapel(),
                entity.getPrincipal(),
                entity.getVigenciaInicio(),
                entity.getVigenciaFim(),
                entity.getStatusResponsavelDepartamento()
        );
    }

    /**
     * Converte DTO de criação para entidade ResponsavelDepartamento.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId} (tenant), auditoria e
     * controles internos devem ser definidos no service.
     * </p>
     */
    public ResponsavelDepartamento toEntity(ResponsavelDepartamentoCreateRequestDto request) {
        return mapper.map(request, ResponsavelDepartamento.class);
    }

    /**
     * Atualiza uma entidade ResponsavelDepartamento existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e campos de auditoria não devem ser alterados.
     * Regras de negócio típicas:
     * <ul>
     *     <li>Garantir unicidade de {@code principal=true} por departamento (por vigência)</li>
     *     <li>Encerrar vigência anterior ao ativar um novo principal</li>
     * </ul>
     * </p>
     */
    public void updateEntity(ResponsavelDepartamentoUpdateRequestDto request, ResponsavelDepartamento entity) {
        entity.setDepartamentoId(request.departamentoId());
        entity.setResponsavelId(request.responsavelId());
        entity.setPapel(request.papel());
        entity.setPrincipal(request.principal());
        entity.setVigenciaInicio(request.vigenciaInicio());
        entity.setVigenciaFim(request.vigenciaFim());
        entity.setStatusResponsavelDepartamento(request.statusResponsavelDepartamento());
    }
}
