package br.com.unicos.ms_pessoas.service.interfaces;

import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos
 * contatos vinculados às pessoas dentro do UniCoS.
 *
 * <p>
 * Contatos incluem e-mail, telefone, celular e outros meios de comunicação.
 * Este serviço centraliza validações, definição de contato principal e
 * integridade entre Pessoa e Contato.
 * </p>
 */
public interface ContatoService {

    /**
     * Cria um novo contato vinculado a uma pessoa.
     *
     * @param request dados do contato a ser criado.
     * @return dados do contato criado.
     */
    ContatoResponse criar(ContatoRequest request);

    /**
     * Atualiza um contato existente.
     *
     * @param id      identificador do contato a ser atualizado.
     * @param request novos dados para o contato.
     * @return DTO contendo os dados atualizados.
     */
    ContatoResponse atualizar(Long id, ContatoRequest request);

    /**
     * Remove um contato pelo ID.
     *
     * @param id identificador do contato.
     */
    void excluir(Long id);

    /**
     * Obtém um contato pelo ID.
     *
     * @param id identificador do contato.
     * @return DTO detalhado do contato, se encontrado.
     */
    Optional<ContatoResponse> buscarPorId(Long id);

    /**
     * Lista todos os contatos cadastrados.
     *
     * @return lista simplificada de contatos.
     */
    List<ContatoListDTO> listarTodos();

    /**
     * Lista todos os contatos pertencentes a uma pessoa específica.
     *
     * @param pessoaId ID da pessoa.
     * @return lista de contatos da pessoa.
     */
    List<ContatoListDTO> listarPorPessoa(Long pessoaId);

    /**
     * Obtém o contato principal de uma pessoa.
     *
     * @param pessoaId ID da pessoa.
     * @return contato principal, se existir.
     */
    Optional<ContatoResponse> buscarPrincipal(Long pessoaId);
}
