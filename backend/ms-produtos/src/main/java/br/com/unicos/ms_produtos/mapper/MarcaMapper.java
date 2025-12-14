package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.marca.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.marca.MarcaResponse;
import br.com.unicos.ms_produtos.model.Marca;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre Marca
 * e seus respectivos DTOs.
 */
@Component
public class MarcaMapper {

    /**
     * Converte entidade para DTO de resposta detalhada.
     */
    public MarcaResponse toResponse(Marca entity) {
        return new MarcaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPaisOrigem()
        );
    }

    /**
     * Converte entidade para DTO de listagem.
     */
    public MarcaListDTO toListDTO(Marca entity) {
        return new MarcaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getPaisOrigem()
        );
    }
}
