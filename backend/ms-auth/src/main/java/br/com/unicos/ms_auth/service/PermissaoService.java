package br.com.unicos.ms_auth.service;

import br.com.unicos.ms_auth.dto.PermissaoRequest;
import br.com.unicos.ms_auth.dto.PermissaoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas operações de negócio
 * relacionadas à entidade Permissao.
 */
public interface PermissaoService {

    /**
     * Cria uma nova permissão granular.
     *
     * @param request Dados para criação da permissão.
     * @return PermissaoResponse criada.
     */
    PermissaoResponse salvar(PermissaoRequest request);

    /**
     * Atualiza uma permissão existente.
     *
     * @param id      ID da permissão.
     * @param request Dados atualizados.
     * @return PermissaoResponse atualizada.
     */
    PermissaoResponse atualizar(Long id, PermissaoRequest request);

    /**
     * Busca uma permissão pelo ID.
     *
     * @param id ID da permissão.
     * @return Optional contendo a PermissaoResponse, se encontrada.
     */
    Optional<PermissaoResponse> buscarPorId(Long id);

    /**
     * Lista todas as permissões cadastradas.
     *
     * @return Lista de PermissaoResponse.
     */
    List<PermissaoResponse> listarTodas();

    /**
     * Remove uma permissão específica.
     *
     * @param id ID da permissão.
     */
    void deletar(Long id);

    /**
     * Verifica se já existe uma permissão com o nome informado.
     *
     * @param nome Nome da permissão.
     * @return true se existir, false caso contrário.
     */
    boolean existePorNome(String nome);
}
