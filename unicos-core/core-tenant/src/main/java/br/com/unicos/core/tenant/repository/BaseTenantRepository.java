package br.com.unicos.core.tenant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repositório base para entidades que operam em contexto multi-tenant.
 * <p>
 * Este contrato define operações mínimas obrigatórias para qualquer entidade
 * que pertença a uma empresa (tenant), garantindo isolamento de dados por
 * {@code empresaId}.
 * </p>
 *
 * <p>
 * A adoção deste repositório base permite:
 * <ul>
 *     <li>Padronização das consultas multi-tenant</li>
 *     <li>Redução de duplicação de métodos entre repositórios</li>
 *     <li>Segurança por design, evitando acesso a dados de outros tenants</li>
 *     <li>Facilidade de manutenção e evolução arquitetural</li>
 * </ul>
 * </p>
 *
 * <p><b>Importante:</b>
 * Este repositório <strong>não deve</strong> ser instanciado diretamente.
 * Ele serve apenas como contrato para repositórios concretos que manipulam
 * entidades multi-tenant.
 * </p>
 *
 * @param <T>  Tipo da entidade gerenciada.
 * @param <ID> Tipo do identificador da entidade.
 */
@NoRepositoryBean
public interface BaseTenantRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * Verifica a existência de uma entidade dentro do contexto de uma empresa
     * (tenant), com base no seu identificador.
     *
     * @param id        Identificador da entidade.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se a entidade existir dentro da empresa;
     * {@code false} caso contrário.
     */
    boolean existsByIdAndEmpresaId(ID id, Long empresaId);

    /**
     * Lista todas as entidades pertencentes a uma empresa (tenant).
     *
     * @param empresaId Identificador da empresa (tenant).
     * @return Lista de entidades associadas à empresa informada.
     */
    Page<T> findAllByEmpresaId(Long empresaId, Pageable pageable);
}
