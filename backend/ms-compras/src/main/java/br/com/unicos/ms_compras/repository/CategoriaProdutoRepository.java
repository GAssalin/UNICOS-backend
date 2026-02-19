package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.CategoriaProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link CategoriaProduto}.
 *
 * <p>
 * Centraliza consultas relacionadas à organização do catálogo por categorias,
 * garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de categorias</li>
 *     <li>Hierarquia de categorias (categoria pai)</li>
 *     <li>Filtros de produtos por categoria</li>
 * </ul>
 * </p>
 */
@Repository
public interface CategoriaProdutoRepository extends BaseTenantRepository<CategoriaProduto, Long> {

    /**
     * Recupera uma categoria específica dentro do tenant.
     *
     * @param id        Identificador da categoria.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a categoria, se encontrada.
     */
    Optional<CategoriaProduto> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Recupera uma categoria pelo nome dentro do tenant.
     *
     * @param nome      Nome da categoria.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a categoria, se encontrada.
     */
    Optional<CategoriaProduto> findByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Verifica se já existe uma categoria com o mesmo nome dentro do tenant.
     *
     * @param nome      Nome da categoria.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Lista categorias por categoria pai dentro do tenant.
     *
     * @param categoriaPaiId Identificador da categoria pai.
     * @param empresaId      Identificador da empresa (tenant).
     * @param pageable       Parâmetros de paginação e ordenação.
     * @return Página de categorias filhas.
     */
    Page<CategoriaProduto> findByCategoriaPaiIdAndEmpresaId(
            Long categoriaPaiId,
            Long empresaId,
            Pageable pageable
    );

}
