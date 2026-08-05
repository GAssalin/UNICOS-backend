package br.com.unicos.ms_estoque.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;
import br.com.unicos.ms_estoque.model.VinculoEstoqueFilial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link VinculoEstoqueFilial}.
 * <p>
 * Centraliza consultas relacionadas ao vínculo Estoque x Filial,
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Vincular um estoque a uma filial</li>
 *     <li>Listar estoques por filial</li>
 *     <li>Listar filiais atendidas por um estoque</li>
 *     <li>Consultar vínculos vigentes por data</li>
 * </ul>
 * </p>
 */
@Repository
public interface VinculoEstoqueFilialRepository extends BaseTenantRepository<VinculoEstoqueFilial, Long> {

    /**
     * Lista vínculos por estoque dentro do tenant com paginação.
     *
     * @param estoqueId Identificador do estoque.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de vínculos do estoque.
     */
    Page<VinculoEstoqueFilial> findByEstoqueIdAndEmpresaId(
            Long estoqueId,
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
    Page<VinculoEstoqueFilial> findByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista vínculos por filial filtrando por status dentro do tenant.
     *
     * @param filialId                      Identificador da filial.
     * @param statusVinculoEstoqueFilial Status do vínculo.
     * @param tenantId                      Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable                      Paginação e ordenação.
     * @return Página de vínculos filtrados por status.
     */
    Page<VinculoEstoqueFilial> findByFilialIdAndStatusVinculoEstoqueFilialAndEmpresaId(
            Long filialId,
            StatusVinculoEstoqueFilial statusVinculoEstoqueFilial,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista vínculos por estoque filtrando por tipo de atuação dentro do tenant.
     *
     * @param estoqueId Identificador do estoque.
     * @param tipoAtuacao    Tipo de atuação.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @param pageable       Paginação e ordenação.
     * @return Página de vínculos filtrados por tipo de atuação.
     */
    Page<VinculoEstoqueFilial> findByEstoqueIdAndTipoAtuacaoAndEmpresaId(
            Long estoqueId,
            TipoAtuacaoEstoque tipoAtuacao,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera um vínculo específico Estoque x Filial dentro do tenant.
     *
     * @param estoqueId Identificador do estoque.
     * @param filialId       Identificador da filial.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return Optional com o vínculo.
     */
    Optional<VinculoEstoqueFilial> findByEstoqueIdAndFilialIdAndEmpresaId(
            Long estoqueId,
            Long filialId,
            Long tenantId
    );

    /**
     * Verifica se já existe vínculo Estoque x Filial dentro do tenant.
     *
     * @param estoqueId Identificador do estoque.
     * @param filialId       Identificador da filial.
     * @param tenantId       Identificador do tenant (empresaId do BaseTenantEntity).
     * @return {@code true} se existir; {@code false} caso contrário.
     */
    boolean existsByEstoqueIdAndFilialIdAndEmpresaId(
            Long estoqueId,
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
    Page<VinculoEstoqueFilial> findByStatusVinculoEstoqueFilialAndVigenciaInicioLessThanEqualAndVigenciaFimGreaterThanEqualAndEmpresaId(
            StatusVinculoEstoqueFilial status,
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
    Page<VinculoEstoqueFilial> findByStatusVinculoEstoqueFilialAndVigenciaInicioLessThanEqualAndVigenciaFimIsNullAndEmpresaId(
            StatusVinculoEstoqueFilial status,
            LocalDate dataReferencia,
            Long tenantId,
            Pageable pageable
    );
}
