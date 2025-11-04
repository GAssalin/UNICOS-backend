package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.TipoRelacaoPessoaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para tipos de relação de pessoa (Cliente, Fornecedor, etc.).
 */
public interface TipoRelacaoPessoaService {

    /**
     * Cria tipo de relação.
     *
     * @param request dados do tipo
     * @return tipo criado
     */
    TipoRelacaoPessoaResponse salvar(TipoRelacaoPessoaRequest request);

    /**
     * Atualiza tipo de relação.
     *
     * @param id      ID do tipo
     * @param request dados atualizados
     * @return tipo atualizado
     */
    TipoRelacaoPessoaResponse atualizar(Long id, TipoRelacaoPessoaRequest request);

    /**
     * Lista todos os tipos.
     *
     * @return lista de tipos
     */
    List<TipoRelacaoPessoaResponse> listarTodos();

    /**
     * Busca tipo por ID.
     *
     * @param id ID do tipo
     * @return tipo (se encontrado)
     */
    Optional<TipoRelacaoPessoaResponse> buscarPorId(Long id);

    /**
     * Exclui tipo por ID.
     *
     * @param id ID do tipo
     */
    void excluir(Long id);
}
