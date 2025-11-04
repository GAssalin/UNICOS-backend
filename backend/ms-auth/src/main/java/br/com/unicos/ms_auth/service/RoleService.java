package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.RoleRequest;
import br.com.unicos.ms_auth.dto.RoleResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade Role.
 */
public interface RoleService {

    /**
     * Cria um novo papel (role) no sistema.
     *
     * @param request Dados para criação do papel.
     * @return RoleResponse criado.
     */
    RoleResponse salvar(RoleRequest request);

    /**
     * Atualiza um papel existente.
     *
     * @param id      ID do papel.
     * @param request Dados atualizados.
     * @return RoleResponse atualizado.
     */
    RoleResponse atualizar(Long id, RoleRequest request);

    /**
     * Busca um papel pelo seu ID.
     *
     * @param id ID do papel.
     * @return Optional contendo o RoleResponse, se encontrado.
     */
    Optional<RoleResponse> buscarPorId(Long id);

    /**
     * Lista todos os papéis cadastrados.
     *
     * @return Lista de RoleResponse.
     */
    List<RoleResponse> listarTodos();

    /**
     * Remove um papel específico.
     *
     * @param id ID do papel.
     */
    void deletar(Long id);

    /**
     * Verifica se já existe um papel com o nome informado.
     *
     * @param nome Nome do papel.
     * @return true se existir, false caso contrário.
     */
    boolean existePorNome(String nome);
}
