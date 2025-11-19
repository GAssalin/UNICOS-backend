package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_auth.dto.usuario.UsuarioResponse;

import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade Usuario.
 */
public interface UsuarioService {

    /**
     * Cria um novo usuário autenticável no sistema.
     *
     * @param request Dados necessários para criação.
     * @return UsuarioResponse representando o usuário criado.
     */
    UsuarioResponse salvar(UsuarioRequest request);

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id      ID do usuário a ser atualizado.
     * @param request Dados atualizados.
     * @return UsuarioResponse com as alterações aplicadas.
     */
    UsuarioResponse atualizar(Long id, UsuarioRequest request);

    /**
     * Busca um usuário pelo ID informado.
     *
     * @param id ID do usuário.
     * @return UsuarioResponse correspondente.
     * @throws jakarta.persistence.EntityNotFoundException caso não exista.
     */
    UsuarioResponse buscarPorId(Long id);

    /**
     * Busca um usuário pelo login.
     *
     * @param login Login do usuário.
     * @return UsuarioResponse correspondente.
     * @throws jakarta.persistence.EntityNotFoundException caso não exista.
     */
    UsuarioResponse buscarPorLogin(String login);

    /**
     * Lista todos os usuários cadastrados no sistema.
     *
     * @return Lista de UsuarioResponse.
     */
    List<UsuarioResponse> listarTodos();

    /**
     * Lista todos os usuários ativos.
     *
     * @return Lista de usuários ativos.
     */
    List<UsuarioResponse> listarAtivos();

    /**
     * Lista todos os usuários inativos.
     *
     * @return Lista de usuários inativos.
     */
    List<UsuarioResponse> listarInativos();

    /**
     * Desativa um usuário do sistema.
     *
     * @param id ID do usuário.
     */
    void desativar(Long id);

    /**
     * Remove um usuário permanentemente do sistema.
     *
     * @param id ID do usuário.
     */
    void deletar(Long id);
}
