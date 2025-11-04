package br.com.unicos.ms_empresa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade DepartamentoEmpresa.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface DepartamentoEmpresaRepository extends JpaRepository<DepartamentoEmpresa, Long> {

    /**
     * Lista todos os departamentos vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de departamentos pertencentes à empresa informada.
     */
    List<DepartamentoEmpresa> findByEmpresaId(Long empresaId);

    /**
     * Busca departamentos cujo nome contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome Termo de busca.
     * @return Lista de departamentos correspondentes ao termo informado.
     */
    List<DepartamentoEmpresa> findByNomeContainingIgnoreCase(String nome);

    /**
     * Lista todos os departamentos ativos.
     *
     * @return Lista de departamentos com o campo "ativo" igual a true.
     */
    List<DepartamentoEmpresa> findByAtivoTrue();

    /**
     * Lista todos os departamentos inativos.
     *
     * @return Lista de departamentos com o campo "ativo" igual a false.
     */
    List<DepartamentoEmpresa> findByAtivoFalse();

    /**
     * Busca um departamento pelo nome exato e pela empresa.
     *
     * @param nome Nome do departamento.
     * @param empresaId ID da empresa à qual pertence.
     * @return Optional contendo o departamento, se encontrado.
     */
    Optional<DepartamentoEmpresa> findByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Verifica se já existe um departamento com o mesmo nome dentro da mesma empresa.
     *
     * @param nome Nome do departamento.
     * @param empresaId ID da empresa.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByNomeAndEmpresaId(String nome, Long empresaId);
}