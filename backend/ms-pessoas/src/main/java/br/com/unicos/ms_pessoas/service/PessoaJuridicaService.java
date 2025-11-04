package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.PessoaJuridicaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para operações de {@link br.com.unicos.ms_pessoas.model.PessoaJuridica}.
 */
public interface PessoaJuridicaService {

    /**
     * Cria pessoa jurídica.
     *
     * @param request dados da PJ
     * @return PJ criada
     */
    PessoaJuridicaResponse salvar(PessoaJuridicaRequest request);

    /**
     * Atualiza pessoa jurídica.
     *
     * @param id      ID da PJ
     * @param request dados atualizados
     * @return PJ atualizada
     */
    PessoaJuridicaResponse atualizar(Long id, PessoaJuridicaRequest request);

    /**
     * Lista todas as PJs.
     *
     * @return lista de PJs
     */
    List<PessoaJuridicaResponse> listarTodos();

    /**
     * Busca PJ por ID.
     *
     * @param id ID da PJ
     * @return PJ (se encontrada)
     */
    Optional<PessoaJuridicaResponse> buscarPorId(Long id);

    /**
     * Busca PJ por CNPJ.
     *
     * @param cnpj CNPJ formatado
     * @return PJ (se encontrada)
     */
    Optional<PessoaJuridicaResponse> buscarPorCnpj(String cnpj);

    /**
     * Exclui PJ por ID.
     *
     * @param id ID da PJ
     */
    void excluir(Long id);
}
