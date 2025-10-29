package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.dto.AtributoPersonalizadoListDTO;
import br.com.erp.ms_produtos.dto.AtributoPersonalizadoRequest;
import br.com.erp.ms_produtos.dto.AtributoPersonalizadoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade AtributoPersonalizado.
 */
public interface AtributoPersonalizadoService {

    /**
     * Cria um novo atributo personalizado para um produto.
     *
     * @param request Dados do atributo a ser criado.
     * @return AtributoPersonalizadoResponse criado.
     */
    AtributoPersonalizadoResponse salvar(AtributoPersonalizadoRequest request);

    /**
     * Atualiza um atributo personalizado existente.
     *
     * @param id      Identificador do atributo.
     * @param request Dados atualizados do atributo.
     * @return AtributoPersonalizadoResponse atualizado.
     */
    AtributoPersonalizadoResponse atualizar(Long id, AtributoPersonalizadoRequest request);

    /**
     * Busca um atributo personalizado pelo seu ID.
     *
     * @param id Identificador do atributo.
     * @return Optional contendo o atributo, se encontrado.
     */
    Optional<AtributoPersonalizadoResponse> buscarPorId(Long id);

    /**
     * Lista todos os atributos personalizados cadastrados.
     *
     * @return Lista de AtributoPersonalizadoResponse.
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
     * Lista todos os atributos personalizados de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de AtributoPersonalizadoResponse.
     */
    List<AtributoPersonalizadoResponse> listarPorProduto(Long produtoId);

    /**
     * Busca todos os atributos cujo nome contenha o termo informado.
     *
     * @param nome Termo de busca.
     * @return Lista de AtributoPersonalizadoListDTO.
     */
    List<AtributoPersonalizadoListDTO> buscarPorNomeContendo(String nome);

    /**
     * Verifica se já existe um atributo com o mesmo nome dentro de um produto.
     *
     * @param produtoId ID do produto.
     * @param nome      Nome do atributo.
     * @return true se já existir, false caso contrário.
     */
    boolean verificarDuplicidade(Long produtoId, String nome);
}