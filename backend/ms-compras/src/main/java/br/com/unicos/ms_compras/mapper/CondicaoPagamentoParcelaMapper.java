package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.CondicaoPagamentoParcelaDto;
import br.com.unicos.ms_compras.model.CondicaoPagamentoParcela;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre {@link CondicaoPagamentoParcela} e {@link CondicaoPagamentoParcelaDto}.
 *
 * <p>
 * Padrão UniCoS:
 * - Não altera tenant/auditoria
 * - Não resolve relacionamentos automaticamente (condicaoPagamento é resolvido no service)
 * - Não contém regras de negócio (ex.: soma de percentuais = 100)
 * </p>
 */
@Component
@RequiredArgsConstructor
public class CondicaoPagamentoParcelaMapper {

    private final ModelMapper mapper;

    /**
     * Converte entidade para DTO de resposta.
     */
    public CondicaoPagamentoParcelaDto toResponse(CondicaoPagamentoParcela entity) {
        if (entity == null) return null;

        Long condicaoPagamentoId = null;
        if (entity.getCondicaoPagamento() != null) {
            condicaoPagamentoId = entity.getCondicaoPagamento().getId();
        }

        return new CondicaoPagamentoParcelaDto(
                entity.getId(),
                condicaoPagamentoId,
                entity.getOrdem(),
                entity.getDiasAposEmissao(),
                entity.getPercentual()
        );
    }

    /**
     * Converte DTO para entidade.
     *
     * <p>
     * NÃO seta {@code condicaoPagamento} aqui.
     * O service deve resolver a CondicaoPagamento (por id) e associar corretamente.
     * </p>
     */
    public CondicaoPagamentoParcela toEntity(CondicaoPagamentoParcelaDto dto) {
        if (dto == null) return null;

        CondicaoPagamentoParcela entity = mapper.map(dto, CondicaoPagamentoParcela.class);
        entity.setCondicaoPagamento(null); // relacionamento é responsabilidade do service
        return entity;
    }

    /**
     * Atualiza entidade existente com base no DTO.
     *
     * <p>
     * Não permite trocar o vínculo de {@code condicaoPagamento}.
     * Se isso for necessário, trate como operação de negócio no service.
     * </p>
     */
    public void updateEntity(CondicaoPagamentoParcelaDto dto, CondicaoPagamentoParcela entity) {
        if (dto == null || entity == null) return;

        entity.setOrdem(dto.ordem());
        entity.setDiasAposEmissao(dto.diasAposEmissao());
        entity.setPercentual(dto.percentual());
    }
}