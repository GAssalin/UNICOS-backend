package br.com.erp.ms_produtos.mapper;


import br.com.erp.ms_produtos.dto.CategoriaListDTO;
import br.com.erp.ms_produtos.dto.CategoriaRequestDTO;
import br.com.erp.ms_produtos.dto.CategoriaResponseDTO;
import br.com.erp.ms_produtos.model.Categoria;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitária para conversão entre entidade Categoria e seus DTOs.
 */
public class CategoriaMapper {

    /**
     * Converte um DTO de requisição em uma entidade Categoria.
     *
     * @param dto CategoriaRequestDTO com os dados da requisição.
     * @return Entidade Categoria.
     */
    public static Categoria toEntity(CategoriaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Categoria.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .build();
    }

    /**
     * Converte uma entidade Categoria em um DTO de resposta detalhada.
     *
     * @param entity Entidade Categoria.
     * @return CategoriaResponseDTO.
     */
    public static CategoriaResponseDTO toResponseDTO(Categoria entity) {
        if (entity == null) {
            return null;
        }

        return CategoriaResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .build();
    }

    /**
     * Converte uma entidade Categoria em um DTO simplificado para listagem.
     *
     * @param entity Entidade Categoria.
     * @return CategoriaListDTO.
     */
    public static CategoriaListDTO toListDTO(Categoria entity) {
        if (entity == null) {
            return null;
        }

        return CategoriaListDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .build();
    }

    /**
     * Converte uma lista de entidades Categoria em uma lista de DTOs de listagem.
     *
     * @param entities Lista de entidades Categoria.
     * @return Lista de CategoriaListDTO.
     */
    public static List<CategoriaListDTO> toListDTO(List<Categoria> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(CategoriaMapper::toListDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza uma entidade Categoria existente com os dados de um DTO de requisição.
     *
     * @param entity Entidade Categoria a ser atualizada.
     * @param dto CategoriaRequestDTO com os novos dados.
     */
    public static void updateEntity(Categoria entity, CategoriaRequestDTO dto) {
        if (entity != null && dto != null) {
            entity.setNome(dto.getNome());
            entity.setDescricao(dto.getDescricao());
        }
    }
}