package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.PessoaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaResponse;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio relacionadas à entidade {@link br.com.unicos.ms_pessoas.model.Pessoa}.
 */
public interface PessoaService {

    /**
     * Cria uma nova pessoa.
     *
     * @param request dados da pessoa
     * @return pessoa criada
     */
    PessoaResponse salvar(PessoaRequest request);

    /**
     * Atualiza os dados de uma pessoa existente.
     *
     * @param id identificador da pessoa
     * @param request dados atualizados
     * @return pessoa atualizada
     */
    PessoaResponse atualizar(Long id, PessoaRequest request);

    /**
     * Lista todas as pessoas.
     *
     * @return lista de pessoas
     */
    List<PessoaResponse> listarTodos();

    /**
     * Busca uma pessoa pelo ID.
     *
     * @param id identificador
     * @return pessoa encontrada
     */
    Optional<PessoaResponse> buscarPorId(Long id);

    /**
     * Exclui uma pessoa pelo ID.
     *
     * @param id identificador
     */
    void excluir(Long id);

    /**
     * Lista todas as pessoas por tipo (Física ou Jurídica).
     *
     * @param tipo tipo de pessoa
     * @return lista de pessoas do tipo informado
     */
    List<PessoaResponse> listarPorTipo(TipoPessoa tipo);
}
