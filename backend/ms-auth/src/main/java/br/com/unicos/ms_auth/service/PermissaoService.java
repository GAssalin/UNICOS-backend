package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;

import java.util.List;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade Permissao.
 */
public interface PermissaoService {

    /**
     * Cria uma nova permissão granular no sistema.
     *
     * @param request Dados necessários para criação.
     * @return PermissaoResponse representando a permissão criada.
     */
    PermissaoResponse salvar(PermissaoRequest request);

    /**
     * Atualiza os dados de uma permissão existente.
     *
     * @param id      ID da permissão a ser atualizada.
     * @param request Dados atualizados.
     * @return PermissaoResponse representando a permissão atualizada.
     */
    PermissaoResponse atualizar(Long id, PermissaoRequest request);

    /**
     * Busca uma permissão pelo ID informado.
     *
     * @param id ID da permissão.
     * @return PermissaoResponse caso encontrada.
     * @throws jakarta.persistence.EntityNotFoundException caso não exista.
     */
    PermissaoResponse buscarPorId(Long id);

    /**
     * Lista todas as permissões cadastradas no sistema.
     *
     * @return Lista de PermissaoResponse.
     */
    List<PermissaoResponse> listarTodas();

    /**
     * Remove uma permissão do sistema.
     *
     * @param id ID da permissão a ser removida.
     */
    void deletar(Long id);

    /**
     * Verifica se já existe uma permissão cadastrada com o código informado.
     *
     * @param codigo Código único da permissão.
     * @return true se existir, false caso contrário.
     */
    boolean existePorCodigo(String codigo);
}
