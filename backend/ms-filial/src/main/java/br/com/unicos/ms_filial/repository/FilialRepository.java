package br.com.unicos.ms_filial.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_filial.enums.StatusFilial;
import br.com.unicos.ms_filial.model.Filial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Filial}.
 * <p>
 * Centraliza consultas relacionadas às unidades (filiais),
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de filiais</li>
 *     <li>Pesquisa por código/CNPJ</li>
 *     <li>Listagem de filiais por empresa e status</li>
 * </ul>
 * </p>
 */
@Repository
public interface FilialRepository extends BaseTenantRepository<Filial, Long> {

    /**
     * Lista filiais de uma empresa dentro do tenant com paginação.
     *
     * @param empresaId Identificador da empresa proprietária.
     * @param tenantId  Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable  Paginação e ordenação.
     * @return Página de filiais.
     */
    Page<Filial> findByEmpresaIdAndEmpresaId(
            Long empresaId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista filiais de uma empresa filtrando por status dentro do tenant.
     *
     * @param empresaId    Identificador da empresa proprietária.
     * @param statusFilial Status da filial.
     * @param tenantId     Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable     Paginação e ordenação.
     * @return Página de filiais filtradas por status.
     */
    Page<Filial> findByEmpresaIdAndStatusFilialAndEmpresaId(
            Long empresaId,
            StatusFilial statusFilial,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera uma filial pelo código dentro do tenant.
     *
     * @param codigo   Código interno da filial.
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com a filial.
     */
    Optional<Filial> findByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Recupera uma filial pelo CNPJ dentro do tenant.
     *
     * @param cnpj     CNPJ da filial sem formatação.
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com a filial.
     */
    Optional<Filial> findByCnpjAndEmpresaId(
            String cnpj,
            Long tenantId
    );

    /**
     * Verifica se já existe uma filial com o mesmo código dentro do tenant.
     *
     * @param codigo   Código interno da filial.
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Verifica se já existe uma filial com o mesmo CNPJ dentro do tenant.
     *
     * @param cnpj     CNPJ da filial sem formatação.
     * @param tenantId Identificador do tenant (empresaId do BaseTenantEntity).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCnpjAndEmpresaId(
            String cnpj,
            Long tenantId
    );
}
