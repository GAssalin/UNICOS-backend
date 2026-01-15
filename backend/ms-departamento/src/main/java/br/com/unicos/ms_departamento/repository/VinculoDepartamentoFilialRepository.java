package br.com.unicos.ms_departamento.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_departamento.enums.StatusVinculoDepartamentoFilial;
import br.com.unicos.ms_departamento.enums.TipoAtuacaoDepartamento;
import br.com.unicos.ms_departamento.model.VinculoDepartamentoFilial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link VinculoDepartamentoFilial}.
 * <p>
 * Centraliza consultas relacionadas ao vínculo Departamento x Filial,
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Vincular um departamento a uma filial</li>
 *     <li>Listar departamentos por filial</li>
 *     <li>Listar filiais atendidas por um departamento</li>
 *     <li>Consultar vínculos vigentes por data</li>
 * </ul>
 * </p>
 */
@Repository
public interface VinculoDepartamentoFilialRepository extends BaseTenantRepository<VinculoDepartamentoFilial, Long> {

    /**
     * Lista vínculos por departamento dentro do tenant com paginação.
     *
     * @param departamentoId Identificador do departamento.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de vínculos do departamento.
     */
    Page<VinculoDepartamentoFilial> findByDepartamentoIdAndEmpresaId(
            Long departamentoId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista vínculos por filial dentro do tenant com paginação.
     *
     * @param filialId  Identificador da filial.
     * @param tenantId  Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable  Paginação e ordenação.
     * @return Página de vínculos da filial.
     */
    Page<VinculoDepartamentoFilial> findByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista vínculos por filial filtrando por status dentro do tenant.
     *
     * @param filialId                      Identificador da filial.
     * @param statusVinculoDepartamentoFilial Status do vínculo.
     * @param tenantId                      Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable                      Paginação e ordenação.
     * @return Página de vínculos filtrados por status.
     */
    Page<VinculoDepartamentoFilial> findByFilialIdAndStatusVinculoDepartamentoFilialAndEmpresaId(
            Long filialId,
            StatusVinculoDepartamentoFilial statusVinculoDepartamentoFilial,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista vínculos por departamento filtrando por tipo de atuação dentro do tenant.
     *
     * @param departamentoId Identificador do departamento.
     * @param tipoAtuacao    Tipo de atuação.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de vínculos filtrados por tipo de atuação.
     */
    Page<VinculoDepartamentoFilial> findByDepartamentoIdAndTipoAtuacaoAndEmpresaId(
            Long departamentoId,
            TipoAtuacaoDepartamento tipoAtuacao,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera um vínculo específico Departamento x Filial dentro do tenant.
     *
     * @param departamentoId Identificador do departamento.
     * @param filialId       Identificador da filial.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o vínculo.
     */
    Optional<VinculoDepartamentoFilial> findByDepartamentoIdAndFilialIdAndEmpresaId(
            Long departamentoId,
            Long filialId,
            Long tenantId
    );

    /**
     * Verifica se já existe vínculo Departamento x Filial dentro do tenant.
     *
     * @param departamentoId Identificador do departamento.
     * @param filialId       Identificador da filial.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return {@code true} se existir; {@code false} caso contrário.
     */
    boolean existsByDepartamentoIdAndFilialIdAndEmpresaId(
            Long departamentoId,
            Long filialId,
            Long tenantId
    );

    /**
     * Lista vínculos vigentes na data de referência dentro do tenant.
     * <p>
     * Considera vigência como:
     * vigenciaInicio <= dataReferencia e (vigenciaFim is null ou vigenciaFim >= dataReferencia).
     *
     * @param status         Status do vínculo.
     * @param dataReferencia Data para validação de vigência.
     * @param dataReferenciaFim Data para comparação com vigência fim (mesma data).
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de vínculos vigentes.
     */
    Page<VinculoDepartamentoFilial> findByStatusVinculoDepartamentoFilialAndVigenciaInicioLessThanEqualAndVigenciaFimGreaterThanEqualAndEmpresaId(
            StatusVinculoDepartamentoFilial status,
            LocalDate dataReferencia,
            LocalDate dataReferenciaFim,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista vínculos vigentes na data de referência dentro do tenant,
     * considerando vigenciaFim nula como vínculo vigente.
     *
     * @param status         Status do vínculo.
     * @param dataReferencia Data para validação de vigência.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de vínculos vigentes.
     */
    Page<VinculoDepartamentoFilial> findByStatusVinculoDepartamentoFilialAndVigenciaInicioLessThanEqualAndVigenciaFimIsNullAndEmpresaId(
            StatusVinculoDepartamentoFilial status,
            LocalDate dataReferencia,
            Long tenantId,
            Pageable pageable
    );
}
