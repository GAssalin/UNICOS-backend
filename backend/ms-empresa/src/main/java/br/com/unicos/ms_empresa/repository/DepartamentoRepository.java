package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Departamento}.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    /**
     * Lista todos os departamentos vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos pertencentes à empresa informada.
     */
    List<Departamento> findByEmpresaId(Long empresaId);

    /**
     * Busca departamentos cujo nome contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome Termo de busca.
     * @return Lista de departamentos correspondentes ao termo informado.
     */
    List<Departamento> findByNomeContainingIgnoreCase(String nome);

    /**
     * Lista todos os departamentos ativos.
     *
     * @return Lista de departamentos com o campo "ativo" igual a true.
     */
    List<Departamento> findByAtivoTrue();

    /**
     * Lista todos os departamentos inativos.
     *
     * @return Lista de departamentos com o campo "ativo" igual a false.
     */
    List<Departamento> findByAtivoFalse();

    /**
     * Busca um departamento pelo nome exato e pela empresa à qual pertence.
     *
     * @param nome      Nome do departamento.
     * @param empresaId ID da empresa.
     * @return Optional contendo o departamento, se encontrado.
     */
    Optional<Departamento> findByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Verifica se já existe um departamento com o mesmo nome dentro da mesma empresa.
     *
     * @param nome      Nome do departamento.
     * @param empresaId ID da empresa.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Lista todos os departamentos de uma empresa e que estejam ativos.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos ativos da empresa informada.
     */
    List<Departamento> findByEmpresaIdAndAtivoTrue(Long empresaId);

    /**
     * Lista todos os departamentos de uma empresa e que estejam inativos.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos inativos da empresa informada.
     */
    List<Departamento> findByEmpresaIdAndAtivoFalse(Long empresaId);

    /**
     * Lista todos os departamentos ordenados alfabeticamente pelo nome.
     *
     * @return Lista de departamentos ordenada por nome.
     */
    List<Departamento> findAllByOrderByNomeAsc();

    /**
     * Lista todos os departamentos de uma empresa ordenados alfabeticamente pelo nome.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos da empresa, ordenada por nome.
     */
    List<Departamento> findByEmpresaIdOrderByNomeAsc(Long empresaId);

    /**
     * Lista departamentos cujo nome contenha o termo informado e que estejam ativos.
     *
     * @param nome Termo parcial de busca.
     * @return Lista de departamentos ativos correspondentes ao termo.
     */
    List<Departamento> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    /**
     * Lista departamentos cujo nome contenha o termo informado e que pertençam à empresa.
     *
     * @param nome      Termo parcial de busca.
     * @param empresaId ID da empresa.
     * @return Lista de departamentos correspondentes ao termo e empresa.
     */
    List<Departamento> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, Long empresaId);
}
