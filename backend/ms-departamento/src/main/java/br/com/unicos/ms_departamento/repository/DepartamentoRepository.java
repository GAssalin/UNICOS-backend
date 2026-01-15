package br.com.unicos.ms_departamento.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_departamento.enums.StatusDepartamento;
import br.com.unicos.ms_departamento.model.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Departamento}.
 * <p>
 * Centraliza consultas relacionadas a departamentos (unidades organizacionais),
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de departamentos</li>
 *     <li>Pesquisa por código/nome</li>
 *     <li>Listagem de departamentos por status</li>
 *     <li>Estrutura organizacional (departamento pai)</li>
 * </ul>
 * </p>
 */
@Repository
public interface DepartamentoRepository extends BaseTenantRepository<Departamento, Long> {

    /**
     * Lista departamentos dentro do tenant com paginação.
     *
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable Paginação e ordenação.
     * @return Página de departamentos.
     */
    Page<Departamento> findAllByEmpresaId(
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista departamentos filtrando por status dentro do tenant.
     *
     * @param statusDepartamento Status do departamento.
     * @param tenantId           Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable           Paginação e ordenação.
     * @return Página de departamentos filtrados por status.
     */
    Page<Departamento> findByStatusDepartamentoAndEmpresaId(
            StatusDepartamento statusDepartamento,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera um departamento pelo código dentro do tenant.
     *
     * @param codigo   Código interno do departamento.
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o departamento.
     */
    Optional<Departamento> findByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Verifica se já existe um departamento com o mesmo código dentro do tenant.
     *
     * @param codigo   Código interno do departamento.
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Lista departamentos filhos de um departamento pai dentro do tenant.
     *
     * @param departamentoPaiId Identificador do departamento pai.
     * @param tenantId          Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable          Paginação e ordenação.
     * @return Página de departamentos filhos.
     */
    Page<Departamento> findByDepartamentoPaiIdAndEmpresaId(
            Long departamentoPaiId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista departamentos raiz (sem pai) dentro do tenant.
     *
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable Paginação e ordenação.
     * @return Página de departamentos raiz.
     */
    Page<Departamento> findByDepartamentoPaiIdIsNullAndEmpresaId(
            Long tenantId,
            Pageable pageable
    );
}
