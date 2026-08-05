package br.com.unicos.ms_estoque.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import br.com.unicos.ms_estoque.model.ResponsavelEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ResponsavelEstoque}.
 * <p>
 * Centraliza consultas relacionadas aos responsáveis/gestores de estoques,
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Definição e manutenção de responsáveis do estoque</li>
 *     <li>Consulta do gestor principal vigente</li>
 *     <li>Listagem por estoque, responsável e papel</li>
 * </ul>
 * </p>
 */
@Repository
public interface ResponsavelEstoqueRepository extends BaseTenantRepository<ResponsavelEstoque, Long> {

    /**
     * Lista responsáveis de um estoque dentro do tenant com paginação.
     *
     * @param estoqueId Identificador do estoque.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de responsáveis do estoque.
     */
    Page<ResponsavelEstoque> findByEstoqueIdAndEmpresaId(
            Long estoqueId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista responsáveis de um estoque filtrando por status dentro do tenant.
     *
     * @param estoqueId              Identificador do estoque.
     * @param statusResponsavelEstoque Status do vínculo do responsável.
     * @param tenantId                    Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable                    Paginação e ordenação.
     * @return Página de responsáveis do estoque filtrados por status.
     */
    Page<ResponsavelEstoque> findByEstoqueIdAndStatusResponsavelEstoqueAndEmpresaId(
            Long estoqueId,
            StatusResponsavelEstoque statusResponsavelEstoque,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista responsáveis por papel dentro do tenant.
     *
     * @param estoqueId Identificador do estoque.
     * @param papel          Papel do responsável.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de responsáveis filtrados por papel.
     */
    Page<ResponsavelEstoque> findByEstoqueIdAndPapelAndEmpresaId(
            Long estoqueId,
            PapelResponsavelEstoque papel,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera o responsável principal de um estoque dentro do tenant.
     *
     * @param estoqueId Identificador do estoque.
     * @param principal      Flag de responsável principal.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o responsável principal.
     */
    Optional<ResponsavelEstoque> findByEstoqueIdAndPrincipalAndEmpresaId(
            Long estoqueId,
            Boolean principal,
            Long tenantId
    );

    /**
     * Verifica se já existe um responsável principal ativo para o estoque dentro do tenant.
     *
     * @param estoqueId Identificador do estoque.
     * @param principal      Flag de responsável principal.
     * @param status         Status do vínculo do responsável.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return {@code true} se existir; {@code false} caso contrário.
     */
    boolean existsByEstoqueIdAndPrincipalAndStatusResponsavelEstoqueAndEmpresaId(
            Long estoqueId,
            Boolean principal,
            StatusResponsavelEstoque status,
            Long tenantId
    );

    /**
     * Lista responsáveis por identificador de responsável (ms-pessoas/ms-rh) dentro do tenant.
     *
     * @param responsavelId Identificador do responsável (externo).
     * @param tenantId      Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable      Paginação e ordenação.
     * @return Página de vínculos do responsável.
     */
    Page<ResponsavelEstoque> findByResponsavelIdAndEmpresaId(
            Long responsavelId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera o responsável principal vigente para um estoque em uma data de referência.
     * <p>
     * Considera vigência como:
     * vigenciaInicio <= dataReferencia e (vigenciaFim is null ou vigenciaFim >= dataReferencia).
     *
     * @param estoqueId Identificador do estoque.
     * @param principal      Flag de responsável principal.
     * @param status         Status do vínculo.
     * @param dataReferencia Data para validação da vigência.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o responsável principal vigente.
     */
    Optional<ResponsavelEstoque> findFirstByEstoqueIdAndPrincipalAndStatusResponsavelEstoqueAndVigenciaInicioLessThanEqualAndVigenciaFimGreaterThanEqualAndEmpresaId(
            Long estoqueId,
            Boolean principal,
            StatusResponsavelEstoque status,
            LocalDate dataReferencia,
            LocalDate dataReferenciaFim,
            Long tenantId
    );

    /**
     * Recupera o responsável principal vigente para um estoque em uma data de referência,
     * considerando vigenciaFim nula como vínculo vigente.
     *
     * @param estoqueId Identificador do estoque.
     * @param principal      Flag de responsável principal.
     * @param status         Status do vínculo.
     * @param dataReferencia Data para validação da vigência.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o responsável principal vigente.
     */
    Optional<ResponsavelEstoque> findFirstByEstoqueIdAndPrincipalAndStatusResponsavelEstoqueAndVigenciaInicioLessThanEqualAndVigenciaFimIsNullAndEmpresaId(
            Long estoqueId,
            Boolean principal,
            StatusResponsavelEstoque status,
            LocalDate dataReferencia,
            Long tenantId
    );
}
