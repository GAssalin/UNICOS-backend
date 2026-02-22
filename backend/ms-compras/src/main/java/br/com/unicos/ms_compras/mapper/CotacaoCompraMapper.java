package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.CotacaoCompraDto;
import br.com.unicos.ms_compras.model.CotacaoCompra;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link CotacaoCompra} e {@link CotacaoCompraDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamentos automaticamente (PedidoCompra é resolvido no service)
 * - Não manipula coleções filhas (itens)
 * - Não altera tenant/auditoria
 * - Sem regras de negócio
 * </p>
 */
@Component
@RequiredArgsConstructor
public class CotacaoCompraMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     *
     * <p>
     * Itens NÃO são retornados aqui (use DTO detalhado/endpoint específico).
     * </p>
     */
    public CotacaoCompraDto toResponse(CotacaoCompra entity) {
        if (entity == null) return null;

        Long pedidoCompraId = null;
        if (entity.getPedidoCompra() != null) {
            pedidoCompraId = entity.getPedidoCompra().getId();
        }

        return new CotacaoCompraDto(
                entity.getId(),
                entity.getCodigo(),
                entity.getDataAbertura(),
                entity.getDataValidade(),
                entity.getStatus(),
                entity.getObservacao(),
                pedidoCompraId
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code pedidoCompra} nem {@code itens}.
     * O service deve resolver o relacionamento e montar/sincronizar os itens.
     * </p>
     */
    public CotacaoCompra toEntity(CotacaoCompraDto dto) {
        if (dto == null) return null;

        CotacaoCompra entity = mapper.map(dto, CotacaoCompra.class);
        entity.setPedidoCompra(null); // relacionamento resolvido no service
        // itens: responsabilidade do service (montagem/sync)
        return entity;
    }

    /**
     * Atualiza entidade existente com base no DTO.
     *
     * <p>
     * Não troca {@code itens}. Não resolve {@code pedidoCompra} automaticamente.
     * Vincular pedido gerado deve ser caso de uso do service.
     * </p>
     */
    public void updateEntity(CotacaoCompraDto dto, CotacaoCompra entity) {
        if (dto == null || entity == null) return;

        entity.setCodigo(dto.codigo());
        entity.setDataAbertura(dto.dataAbertura());
        entity.setDataValidade(dto.dataValidade());
        entity.setStatus(dto.status());
        entity.setObservacao(dto.observacao());
        // pedidoCompra: responsabilidade do service
    }
}