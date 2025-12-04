package br.com.unicos.ms_produtos.mapper;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.model.Categoria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Responsável por conversões entre Categoria e seus DTOs
 * utilizando ModelMapper, com ajustes manuais quando necessário
 * (especialmente para hierarquia).
 */
@Component
@RequiredArgsConstructor
public class CategoriaMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade Categoria em CategoriaResponse,
     * incluindo subcategorias e categoriaPaiId.
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
                subcategorias,
                entity.getAtivo()
        );
    }

    /**
     * Converte Categoria para CategoriaListDTO.
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
