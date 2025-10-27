package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.dto.CategoriaRequestDTO;
import br.com.erp.ms_produtos.dto.CategoriaResponseDTO;
import br.com.erp.ms_produtos.dto.CategoriaListDTO;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    /**
     * Cria uma nova categoria.
     *
     * @param request Dados da categoria a ser criada.
     * @return CategoriaResponseDTO representando a categoria criada.
     */
    CategoriaResponseDTO salvar(CategoriaRequestDTO request);

    /**
     * Atualiza os dados de uma categoria existente.
     *
     * @param id      Identificador da categoria.
     * @param request Dados atualizados da categoria.
     * @return CategoriaResponseDTO com os dados atualizados.
     */
    CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO request);

    /**
     * Busca uma categoria pelo ID.
     *
     * @param id Identificador da categoria.
     * @return CategoriaResponse, se encontrada.
     */
    Optional<CategoriaResponseDTO> buscarPorId(Long id);

    /**
     * Lista todas as categorias.
     *
     * @return Lista de CategoriaResponse.
     */
    List<CategoriaResponseDTO> listarTodas();

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
    List<CategoriaResponseDTO> buscarPorNome(String nome);

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