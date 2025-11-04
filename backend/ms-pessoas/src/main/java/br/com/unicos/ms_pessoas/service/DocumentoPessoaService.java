package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.dto.DocumentoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.DocumentoPessoaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service para documentos de pessoas.
 */
public interface DocumentoPessoaService {

    /**
     * Cria documento.
     *
     * @param request dados do documento
     * @return documento criado
     */
    DocumentoPessoaResponse salvar(DocumentoPessoaRequest request);

    /**
     * Atualiza documento.
     *
     * @param id      ID do documento
     * @param request dados atualizados
     * @return documento atualizado
     */
    DocumentoPessoaResponse atualizar(Long id, DocumentoPessoaRequest request);

    /**
     * Lista documentos por pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de documentos
     */
    List<DocumentoPessoaResponse> listarPorPessoa(Long pessoaId);

    /**
     * Busca documento por ID.
     *
     * @param id ID do documento
     * @return documento (se encontrado)
     */
    Optional<DocumentoPessoaResponse> buscarPorId(Long id);

    /**
     * Exclui documento por ID.
     *
     * @param id ID do documento
     */
    void excluir(Long id);
}
