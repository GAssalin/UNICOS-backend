package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.model.Categoria;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper responsável pela conversão entre Categoria
 * e seus respectivos DTOs.
 */
@Component
public class CategoriaMapper {

    /**
     * Converte Categoria para DTO de resposta detalhada,
     * incluindo subcategorias.
     */
    public CategoriaResponse toResponse(Categoria entity) {

        List<CategoriaListDTO> subcategorias = entity.getSubcategorias() == null
                ? List.of()
                : entity.getSubcategorias()
                .stream()
                .map(this::toListDTO)
                .toList();

        return new CategoriaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getCategoriaPai() != null ? entity.getCategoriaPai().getId() : null,
                entity.getAtivo(),
                subcategorias
        );
    }

    /**
     * Converte Categoria para DTO de listagem.
     */
    public CategoriaListDTO toListDTO(Categoria entity) {
        return new CategoriaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCategoriaPai() != null ? entity.getCategoriaPai().getId() : null,
                entity.getAtivo()
        );
    }
}
