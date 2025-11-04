package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaFisicaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para operações de {@link br.com.unicos.ms_pessoas.model.PessoaFisica}.
 */
public interface PessoaFisicaService {

    /**
     * Cria pessoa física.
     *
     * @param request dados da PF
     * @return PF criada
     */
    PessoaFisicaResponse salvar(PessoaFisicaRequest request);

    /**
     * Atualiza pessoa física.
     *
     * @param id      ID da PF
     * @param request dados atualizados
     * @return PF atualizada
     */
    PessoaFisicaResponse atualizar(Long id, PessoaFisicaRequest request);

    /**
     * Lista todas as PFs.
     *
     * @return lista de PFs
     */
    List<PessoaFisicaResponse> listarTodos();

    /**
     * Busca PF por ID.
     *
     * @param id ID da PF
     * @return PF (se encontrada)
     */
    Optional<PessoaFisicaResponse> buscarPorId(Long id);

    /**
     * Busca PF por CPF.
     *
     * @param cpf CPF formatado
     * @return PF (se encontrada)
     */
    Optional<PessoaFisicaResponse> buscarPorCpf(String cpf);

    /**
     * Exclui PF por ID.
     *
     * @param id ID da PF
     */
    void excluir(Long id);
}
