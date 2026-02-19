package br.com.unicos.ms_estoque.mapper;

import br.com.unicos.ms_estoque.dto.estoque.EstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.estoque.EstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.model.Estoque;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link Estoque} e seus DTOs.
 *
 * <p>
 * Segue o padrão de mapeamento manual adotado no UniCoS,
 * utilizando ModelMapper apenas para conversões diretas
 * e protegendo campos sensíveis no domínio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class EstoqueMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade Estoque para DTO de resposta.
     */
    public EstoqueResponseDto toResponse(Estoque entity) {
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
     * Converte DTO de criação para entidade Estoque.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId} (tenant), auditoria e
     * controles internos devem ser definidos no service.
     * </p>
     */
    public Estoque toEntity(EstoqueCreateRequestDto request) {
        return mapper.map(request, Estoque.class);
    }

    /**
     * Atualiza uma entidade Estoque existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e campos de auditoria não devem ser alterados.
     * Ajuste regras adicionais (ex.: {@code codigo} imutável) conforme sua política.
     * </p>
     */
    public void updateEntity(EstoqueUpdateRequestDto request, Estoque entity) {
        entity.setCodigo(request.codigo());
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setStatusEstoque(request.statusEstoque());
        entity.setEstoquePaiId(request.estoquePaiId());
    }
}