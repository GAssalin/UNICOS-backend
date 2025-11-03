package br.com.unicos.ms_produtos.service;

import br.com.unicos.ms_produtos.dto.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.MarcaRequest;
import br.com.unicos.ms_produtos.dto.MarcaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade Marca.
 */
public interface MarcaService {

    /**
     * Cria uma nova marca.
     *
     * @param request Dados da marca a ser criada.
     * @return MarcaResponse representando a marca criada.
     */
    MarcaResponse salvar(MarcaRequest request);

    /**
     * Atualiza os dados de uma marca existente.
     *
     * @param id      Identificador da marca.
     * @param request Dados atualizados.
     * @return MarcaResponse atualizada.
     */
    MarcaResponse atualizar(Long id, MarcaRequest request);

    /**
     * Busca uma marca pelo ID.
     *
     * @param id Identificador da marca.
     * @return MarcaResponse, se encontrada.
     */
    Optional<MarcaResponse> buscarPorId(Long id);

    /**
     * Lista todas as marcas cadastradas.
     *
     * @return Lista de MarcaResponse.
     */
    List<MarcaResponse> listarTodas();

    /**
     * Lista as marcas de forma simplificada (id + nome).
     *
     * @return Lista de MarcaListDTO.
     */
    List<MarcaListDTO> listarSimples();

    /**
     * Busca marcas cujo nome contenha determinado termo (busca parcial).
     *
     * @param nome Termo de busca.
     * @return Lista de MarcaResponse correspondentes.
     */
    List<MarcaResponse> buscarPorNome(String nome);

    /**
     * Exclui uma marca pelo ID.
     *
     * @param id Identificador da marca.
     */
    void deletar(Long id);

    /**
     * Verifica se já existe uma marca com o nome informado.
     *
     * @param nome Nome da marca.
     * @return true se já existir, false caso contrário.
     */
    boolean existePorNome(String nome);
}