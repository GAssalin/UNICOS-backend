package br.com.unicos.ms_produto.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_produto.model.UnidadeMedida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link UnidadeMedida}.
 *
 * <p>
 * Centraliza consultas relacionadas às unidades de medida utilizadas
 * no catálogo de produtos, garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de unidades de medida</li>
 *     <li>Validação de unidades utilizadas em produtos</li>
 *     <li>Padronização de dados operacionais</li>
 * </ul>
 * </p>
 */
@Repository
public interface UnidadeMedidaRepository extends BaseTenantRepository<UnidadeMedida, Long> {

    /**
     * Recupera uma unidade de medida específica dentro do tenant.
     *
     * @param id        Identificador da unidade.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a unidade, se encontrada.
     */
    Optional<UnidadeMedida> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Recupera uma unidade de medida pelo código dentro do tenant.
     *
     * @param codigo    Código da unidade (ex.: UN, KG, L).
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a unidade, se encontrada.
     */
    Optional<UnidadeMedida> findByCodigoAndEmpresaId(String codigo, Long empresaId);

    /**
     * Verifica se já existe uma unidade de medida com o mesmo código
     * dentro do tenant, evitando duplicidade.
     *
     * @param codigo    Código da unidade.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByCodigoAndEmpresaId(String codigo, Long empresaId);

    /**
     * Lista unidades por status (ativo/inativo) dentro do tenant.
     *
     * @param ativo     Status da unidade.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de unidades de medida.
     */
    Page<UnidadeMedida> findByAtivoAndEmpresaId(Boolean ativo, Long empresaId, Pageable pageable);

}
