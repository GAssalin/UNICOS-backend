package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.EnderecoFornecedorDto;
import br.com.unicos.ms_compras.model.EnderecoFornecedor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link EnderecoFornecedor}
 * e {@link EnderecoFornecedorDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamento automaticamente (Fornecedor é resolvido no service)
 * - Não altera tenant/auditoria
 * - Não contém regras de negócio
 * </p>
 */
@Component
@RequiredArgsConstructor
public class EnderecoFornecedorMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     */
    public EnderecoFornecedorDto toResponse(EnderecoFornecedor entity) {
        if (entity == null) return null;

        Long fornecedorId = null;
        if (entity.getFornecedor() != null) {
            fornecedorId = entity.getFornecedor().getId();
        }

        return new EnderecoFornecedorDto(
                entity.getId(),
                fornecedorId,
                entity.getTipo(),
                entity.getCep(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getUf(),
                entity.getObservacao()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code fornecedor}.
     * O service deve buscar o Fornecedor por id e associar corretamente.
     * </p>
     */
    public EnderecoFornecedor toEntity(EnderecoFornecedorDto dto) {
        if (dto == null) return null;

        EnderecoFornecedor entity = mapper.map(dto, EnderecoFornecedor.class);
        entity.setFornecedor(null); // relacionamento resolvido no service
        return entity;
    }

    /**
     * Atualiza entidade existente.
     *
     * <p>
     * Não permite trocar o vínculo com {@code fornecedor}.
     * Caso necessário, tratar como regra de negócio no service.
     * </p>
     */
    public void updateEntity(EnderecoFornecedorDto dto, EnderecoFornecedor entity) {
        if (dto == null || entity == null) return;

        entity.setTipo(dto.tipo());
        entity.setCep(dto.cep());
        entity.setLogradouro(dto.logradouro());
        entity.setNumero(dto.numero());
        entity.setComplemento(dto.complemento());
        entity.setBairro(dto.bairro());
        entity.setCidade(dto.cidade());
        entity.setUf(dto.uf());
        entity.setObservacao(dto.observacao());
    }
}