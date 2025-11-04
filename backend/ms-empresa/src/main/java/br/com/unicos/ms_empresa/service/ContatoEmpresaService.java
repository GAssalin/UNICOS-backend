package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.ContatoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.ContatoEmpresaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link br.com.unicos.ms_empresa.model.ContatoEmpresa}.
 * <p>
 * Define os métodos para criação, atualização, listagem e exclusão de contatos corporativos.
 */
public interface ContatoEmpresaService {

    /**
     * Cria e salva um novo contato vinculado a uma empresa.
     *
     * @param request DTO contendo os dados do contato.
     * @return DTO representando o contato salvo.
     */
    ContatoEmpresaResponse salvar(ContatoEmpresaRequest request);

    /**
     * Atualiza os dados de um contato existente.
     *
     * @param id      ID do contato.
     * @param request DTO contendo as novas informações.
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
     * Lista todos os contatos ativos.
     *
     * @return Lista de contatos com o campo "ativo" igual a true.
     */
    List<ContatoEmpresaResponse> listarAtivos();

    /**
     * Lista todos os contatos inativos.
     *
     * @return Lista de contatos com o campo "ativo" igual a false.
     */
    List<ContatoEmpresaResponse> listarInativos();

    /**
     * Lista todos os contatos vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de contatos pertencentes à empresa informada.
     */
    List<ContatoEmpresaResponse> listarPorEmpresa(Long empresaId);

    /**
     * Lista os contatos ativos vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de contatos ativos da empresa informada.
     */
    List<ContatoEmpresaResponse> listarPorEmpresaAtivos(Long empresaId);

    /**
     * Busca contatos cujo nome contenha um determinado termo.
     *
     * @param nome Termo parcial do nome do contato.
     * @return Lista de contatos correspondentes ao termo informado.
     */
    List<ContatoEmpresaResponse> buscarPorNome(String nome);

    /**
     * Busca um contato pelo e-mail exato.
     *
     * @param email E-mail do contato.
     * @return Optional contendo o contato, se encontrado.
     */
    Optional<ContatoEmpresaResponse> buscarPorEmail(String email);

    /**
     * Busca contatos de uma empresa cujo e-mail contenha o termo informado.
     *
     * @param empresaId ID da empresa.
     * @param email Termo parcial do e-mail.
     * @return Lista de contatos correspondentes ao termo informado.
     */
    List<ContatoEmpresaResponse> buscarPorEmailParcial(Long empresaId, String email);

    /**
     * Remove um contato do sistema pelo seu ID.
     *
     * @param id ID do contato a ser removido.
     */
    void deletar(Long id);
}
