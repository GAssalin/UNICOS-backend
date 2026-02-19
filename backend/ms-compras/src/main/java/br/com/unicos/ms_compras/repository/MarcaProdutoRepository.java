package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.MarcaProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link MarcaProduto}.
 *
 * <p>
 * Centraliza consultas relacionadas às marcas do catálogo,
 * garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de marcas</li>
 *     <li>Filtros de produtos por marca</li>
 *     <li>Padronização de dados comerciais</li>
 * </ul>
 * </p>
 */
@Repository
public interface MarcaProdutoRepository extends BaseTenantRepository<MarcaProduto, Long> {

    /**
     * Recupera uma marca específica dentro do tenant.
     *
     * @param id        Identificador da marca.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a marca, se encontrada.
     */
    Optional<MarcaProduto> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Recupera uma marca pelo nome dentro do tenant.
     *
     * @param nome      Nome da marca.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a marca, se encontrada.
     */
    Optional<MarcaProduto> findByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Verifica se já existe uma marca com o mesmo nome dentro do tenant.
     *
     * @param nome      Nome da marca.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Lista marcas por status (ativo/inativo) dentro do tenant, com paginação.
     *
     * @param ativo     Status da marca.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de marcas.
     */
    Page<MarcaProduto> findByAtivoAndEmpresaId(Boolean ativo, Long empresaId, Pageable pageable);

}
