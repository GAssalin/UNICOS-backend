package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoResponse;
import br.com.unicos.ms_produtos.model.HistoricoPreco;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre HistoricoPreco
 * e seus respectivos DTOs.
 */
@Component
public class HistoricoPrecoMapper {

    /**
     * Converte a entidade para DTO de resposta detalhada.
     */
    public HistoricoPrecoResponse toResponse(HistoricoPreco entity) {
        return new HistoricoPrecoResponse(
                entity.getId(),
                entity.getProduto().getId(),
                entity.getPrecoAnterior(),
                entity.getNovoPreco(),
                entity.getDataAlteracao(),
                entity.getMotivo()
        );
    }

    /**
     * Converte a entidade para DTO de listagem.
     */
    public HistoricoPrecoListDTO toListDTO(HistoricoPreco entity) {
        return new HistoricoPrecoListDTO(
                entity.getId(),
                entity.getPrecoAnterior(),
                entity.getNovoPreco(),
                entity.getDataAlteracao()
        );
    }
}
