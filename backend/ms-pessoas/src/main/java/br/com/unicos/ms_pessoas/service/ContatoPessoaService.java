package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.ContatoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.ContatoPessoaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para contatos de pessoas.
 */
public interface ContatoPessoaService {

    /**
     * Cria contato.
     *
     * @param request dados do contato
     * @return contato criado
     */
    ContatoPessoaResponse salvar(ContatoPessoaRequest request);

    /**
     * Atualiza contato.
     *
     * @param id      ID do contato
     * @param request dados atualizados
     * @return contato atualizado
     */
    ContatoPessoaResponse atualizar(Long id, ContatoPessoaRequest request);

    /**
     * Lista contatos por pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de contatos
     */
    List<ContatoPessoaResponse> listarPorPessoa(Long pessoaId);

    /**
     * Busca contato por ID.
     *
     * @param id ID do contato
     * @return contato (se encontrado)
     */
    Optional<ContatoPessoaResponse> buscarPorId(Long id);

    /**
     * Exclui contato por ID.
     *
     * @param id ID do contato
     */
    void excluir(Long id);
}
