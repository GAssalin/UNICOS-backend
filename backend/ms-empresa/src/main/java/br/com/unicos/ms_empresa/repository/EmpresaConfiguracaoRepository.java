package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaConfiguracao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaConfiguracao}.
 * <p>
 * Centraliza consultas relacionadas às configurações globais de uma empresa (tenant),
 * garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Gerenciamento de parâmetros globais do tenant</li>
 *     <li>Customização de comportamento por empresa</li>
 *     <li>Integrações entre microsserviços</li>
 * </ul>
 * </p>
 */
@Repository
public interface EmpresaConfiguracaoRepository extends BaseTenantRepository<EmpresaConfiguracao, Long> {

    /**
     * Recupera uma configuração específica da empresa pelo nome da chave,
     * respeitando o contexto multi-tenant.
     *
     * @param empresa   Empresa proprietária da configuração.
     * @param chave     Nome da chave de configuração.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a configuração, se encontrada.
     */
    Optional<EmpresaConfiguracao> findByEmpresaAndChaveAndEmpresaId(
            Empresa empresa,
            String chave,
            Long empresaId
    );

    /**
     * Verifica se já existe uma configuração cadastrada para a empresa
     * com a mesma chave, evitando duplicidade.
     *
     * @param empresa   Empresa proprietária da configuração.
     * @param chave     Nome da chave de configuração.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByEmpresaAndChaveAndEmpresaId(
            Empresa empresa,
            String chave,
            Long empresaId
    );

    /**
     * Lista todas as configurações de uma empresa dentro do tenant,
     * com suporte à paginação.
     *
     * @param empresa   Empresa proprietária das configurações.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de configurações da empresa.
     */
    Page<EmpresaConfiguracao> findByEmpresaAndEmpresaId(
            Empresa empresa,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Remove uma configuração específica de uma empresa dentro do tenant.
     *
     * @param empresa   Empresa proprietária da configuração.
     * @param chave     Nome da chave da configuração.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByEmpresaAndChaveAndEmpresaId(
            Empresa empresa,
            String chave,
            Long empresaId
    );
}
