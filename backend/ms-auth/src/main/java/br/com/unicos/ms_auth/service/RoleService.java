package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.role.RoleRequest;
import br.com.unicos.ms_auth.dto.role.RoleResponse;

import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade Role.
 */
public interface RoleService {

    /**
     * Cria um novo papel (role) no sistema.
     *
     * @param request Dados necessários para criação.
     * @return RoleResponse representando o papel criado.
     */
    RoleResponse salvar(RoleRequest request);

    /**
     * Atualiza os dados de um papel existente.
     *
     * @param id      ID do papel a ser atualizado.
     * @param request Dados atualizados.
     * @return RoleResponse representando o papel atualizado.
     */
    RoleResponse atualizar(Long id, RoleRequest request);

    /**
     * Busca um papel pelo seu ID.
     *
     * @param id ID do papel.
     * @return RoleResponse encontrado.
     * @throws jakarta.persistence.EntityNotFoundException caso não exista.
     */
    RoleResponse buscarPorId(Long id);

    /**
     * Lista todos os papéis cadastrados no sistema.
     *
     * @return Lista de RoleResponse.
     */
    List<RoleResponse> listarTodos();

    /**
     * Remove um papel do sistema.
     *
     * @param id ID do papel a ser removido.
     */
    void deletar(Long id);

    /**
     * Verifica se já existe um papel com o código informado.
     *
     * @param codigo Código do papel.
     * @return true se existir, false caso contrário.
     */
    boolean existePorNome(String codigo);
}
