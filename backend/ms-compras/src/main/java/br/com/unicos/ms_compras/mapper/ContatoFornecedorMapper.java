package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.ContatoFornecedorDto;
import br.com.unicos.ms_compras.model.ContatoFornecedor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link ContatoFornecedor}
 * e {@link ContatoFornecedorDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamento com Fornecedor automaticamente
 * - Não altera tenant/auditoria
 * - Não contém regra de negócio
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ContatoFornecedorMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     */
    public ContatoFornecedorDto toResponse(ContatoFornecedor entity) {
        if (entity == null) return null;

        Long fornecedorId = null;
        if (entity.getFornecedor() != null) {
            fornecedorId = entity.getFornecedor().getId();
        }

        return new ContatoFornecedorDto(
                entity.getId(),
                fornecedorId,
                entity.getNome(),
                entity.getCargo(),
                entity.getTelefone(),
                entity.getCelular(),
                entity.getEmail(),
                entity.getPrincipal(),
                entity.getObservacao()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta o fornecedor automaticamente.
     * O service deve buscar o fornecedor e associar corretamente.
     * </p>
     */
    public ContatoFornecedor toEntity(ContatoFornecedorDto dto) {
        if (dto == null) return null;

        ContatoFornecedor entity = mapper.map(dto, ContatoFornecedor.class);
        entity.setFornecedor(null); // relacionamento resolvido no service
        return entity;
    }

    /**
     * Atualiza entidade existente.
     *
     * <p>
     * Não permite trocar o fornecedor.
     * Caso seja necessário, trate como operação de negócio no service.
     * </p>
     */
    public void updateEntity(ContatoFornecedorDto dto, ContatoFornecedor entity) {
        if (dto == null || entity == null) return;

        entity.setNome(dto.nome());
        entity.setCargo(dto.cargo());
        entity.setTelefone(dto.telefone());
        entity.setCelular(dto.celular());
        entity.setEmail(dto.email());
        entity.setPrincipal(dto.principal());
        entity.setObservacao(dto.observacao());
    }
}