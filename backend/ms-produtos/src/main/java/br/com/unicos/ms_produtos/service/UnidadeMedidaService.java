package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.UnidadeMedidaRequest;
import br.com.unicos.ms_produtos.dto.UnidadeMedidaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade UnidadeMedida.
 *
 * Define métodos para criação, atualização, exclusão e consultas específicas.
 */
public interface UnidadeMedidaService {

    /**
     * Cria uma nova unidade de medida.
     *
     * @param request Dados da unidade a ser criada.
     * @return UnidadeMedidaResponse criada.
     */
    UnidadeMedidaResponse salvar(UnidadeMedidaRequest request);

    /**
     * Atualiza uma unidade de medida existente.
     *
     * @param id      Identificador da unidade.
     * @param request Dados atualizados.
     * @return UnidadeMedidaResponse atualizada.
     */
    UnidadeMedidaResponse atualizar(Long id, UnidadeMedidaRequest request);

    /**
     * Busca uma unidade de medida pelo seu ID.
     *
     * @param id Identificador da unidade.
     * @return Optional contendo a unidade, se encontrada.
     */
    Optional<UnidadeMedidaResponse> buscarPorId(Long id);

    /**
     * Lista todas as unidades de medida cadastradas.
     *
     * @return Lista de UnidadeMedidaResponse.
     */
    List<UnidadeMedidaResponse> listarTodas();

    /**
     * Exclui uma unidade de medida.
     *
     * @param id Identificador da unidade.
     */
    void deletar(Long id);

    // ==================================
    // 🔹 MÉTODOS ESPECÍFICOS
    // ==================================

    /**
     * Busca uma unidade de medida pelo nome.
     *
     * @param nome Nome da unidade.
     * @return Optional contendo a unidade, se encontrada.
     */
    Optional<UnidadeMedidaResponse> buscarPorNome(String nome);

    /**
     * Busca uma unidade de medida pela sigla.
     *
     * @param sigla Sigla da unidade.
     * @return Optional contendo a unidade, se encontrada.
     */
    Optional<UnidadeMedidaResponse> buscarPorSigla(String sigla);

    /**
     * Lista todas as unidades cujo nome contenha o termo informado.
     *
     * @param nome Termo de busca.
     * @return Lista de UnidadeMedidaListDTO.
     */
    List<UnidadeMedidaListDTO> buscarPorNomeContendo(String nome);

    /**
     * Lista todas as unidades ordenadas alfabeticamente.
     *
     * @return Lista de UnidadeMedidaListDTO.
     */
    List<UnidadeMedidaListDTO> listarSimples();

    /**
     * Verifica se já existe uma unidade com a sigla informada.
     *
     * @param sigla Sigla da unidade.
     * @return true se já existir, false caso contrário.
     */
    boolean verificarSiglaExistente(String sigla);
}