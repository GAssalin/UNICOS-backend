package br.com.unicos.ms_estoque.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_estoque.enums.StatusEstoque;
import br.com.unicos.ms_estoque.model.Estoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Estoque}.
 * <p>
 * Centraliza consultas relacionadas a estoques (unidades organizacionais),
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de estoques</li>
 *     <li>Pesquisa por código/nome</li>
 *     <li>Listagem de estoques por status</li>
 *     <li>Estrutura organizacional (estoque pai)</li>
 * </ul>
 * </p>
 */
@Repository
public interface EstoqueRepository extends BaseTenantRepository<Estoque, Long> {

    /**
     * Lista estoques dentro do tenant com paginação.
     *
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable Paginação e ordenação.
     * @return Página de estoques.
     */
    Page<Estoque> findAllByEmpresaId(
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista estoques filtrando por status dentro do tenant.
     *
     * @param statusEstoque Status do estoque.
     * @param tenantId           Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable           Paginação e ordenação.
     * @return Página de estoques filtrados por status.
     */
    Page<Estoque> findByStatusEstoqueAndEmpresaId(
            StatusEstoque statusEstoque,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera um estoque pelo código dentro do tenant.
     *
     * @param codigo   Código interno do estoque.
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o estoque.
     */
    Optional<Estoque> findByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Verifica se já existe um estoque com o mesmo código dentro do tenant.
     *
     * @param codigo   Código interno do estoque.
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Lista estoques filhos de um estoque pai dentro do tenant.
     *
     * @param estoquePaiId Identificador do estoque pai.
     * @param tenantId          Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable          Paginação e ordenação.
     * @return Página de estoques filhos.
     */
    Page<Estoque> findByEstoquePaiIdAndEmpresaId(
            Long estoquePaiId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista estoques raiz (sem pai) dentro do tenant.
     *
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable Paginação e ordenação.
     * @return Página de estoques raiz.
     */
    Page<Estoque> findByEstoquePaiIdIsNullAndEmpresaId(
            Long tenantId,
            Pageable pageable
    );
}
