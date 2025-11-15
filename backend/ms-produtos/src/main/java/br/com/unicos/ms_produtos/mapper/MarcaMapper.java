package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.marca.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.marca.MarcaResponse;
import br.com.unicos.ms_produtos.model.Marca;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável por conversões entre Marca e seus DTOs.
 */
@Component
public class MarcaMapper {

    /**
     * Converte entidade para o DTO completo.
     */
    public MarcaResponse toResponse(Marca m) {
        return new MarcaResponse(
                m.getId(),
                m.getNome(),
                m.getDescricao(),
                m.getPaisOrigem(),
                true // flag ativo (placeholder, pois Marca não possui campo 'ativo')
        );
    }

    /**
     * Converte entidade para DTO de listagem simples.
     */
    public MarcaListDTO toListDTO(Marca m) {
        return new MarcaListDTO(
                m.getId(),
                m.getNome(),
                m.getPaisOrigem()
        );
    }
}
