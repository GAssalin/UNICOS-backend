package br.com.unicos.ms_produtos.service.interfaces;

import br.com.unicos.ms_produtos.dto.categoria.CategoriaListDTO;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaRequest;
import br.com.unicos.ms_produtos.dto.categoria.CategoriaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas
 * à gestão de categorias de produtos.
 *
 * <p>
 * Suporta hierarquia entre categorias (categoria pai e subcategorias)
 * e controle de status operacional.
 * </p>
 */
public interface CategoriaService {

    /**
     * Cria uma nova categoria.
     *
     * @param request Dados da categoria a ser criada.
     * @return CategoriaResponse representando a categoria criada.
     */
    CategoriaResponse salvar(CategoriaRequest request);

    /**
     * Atualiza os dados de uma categoria existente.
     *
     * @param id      Identificador da categoria.
     * @param request Dados atualizados da categoria.
     * @return CategoriaResponse com os dados atualizados.
     */
    CategoriaResponse atualizar(Long id, CategoriaRequest request);

    /**
     * Busca uma categoria pelo seu identificador.
     *
     * @param id Identificador da categoria.
     * @return CategoriaResponse, se encontrada.
     */
    Optional<CategoriaResponse> buscarPorId(Long id);

    /**
     * Lista todas as categorias cadastradas.
     *
     * @return Lista completa de categorias.
     */
    List<CategoriaResponse> listarTodas();

    /**
     * Lista todas as categorias de forma simplificada
     * (id, nome, categoria pai e status).
     *
     * @return Lista de CategoriaListDTO.
     */
    List<CategoriaListDTO> listarSimples();

    /**
     * Busca categorias cujo nome contenha o termo informado.
     *
     * @param nome Termo de busca.
     * @return Lista de categorias correspondentes.
     */
    List<CategoriaResponse> buscarPorNome(String nome);

    /**
     * Remove uma categoria pelo seu identificador.
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
