package br.com.unicos.ms_empresa.service;

import br.com.unicos.ms_empresa.dto.DepartamentoRequest;
import br.com.unicos.ms_empresa.dto.DepartamentoResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link br.com.unicos.ms_empresa.model.Departamento}.
 * <p>
 * Define os métodos de criação, atualização, listagem e exclusão
 * dos departamentos vinculados a uma empresa.
 */
public interface DepartamentoService {

    /**
     * Cria e salva um novo departamento vinculado a uma empresa.
     *
     * @param request DTO contendo os dados do departamento.
     * @return DTO representando o departamento salvo.
     */
    DepartamentoResponse salvar(DepartamentoRequest request);

    /**
     * Atualiza os dados de um departamento existente.
     *
     * @param id      ID do departamento a ser atualizado.
     * @param request DTO contendo os novos dados.
     * @return DTO representando o departamento atualizado.
     */
    DepartamentoResponse atualizar(Long id, DepartamentoRequest request);

    /**
     * Busca um departamento pelo seu ID.
     *
     * @param id ID do departamento.
     * @return Optional contendo o DTO do departamento, se encontrado.
     */
    Optional<DepartamentoResponse> buscarPorId(Long id);

    /**
     * Lista todos os departamentos cadastrados.
     *
     * @return Lista de departamentos.
     */
    List<DepartamentoResponse> listarTodos();

    /**
     * Lista todos os departamentos ativos.
     *
     * @return Lista de departamentos com o campo "ativo" igual a true.
     */
    List<DepartamentoResponse> listarAtivos();

    /**
     * Lista todos os departamentos inativos.
     *
     * @return Lista de departamentos com o campo "ativo" igual a false.
     */
    List<DepartamentoResponse> listarInativos();

    /**
     * Lista todos os departamentos vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos pertencentes à empresa informada.
     */
    List<DepartamentoResponse> listarPorEmpresa(Long empresaId);

    /**
     * Lista todos os departamentos ativos vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos ativos pertencentes à empresa informada.
     */
    List<DepartamentoResponse> listarPorEmpresaAtivos(Long empresaId);

    /**
     * Busca departamentos pelo nome (parcial ou total).
     *
     * @param nome Termo de busca.
     * @return Lista de departamentos correspondentes ao termo informado.
     */
    List<DepartamentoResponse> buscarPorNome(String nome);

    /**
     * Busca departamentos cujo nome contenha o termo informado
     * e que estejam ativos.
     *
     * @param nome Termo de busca.
     * @return Lista de departamentos ativos correspondentes.
     */
    List<DepartamentoResponse> buscarPorNomeEAtivo(String nome);

    /**
     * Lista todos os departamentos ordenados alfabeticamente pelo nome.
     *
     * @return Lista de departamentos ordenada por nome.
     */
    List<DepartamentoResponse> listarOrdenadosPorNome();

    /**
     * Lista todos os departamentos de uma empresa,
     * ordenados alfabeticamente pelo nome.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos da empresa ordenada por nome.
     */
    List<DepartamentoResponse> listarPorEmpresaOrdenados(Long empresaId);

    /**
     * Remove um departamento do sistema pelo ID.
     *
     * @param id ID do departamento a ser removido.
     */
    void deletar(Long id);
}
