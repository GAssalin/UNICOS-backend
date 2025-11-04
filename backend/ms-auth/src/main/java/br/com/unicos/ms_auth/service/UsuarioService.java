package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.UsuarioRequest;
import br.com.unicos.ms_auth.dto.UsuarioResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade Usuario.
 */
public interface UsuarioService {

    /**
     * Cria um novo usuário autenticável no sistema.
     *
     * @param request Dados para criação do usuário.
     * @return UsuarioResponse criado.
     */
    UsuarioResponse salvar(UsuarioRequest request);

    /**
     * Atualiza um usuário existente.
     *
     * @param id      ID do usuário.
     * @param request Dados atualizados.
     * @return UsuarioResponse atualizado.
     */
    UsuarioResponse atualizar(Long id, UsuarioRequest request);

    /**
     * Busca um usuário por ID.
     *
     * @param id ID do usuário.
     * @return Optional contendo o UsuarioResponse, se encontrado.
     */
    Optional<UsuarioResponse> buscarPorId(Long id);

    /**
     * Lista todos os usuários cadastrados.
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
     * Desativa um usuário existente.
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
