package br.com.erp.ms_empresa.service;

import br.com.erp.ms_empresa.dto.DepartamentoEmpresaRequest;
import br.com.erp.ms_empresa.dto.DepartamentoEmpresaResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade DepartamentoEmpresa.
 *
 * Define os métodos de criação, atualização, listagem e exclusão
 * dos departamentos vinculados a uma empresa.
 */
public interface DepartamentoEmpresaService {

    /**
     * Salva um novo departamento vinculado a uma empresa.
     *
     * @param request DTO contendo os dados do departamento.
     * @return DTO representando o departamento salvo.
     */
    DepartamentoEmpresaResponse salvar(DepartamentoEmpresaRequest request);

    /**
     * Atualiza um departamento existente.
     *
     * @param id ID do departamento a ser atualizado.
     * @param request DTO contendo os novos dados.
     * @return DTO representando o departamento atualizado.
     */
    DepartamentoEmpresaResponse atualizar(Long id, DepartamentoEmpresaRequest request);

    /**
     * Busca um departamento pelo ID.
     *
     * @param id ID do departamento.
     * @return Optional contendo o DTO do departamento, se encontrado.
     */
    Optional<DepartamentoEmpresaResponse> buscarPorId(Long id);

    /**
     * Lista todos os departamentos cadastrados.
     *
     * @return Lista de departamentos.
     */
    List<DepartamentoEmpresaResponse> listarTodos();

    /**
     * Lista todos os departamentos de uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos vinculados à empresa informada.
     */
    List<DepartamentoEmpresaResponse> listarPorEmpresa(Long empresaId);

    /**
     * Busca departamentos pelo nome (parcial ou total).
     *
     * @param nome Termo de busca.
     * @return Lista de departamentos correspondentes.
     */
    List<DepartamentoEmpresaResponse> buscarPorNome(String nome);

    /**
     * Lista todos os departamentos ativos.
     *
     * @return Lista de departamentos ativos.
     */
    List<DepartamentoEmpresaResponse> listarAtivos();

    /**
     * Lista todos os departamentos inativos.
     *
     * @return Lista de departamentos inativos.
     */
    List<DepartamentoEmpresaResponse> listarInativos();

    /**
     * Remove um departamento pelo ID.
     *
     * @param id ID do departamento a ser removido.
     */
    void deletar(Long id);
}