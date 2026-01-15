package br.com.unicos.ms_departamento.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_departamento.enums.PapelResponsavelDepartamento;
import br.com.unicos.ms_departamento.enums.StatusResponsavelDepartamento;
import br.com.unicos.ms_departamento.model.ResponsavelDepartamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ResponsavelDepartamento}.
 * <p>
 * Centraliza consultas relacionadas aos responsáveis/gestores de departamentos,
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Definição e manutenção de responsáveis do departamento</li>
 *     <li>Consulta do gestor principal vigente</li>
 *     <li>Listagem por departamento, responsável e papel</li>
 * </ul>
 * </p>
 */
@Repository
public interface ResponsavelDepartamentoRepository extends BaseTenantRepository<ResponsavelDepartamento, Long> {

    /**
     * Lista responsáveis de um departamento dentro do tenant com paginação.
     *
     * @param departamentoId Identificador do departamento.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de responsáveis do departamento.
     */
    Page<ResponsavelDepartamento> findByDepartamentoIdAndEmpresaId(
            Long departamentoId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista responsáveis de um departamento filtrando por status dentro do tenant.
     *
     * @param departamentoId              Identificador do departamento.
     * @param statusResponsavelDepartamento Status do vínculo do responsável.
     * @param tenantId                    Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable                    Paginação e ordenação.
     * @return Página de responsáveis do departamento filtrados por status.
     */
    Page<ResponsavelDepartamento> findByDepartamentoIdAndStatusResponsavelDepartamentoAndEmpresaId(
            Long departamentoId,
            StatusResponsavelDepartamento statusResponsavelDepartamento,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista responsáveis por papel dentro do tenant.
     *
     * @param departamentoId Identificador do departamento.
     * @param papel          Papel do responsável.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de responsáveis filtrados por papel.
     */
    Page<ResponsavelDepartamento> findByDepartamentoIdAndPapelAndEmpresaId(
            Long departamentoId,
            PapelResponsavelDepartamento papel,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera o responsável principal de um departamento dentro do tenant.
     *
     * @param departamentoId Identificador do departamento.
     * @param principal      Flag de responsável principal.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o responsável principal.
     */
    Optional<ResponsavelDepartamento> findByDepartamentoIdAndPrincipalAndEmpresaId(
            Long departamentoId,
            Boolean principal,
            Long tenantId
    );

    /**
     * Verifica se já existe um responsável principal ativo para o departamento dentro do tenant.
     *
     * @param departamentoId Identificador do departamento.
     * @param principal      Flag de responsável principal.
     * @param status         Status do vínculo do responsável.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return {@code true} se existir; {@code false} caso contrário.
     */
    boolean existsByDepartamentoIdAndPrincipalAndStatusResponsavelDepartamentoAndEmpresaId(
            Long departamentoId,
            Boolean principal,
            StatusResponsavelDepartamento status,
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
    Page<ResponsavelDepartamento> findByResponsavelIdAndEmpresaId(
            Long responsavelId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera o responsável principal vigente para um departamento em uma data de referência.
     * <p>
     * Considera vigência como:
     * vigenciaInicio <= dataReferencia e (vigenciaFim is null ou vigenciaFim >= dataReferencia).
     *
     * @param departamentoId Identificador do departamento.
     * @param principal      Flag de responsável principal.
     * @param status         Status do vínculo.
     * @param dataReferencia Data para validação da vigência.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o responsável principal vigente.
     */
    Optional<ResponsavelDepartamento> findFirstByDepartamentoIdAndPrincipalAndStatusResponsavelDepartamentoAndVigenciaInicioLessThanEqualAndVigenciaFimGreaterThanEqualAndEmpresaId(
            Long departamentoId,
            Boolean principal,
            StatusResponsavelDepartamento status,
            LocalDate dataReferencia,
            LocalDate dataReferenciaFim,
            Long tenantId
    );

    /**
     * Recupera o responsável principal vigente para um departamento em uma data de referência,
     * considerando vigenciaFim nula como vínculo vigente.
     *
     * @param departamentoId Identificador do departamento.
     * @param principal      Flag de responsável principal.
     * @param status         Status do vínculo.
     * @param dataReferencia Data para validação da vigência.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o responsável principal vigente.
     */
    Optional<ResponsavelDepartamento> findFirstByDepartamentoIdAndPrincipalAndStatusResponsavelDepartamentoAndVigenciaInicioLessThanEqualAndVigenciaFimIsNullAndEmpresaId(
            Long departamentoId,
            Boolean principal,
            StatusResponsavelDepartamento status,
            LocalDate dataReferencia,
            Long tenantId
    );
}
