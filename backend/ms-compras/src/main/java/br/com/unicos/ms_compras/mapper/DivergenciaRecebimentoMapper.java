package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.DivergenciaRecebimentoDto;
import br.com.unicos.ms_compras.model.DivergenciaRecebimento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link DivergenciaRecebimento} e {@link DivergenciaRecebimentoDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamento automaticamente (ItemRecebimentoCompra é resolvido no service)
 * - Não altera tenant/auditoria
 * - Sem regras de negócio
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DivergenciaRecebimentoMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     */
    public DivergenciaRecebimentoDto toResponse(DivergenciaRecebimento entity) {
        if (entity == null) return null;

        Long itemRecebimentoCompraId = null;
        if (entity.getItemRecebimentoCompra() != null) {
            itemRecebimentoCompraId = entity.getItemRecebimentoCompra().getId();
        }

        return new DivergenciaRecebimentoDto(
                entity.getId(),
                itemRecebimentoCompraId,
                entity.getTipo(),
                entity.getDescricao(),
                entity.getQuantidadeDivergente()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code itemRecebimentoCompra} aqui.
     * O service deve buscar o ItemRecebimentoCompra por id e associar corretamente.
     * </p>
     */
    public DivergenciaRecebimento toEntity(DivergenciaRecebimentoDto dto) {
        if (dto == null) return null;

        DivergenciaRecebimento entity = mapper.map(dto, DivergenciaRecebimento.class);
        entity.setItemRecebimentoCompra(null); // relacionamento resolvido no service
        return entity;
    }

    /**
     * Atualiza entidade existente.
     *
     * <p>
     * Não permite trocar o vínculo com {@code itemRecebimentoCompra}.
     * Caso necessário, trate como operação de negócio no service.
     * </p>
     */
    public void updateEntity(DivergenciaRecebimentoDto dto, DivergenciaRecebimento entity) {
        if (dto == null || entity == null) return;

        entity.setTipo(dto.tipo());
        entity.setDescricao(dto.descricao());
        entity.setQuantidadeDivergente(dto.quantidadeDivergente());
    }
}