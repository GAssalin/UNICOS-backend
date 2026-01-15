package br.com.unicos.ms_departamento.mapper;

import br.com.unicos.ms_departamento.dto.departamento.DepartamentoCreateRequestDto;
import br.com.unicos.ms_departamento.dto.departamento.DepartamentoResponseDto;
import br.com.unicos.ms_departamento.dto.departamento.DepartamentoUpdateRequestDto;
import br.com.unicos.ms_departamento.model.Departamento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link Departamento} e seus DTOs.
 *
 * <p>
 * Segue o padrão de mapeamento manual adotado no UniCoS,
 * utilizando ModelMapper apenas para conversões diretas
 * e protegendo campos sensíveis no domínio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DepartamentoMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade Departamento para DTO de resposta.
     */
    public DepartamentoResponseDto toResponse(Departamento entity) {
        return new DepartamentoResponseDto(
                entity.getId(),
                entity.getCodigo(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getStatusDepartamento(),
                entity.getDepartamentoPaiId()
        );
    }

    /**
     * Converte DTO de criação para entidade Departamento.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId} (tenant), auditoria e
     * controles internos devem ser definidos no service.
     * </p>
     */
    public Departamento toEntity(DepartamentoCreateRequestDto request) {
        return mapper.map(request, Departamento.class);
    }

    /**
     * Atualiza uma entidade Departamento existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e campos de auditoria não devem ser alterados.
     * Ajuste regras adicionais (ex.: {@code codigo} imutável) conforme sua política.
     * </p>
     */
    public void updateEntity(DepartamentoUpdateRequestDto request, Departamento entity) {
        entity.setCodigo(request.codigo());
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setStatusDepartamento(request.statusDepartamento());
        entity.setDepartamentoPaiId(request.departamentoPaiId());
    }
}
