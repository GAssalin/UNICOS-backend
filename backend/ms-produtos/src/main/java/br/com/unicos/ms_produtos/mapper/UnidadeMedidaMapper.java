package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaResponse;
import br.com.unicos.ms_produtos.model.UnidadeMedida;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre UnidadeMedida
 * e seus respectivos DTOs.
 */
@Component
public class UnidadeMedidaMapper {

    /**
     * Converte entidade para DTO de resposta detalhada.
     */
    public UnidadeMedidaResponse toResponse(UnidadeMedida entity) {
        return new UnidadeMedidaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getSigla(),
                entity.getDescricao(),
                entity.getAtivo()
        );
    }

    /**
     * Converte entidade para DTO de listagem.
     */
    public UnidadeMedidaListDTO toListDTO(UnidadeMedida entity) {
        return new UnidadeMedidaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getSigla(),
                entity.getAtivo()
        );
    }
}
