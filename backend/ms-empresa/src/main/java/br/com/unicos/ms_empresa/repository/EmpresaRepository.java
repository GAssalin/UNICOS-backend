package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;
import br.com.unicos.ms_empresa.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Empresa}.
 * <p>
 * Centraliza consultas relacionadas às empresas (tenants) do UniCoS,
 * garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de empresas</li>
 *     <li>Validação de tenant ativo</li>
 *     <li>Integração entre microsserviços</li>
 *     <li>Inicialização de contexto multi-tenant</li>
 * </ul>
 * </p>
 */
@Repository
public interface EmpresaRepository extends BaseTenantRepository<Empresa, Long> {

    /**
     * Recupera uma empresa pelo CNPJ dentro do contexto da empresa (tenant).
     *
     * @param cnpj      CNPJ da empresa (sem formatação).
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a empresa, se encontrada.
     */
    Optional<Empresa> findByCnpjAndEmpresaId(String cnpj, Long empresaId);

    /**
     * Verifica se já existe uma empresa cadastrada com o mesmo CNPJ
     * dentro do tenant.
     *
     * @param cnpj      CNPJ da empresa.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCnpjAndEmpresaId(String cnpj, Long empresaId);

    /**
     * Lista empresas filtrando pelo status operacional,
     * respeitando o contexto multi-tenant.
     *
     * @param statusEmpresa Status da empresa.
     * @param empresaId     Identificador da empresa (tenant).
     * @param pageable      Parâmetros de paginação.
     * @return Página de empresas filtradas por status.
     */
    Page<Empresa> findByStatusEmpresaAndEmpresaId(
            StatusEmpresa statusEmpresa,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista empresas filtrando pelo tipo (MATRIZ ou FILIAL),
     * respeitando o contexto multi-tenant.
     *
     * @param tipoEmpresa Tipo da empresa.
     * @param empresaId   Identificador da empresa (tenant).
     * @param pageable    Parâmetros de paginação.
     * @return Página de empresas filtradas por tipo.
     */
    Page<Empresa> findByTipoEmpresaAndEmpresaId(
            TipoEmpresa tipoEmpresa,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista todas as empresas pertencentes ao tenant,
     * com suporte à paginação.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de empresas.
     */
    Page<Empresa> findByEmpresaId(Long empresaId, Pageable pageable);
}
