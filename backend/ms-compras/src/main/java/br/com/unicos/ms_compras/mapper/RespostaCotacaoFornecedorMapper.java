package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.RespostaCotacaoFornecedorDto;
import br.com.unicos.ms_compras.model.RespostaCotacaoFornecedor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link RespostaCotacaoFornecedor}
 * e {@link RespostaCotacaoFornecedorDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não resolve relacionamentos automaticamente (CotacaoCompra/Fornecedor/CondicaoPagamento são resolvidos no service)
 * - Não manipula coleções filhas (itens)
 * - Não altera tenant/auditoria
 * - Sem regras de negócio (ex.: cálculo totalProposto)
 * </p>
 */
@Component
@RequiredArgsConstructor
public class RespostaCotacaoFornecedorMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO.
     *
     * <p>
     * Itens NÃO são retornados aqui (use DTO detalhado/endpoint específico).
     * </p>
     */
    public RespostaCotacaoFornecedorDto toResponse(RespostaCotacaoFornecedor entity) {
        if (entity == null) return null;

        Long cotacaoCompraId = null;
        if (entity.getCotacaoCompra() != null) {
            cotacaoCompraId = entity.getCotacaoCompra().getId();
        }

        Long fornecedorId = null;
        if (entity.getFornecedor() != null) {
            fornecedorId = entity.getFornecedor().getId();
        }

        Long condicaoPagamentoId = null;
        if (entity.getCondicaoPagamento() != null) {
            condicaoPagamentoId = entity.getCondicaoPagamento().getId();
        }

        return new RespostaCotacaoFornecedorDto(
                entity.getId(),
                cotacaoCompraId,
                fornecedorId,
                entity.getStatus(),
                entity.getRespondidoEm(),
                condicaoPagamentoId,
                entity.getTotalProposto(),
                entity.getFrete(),
                entity.getObservacao()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code cotacaoCompra}, {@code fornecedor}, {@code condicaoPagamento} e {@code itens}.
     * O service deve buscar as entidades por id e associar corretamente, além de montar/sincronizar itens.
     * </p>
     */
    public RespostaCotacaoFornecedor toEntity(RespostaCotacaoFornecedorDto dto) {
        if (dto == null) return null;

        RespostaCotacaoFornecedor entity = mapper.map(dto, RespostaCotacaoFornecedor.class);
        entity.setCotacaoCompra(null);       // relacionamento resolvido no service
        entity.setFornecedor(null);          // relacionamento resolvido no service
        entity.setCondicaoPagamento(null);   // relacionamento resolvido no service (opcional)
        // itens: responsabilidade do service (montagem/sync)
        return entity;
    }

    /**
     * Atualiza entidade existente com base no DTO.
     *
     * <p>
     * Não permite trocar vínculos com {@code cotacaoCompra} e {@code fornecedor}.
     * Vincular/alterar {@code condicaoPagamento} eu recomendo fazer no service também (por id).
     * </p>
     */
    public void updateEntity(RespostaCotacaoFornecedorDto dto, RespostaCotacaoFornecedor entity) {
        if (dto == null || entity == null) return;

        entity.setStatus(dto.status());
        entity.setRespondidoEm(dto.respondidoEm());
        entity.setTotalProposto(dto.totalProposto());
        entity.setFrete(dto.frete());
        entity.setObservacao(dto.observacao());
        // condicaoPagamento: responsabilidade do service (resolve por id e seta)
    }
}