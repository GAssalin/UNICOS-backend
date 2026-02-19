package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueResponseDto;
import br.com.unicos.ms_estoque.model.ResponsavelEstoque;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link ResponsavelEstoque} e seus DTOs.
 *
 * <p>
 * Segue o padrão de mapeamento manual adotado no UniCoS,
 * utilizando ModelMapper apenas para conversões diretas
 * e protegendo campos sensíveis no domínio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ResponsavelEstoqueMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade ResponsavelEstoque para DTO de resposta.
     */
    public ResponsavelEstoqueResponseDto toResponse(ResponsavelEstoque entity) {
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
     * Converte DTO de criação para entidade ResponsavelEstoque.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId} (tenant), auditoria e
     * controles internos devem ser definidos no service.
     * </p>
     */
    public ResponsavelEstoque toEntity(ResponsavelEstoqueCreateRequestDto request) {
        return mapper.map(request, ResponsavelEstoque.class);
    }

    /**
     * Atualiza uma entidade ResponsavelEstoque existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e campos de auditoria não devem ser alterados.
     * Regras de negócio típicas:
     * <ul>
     *     <li>Garantir unicidade de {@code principal=true} por estoque (por vigência)</li>
     *     <li>Encerrar vigência anterior ao ativar um novo principal</li>
     * </ul>
     * </p>
     */
    public void updateEntity(ResponsavelEstoqueUpdateRequestDto request, ResponsavelEstoque entity) {
        entity.setEstoqueId(request.estoqueId());
        entity.setResponsavelId(request.responsavelId());
        entity.setPapel(request.papel());
        entity.setPrincipal(request.principal());
        entity.setVigenciaInicio(request.vigenciaInicio());
        entity.setVigenciaFim(request.vigenciaFim());
        entity.setStatusResponsavelEstoque(request.statusResponsavelEstoque());
    }
}
