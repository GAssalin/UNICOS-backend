package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    /**
     * Cria uma nova categoria.
     *
     * @param request Dados da categoria a ser criada.
     * @return CategoriaResponseDTO representando a categoria criada.
     */
    CategoriaResponse salvar(CategoriaRequest request);

    /**
     * Atualiza os dados de uma categoria existente.
     *
     * @param id      Identificador da categoria.
     * @param request Dados atualizados da categoria.
     * @return CategoriaResponseDTO com os dados atualizados.
     */
    CategoriaResponse atualizar(Long id, CategoriaRequest request);

    /**
     * Busca uma categoria pelo ID.
     *
     * @param id Identificador da categoria.
     * @return CategoriaResponse, se encontrada.
     */
    Optional<CategoriaResponse> buscarPorId(Long id);

    /**
     * Lista todas as categorias.
     *
     * @return Lista de CategoriaResponse.
     */
    List<CategoriaResponse> listarTodas();

    /**
     * Lista todas as categorias de forma simplificada (id + nome).
     *
     * @return Lista de CategoriaListDTO.
     */
    List<CategoriaListDTO> listarSimples();

    /**
     * Busca categorias cujo nome contenha determinado termo (busca parcial).
     *
     * @param nome Termo de busca.
     * @return Lista de CategoriaResponseDTO que correspondem ao nome informado.
     */
    List<CategoriaResponse> buscarPorNome(String nome);

    /**
     * Exclui uma categoria pelo ID.
     *
     * @param id Identificador da categoria.
     */
    void deletar(Long id);

    /**
     * Verifica se já existe uma categoria com o nome informado.
     *
     * @param nome Nome da categoria.
     * @return true se o nome já estiver cadastrado, false caso contrário.
     */
    boolean existePorNome(String nome);
}