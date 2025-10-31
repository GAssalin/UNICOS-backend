package br.com.erp.ms_empresa.service;

import br.com.erp.ms_empresa.dto.ContatoEmpresaRequest;
import br.com.erp.ms_empresa.dto.ContatoEmpresaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade ContatoEmpresa.
 *
 * Define os métodos para criação, atualização, listagem e exclusão de contatos corporativos.
 */
public interface ContatoEmpresaService {

    /**
     * Salva um novo contato vinculado a uma empresa.
     *
     * @param request DTO contendo os dados do contato.
     * @return DTO representando o contato salvo.
     */
    ContatoEmpresaResponse salvar(ContatoEmpresaRequest request);

    /**
     * Atualiza um contato existente.
     *
     * @param id ID do contato.
     * @param request DTO contendo os novos dados do contato.
     * @return DTO representando o contato atualizado.
     */
    ContatoEmpresaResponse atualizar(Long id, ContatoEmpresaRequest request);

    /**
     * Busca um contato pelo seu ID.
     *
     * @param id ID do contato.
     * @return Optional contendo o contato, se encontrado.
     */
    Optional<ContatoEmpresaResponse> buscarPorId(Long id);

    /**
     * Lista todos os contatos cadastrados.
     *
     * @return Lista de contatos.
     */
    List<ContatoEmpresaResponse> listarTodos();

    /**
     * Lista todos os contatos de uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de contatos vinculados à empresa.
     */
    List<ContatoEmpresaResponse> listarPorEmpresa(Long empresaId);

    /**
     * Busca contatos cujo nome contenha um determinado termo.
     *
     * @param nome Nome ou parte do nome do contato.
     * @return Lista de contatos correspondentes.
     */
    List<ContatoEmpresaResponse> buscarPorNome(String nome);

    /**
     * Busca um contato pelo e-mail.
     *
     * @param email E-mail do contato.
     * @return Optional contendo o contato, se encontrado.
     */
    Optional<ContatoEmpresaResponse> buscarPorEmail(String email);

    /**
     * Remove um contato pelo ID.
     *
     * @param id ID do contato a ser removido.
     */
    void deletar(Long id);
}