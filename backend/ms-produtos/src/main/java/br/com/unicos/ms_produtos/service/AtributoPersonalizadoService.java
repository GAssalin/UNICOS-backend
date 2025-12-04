package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos
 * atributos personalizados vinculados às categorias de produtos.
 *
 * <p>
 * Os atributos personalizados permitem que categorias possuam
 * propriedades específicas, como "Cor", "Tamanho", "Material", etc.
 * </p>
 */
public interface AtributoPersonalizadoService {

    /**
     * Cria um novo atributo personalizado vinculado a uma categoria.
     *
     * @param request dados do atributo a ser criado.
     * @return DTO contendo as informações do atributo criado.
     */
    AtributoPersonalizadoResponse criar(AtributoPersonalizadoRequest request);

    /**
     * Atualiza um atributo personalizado existente.
     *
     * @param id      identificador do atributo a ser atualizado.
     * @param request novos dados para o atributo.
     * @return DTO contendo as informações atualizadas.
     */
    AtributoPersonalizadoResponse atualizar(Long id, AtributoPersonalizadoRequest request);

    /**
     * Remove um atributo personalizado pelo ID.
     *
     * @param id identificador do atributo.
     */
    void excluir(Long id);

    /**
     * Obtém um atributo personalizado pelo ID.
     *
     * @param id identificador do atributo.
     * @return DTO detalhado do atributo, se encontrado.
     */
    Optional<AtributoPersonalizadoResponse> buscarPorId(Long id);

    /**
     * Lista todos os atributos personalizados existentes.
     *
     * @return lista simplificada de atributos personalizados.
     */
    List<AtributoPersonalizadoListDTO> listarTodos();

    /**
     * Lista todos os atributos vinculados a uma categoria específica.
     *
     * @param categoriaId ID da categoria.
     * @return lista simplificada de atributos da categoria.
     */
    List<AtributoPersonalizadoListDTO> listarPorCategoria(Long categoriaId);
}
