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
                entity.getCodigo(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getParcelado()
        );
    }

    public CondicaoPagamento toEntity(CondicaoPagamentoDto dto) {
        // OK usar ModelMapper aqui, mas atenção ao campo "parcelas" (ver observação abaixo).
        return mapper.map(dto, CondicaoPagamento.class);
    }

    public void updateEntity(CondicaoPagamentoDto dto, CondicaoPagamento entity) {
        entity.setCodigo(dto.codigo());
        entity.setNome(dto.nome());
        entity.setDescricao(dto.descricao());
        entity.setParcelado(dto.parcelado());
        // "parcelas" não deve ser mexido aqui sem uma regra clara (ver item 4).
    }
}