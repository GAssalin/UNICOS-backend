package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaParametro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaParametro}.
 * <p>
 * Centraliza consultas relacionadas aos parâmetros flexíveis de customização
 * por empresa (tenant), no formato chave-valor.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Customização de regras de negócio por empresa</li>
 *     <li>Feature toggles</li>
 *     <li>Parâmetros operacionais dinâmicos</li>
 * </ul>
 * </p>
 */
@Repository
public interface EmpresaParametroRepository extends BaseTenantRepository<EmpresaParametro, Long> {

    /**
     * Recupera um parâmetro específico da empresa pelo nome da chave,
     * respeitando o contexto multi-tenant.
     *
     * @param empresa   Empresa proprietária do parâmetro.
     * @param chave     Nome da chave do parâmetro.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o parâmetro, se encontrado.
     */
    Optional<EmpresaParametro> findByEmpresaAndChaveAndEmpresaId(
            Empresa empresa,
            String chave,
            Long empresaId
    );

    /**
     * Verifica se já existe um parâmetro cadastrado para a empresa
     * com a mesma chave, evitando duplicidade.
     *
     * @param empresa   Empresa proprietária do parâmetro.
     * @param chave     Nome da chave do parâmetro.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByEmpresaAndChaveAndEmpresaId(
            Empresa empresa,
            String chave,
            Long empresaId
    );

    /**
     * Lista todos os parâmetros de uma empresa dentro do tenant,
     * com suporte à paginação.
     *
     * @param empresa   Empresa proprietária dos parâmetros.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de parâmetros da empresa.
     */
    Page<EmpresaParametro> findByEmpresaAndEmpresaId(
            Empresa empresa,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Remove um parâmetro específico de uma empresa dentro do tenant.
     *
     * @param empresa   Empresa proprietária do parâmetro.
     * @param chave     Nome da chave do parâmetro.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByEmpresaAndChaveAndEmpresaId(
            Empresa empresa,
            String chave,
            Long empresaId
    );
}
