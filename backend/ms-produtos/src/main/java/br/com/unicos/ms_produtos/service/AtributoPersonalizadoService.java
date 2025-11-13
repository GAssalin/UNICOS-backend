package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade {@link br.com.unicos.ms_produtos.model.AtributoPersonalizado}.
 *
 * <p>
 * Fornece métodos para gerenciamento dos atributos configuráveis
 * das categorias de produtos, como "Cor", "Tamanho" e "Material".
 * </p>
 */
public interface AtributoPersonalizadoService {

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo atributo personalizado para uma categoria.
     *
     * @param request Dados do atributo a ser criado.
     * @return {@link AtributoPersonalizadoResponse} com os dados do atributo criado.
     */
    AtributoPersonalizadoResponse salvar(AtributoPersonalizadoRequest request);

    /**
     * Atualiza um atributo personalizado existente.
     *
     * @param id      Identificador do atributo.
     * @param request Dados atualizados do atributo.
     * @return {@link AtributoPersonalizadoResponse} com os dados atualizados.
     */
    AtributoPersonalizadoResponse atualizar(Long id, AtributoPersonalizadoRequest request);

    /**
     * Busca um atributo personalizado pelo seu ID.
     *
     * @param id Identificador do atributo.
     * @return {@link Optional} contendo o atributo, se encontrado.
     */
    Optional<AtributoPersonalizadoResponse> buscarPorId(Long id);

    /**
     * Lista todos os atributos personalizados cadastrados no sistema.
     *
     * @return Lista de {@link AtributoPersonalizadoResponse}.
     */
    List<AtributoPersonalizadoResponse> listarTodos();

    /**
     * Exclui um atributo personalizado pelo ID.
     *
     * @param id Identificador do atributo.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 MÉTODOS ESPECÍFICOS
    // ==================================

    /**
     * Lista todos os atributos personalizados vinculados a uma categoria específica.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de {@link AtributoPersonalizadoResponse} da categoria.
     */
    List<AtributoPersonalizadoResponse> listarPorCategoria(Long categoriaId);

    /**
     * Busca atributos personalizados cujo nome contenha o termo informado.
     *
     * @param nome Termo de busca parcial (case insensitive).
     * @return Lista simplificada de {@link AtributoPersonalizadoListDTO}.
     */
    List<AtributoPersonalizadoListDTO> buscarPorNomeContendo(String nome);
}
