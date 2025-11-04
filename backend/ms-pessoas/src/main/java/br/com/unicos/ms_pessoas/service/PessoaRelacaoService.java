package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.PessoaRelacaoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para vínculos/relacionamentos entre pessoas e seus papéis.
 */
public interface PessoaRelacaoService {

    /**
     * Cria vínculo.
     *
     * @param request dados do vínculo
     * @return vínculo criado
     */
    PessoaRelacaoResponse salvar(PessoaRelacaoRequest request);

    /**
     * Atualiza vínculo.
     *
     * @param id      ID do vínculo
     * @param request dados atualizados
     * @return vínculo atualizado
     */
    PessoaRelacaoResponse atualizar(Long id, PessoaRelacaoRequest request);

    /**
     * Lista vínculos por pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de vínculos
     */
    List<PessoaRelacaoResponse> listarPorPessoa(Long pessoaId);

    /**
     * Busca vínculo por ID.
     *
     * @param id ID do vínculo
     * @return vínculo (se encontrado)
     */
    Optional<PessoaRelacaoResponse> buscarPorId(Long id);

    /**
     * Exclui vínculo por ID.
     *
     * @param id ID do vínculo
     */
    void excluir(Long id);
}
