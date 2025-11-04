package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.EnderecoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.EnderecoPessoaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciamento de endereços de pessoas.
 */
public interface EnderecoPessoaService {

    /**
     * Cria endereço.
     *
     * @param request dados do endereço
     * @return endereço criado
     */
    EnderecoPessoaResponse salvar(EnderecoPessoaRequest request);

    /**
     * Atualiza endereço.
     *
     * @param id      ID do endereço
     * @param request dados atualizados
     * @return endereço atualizado
     */
    EnderecoPessoaResponse atualizar(Long id, EnderecoPessoaRequest request);

    /**
     * Lista endereços por pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de endereços
     */
    List<EnderecoPessoaResponse> listarPorPessoa(Long pessoaId);

    /**
     * Busca endereço por ID.
     *
     * @param id ID do endereço
     * @return endereço (se encontrado)
     */
    Optional<EnderecoPessoaResponse> buscarPorId(Long id);

    /**
     * Exclui endereço por ID.
     *
     * @param id ID do endereço
     */
    void excluir(Long id);
}
