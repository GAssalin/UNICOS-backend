package br.com.unicos.ms_vendas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_vendas.model.ProdutoAtributo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ProdutoAtributo}.
 *
 * <p>
 * Centraliza consultas relacionadas ao dicionário de atributos do catálogo
 * (ex.: Cor, Tamanho, Voltagem), garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de atributos do catálogo</li>
 *     <li>Validação de atributos antes de associar valores aos produtos</li>
 *     <li>Filtros e padronização de dados de atributos</li>
 * </ul>
 * </p>
 */
@Repository
public interface ProdutoAtributoRepository extends BaseTenantRepository<ProdutoAtributo, Long> {

    /**
     * Recupera um atributo específico dentro do tenant.
     *
     * @param id        Identificador do atributo.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o atributo, se encontrado.
     */
    Optional<ProdutoAtributo> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Recupera um atributo pelo nome dentro do tenant.
     *
     * @param nome      Nome do atributo.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o atributo, se encontrado.
     */
    Optional<ProdutoAtributo> findByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Verifica se já existe um atributo com o mesmo nome dentro do tenant.
     *
     * @param nome      Nome do atributo.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Lista atributos por status (ativo/inativo) dentro do tenant, com paginação.
     *
     * @param ativo     Status do atributo.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de atributos.
     */
    Page<ProdutoAtributo> findByAtivoAndEmpresaId(Boolean ativo, Long empresaId, Pageable pageable);

}
