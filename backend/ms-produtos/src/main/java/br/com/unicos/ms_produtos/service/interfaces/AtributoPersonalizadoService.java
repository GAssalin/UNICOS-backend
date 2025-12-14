package br.com.unicos.ms_produtos.service.interfaces;

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
 * Os atributos personalizados permitem que cada categoria possua
 * propriedades específicas, como "Cor", "Tamanho", "Material", etc.,
 * que podem ser aplicadas dinamicamente aos produtos.
 * </p>
 */
public interface AtributoPersonalizadoService {

    /**
     * Cria um novo atributo personalizado vinculado a uma categoria.
     *
     * @param request Dados do atributo a ser criado.
     * @return DTO contendo as informações do atributo criado.
     */
    AtributoPersonalizadoResponse criar(AtributoPersonalizadoRequest request);

    /**
     * Atualiza um atributo personalizado existente.
     *
     * @param id      Identificador do atributo.
     * @param request Novos dados do atributo.
     * @return DTO contendo as informações atualizadas.
     */
    AtributoPersonalizadoResponse atualizar(Long id, AtributoPersonalizadoRequest request);

    /**
     * Remove um atributo personalizado pelo seu identificador.
     *
     * @param id Identificador do atributo.
     */
    void excluir(Long id);

    /**
     * Obtém um atributo personalizado pelo ID.
     *
     * @param id Identificador do atributo.
     * @return DTO detalhado do atributo, se encontrado.
     */
    Optional<AtributoPersonalizadoResponse> buscarPorId(Long id);

    /**
     * Lista todos os atributos personalizados cadastrados.
     *
     * @return Lista simplificada de atributos personalizados.
     */
    List<AtributoPersonalizadoListDTO> listarTodos();

    /**
     * Lista todos os atributos vinculados a uma categoria específica.
     *
     * @param categoriaId ID da categoria.
     * @return Lista simplificada de atributos da categoria.
     */
    List<AtributoPersonalizadoListDTO> listarPorCategoria(Long categoriaId);
}
