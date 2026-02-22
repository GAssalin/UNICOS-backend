package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.CondicaoPagamentoDto;
import br.com.unicos.ms_compras.model.CondicaoPagamento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper da entidade CondicaoPagamento.
 */
@Component
@RequiredArgsConstructor
public class CondicaoPagamentoMapper {

    private final ModelMapper mapper;

    public CondicaoPagamentoDto toResponse(CondicaoPagamento entity) {
        return new CondicaoPagamentoDto(
                entity.getId(),
                entity.getDescricao(),
                entity.getNumeroParcelas(),
                entity.getAtivo()
        );
    }

    public CondicaoPagamento toEntity(CondicaoPagamentoDto dto) {
        return mapper.map(dto, CondicaoPagamento.class);
    }

    public void updateEntity(CondicaoPagamentoDto dto, CondicaoPagamento entity) {
        entity.setDescricao(dto.descricao());
        entity.setNumeroParcelas(dto.numeroParcelas());
        entity.setAtivo(dto.ativo());
    }
}