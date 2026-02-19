package br.com.unicos.ms_vendas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_vendas.model.ProdutoTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ProdutoTipo}.
 *
 * <p>
 * Centraliza consultas relacionadas aos tipos de produto configuráveis
 * (ex.: Mercadoria, Matéria-Prima, Embalagem, Serviço, Assinatura),
 * garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Cadastro e manutenção de tipos configuráveis</li>
 *     <li>Padronização de classificação de itens do catálogo</li>
 *     <li>Filtros e relatórios por tipo</li>
 * </ul>
 * </p>
 */
@Repository
public interface ProdutoTipoRepository extends BaseTenantRepository<ProdutoTipo, Long> {

    /**
     * Recupera um tipo de produto específico dentro do tenant.
     *
     * @param id        Identificador do tipo.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o tipo, se encontrado.
     */
    Optional<ProdutoTipo> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Recupera um tipo de produto pelo nome dentro do tenant.
     *
     * @param nome      Nome do tipo de produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o tipo, se encontrado.
     */
    Optional<ProdutoTipo> findByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Verifica se já existe um tipo de produto com o mesmo nome dentro do tenant.
     *
     * @param nome      Nome do tipo de produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se já existir; {@code false} caso contrário.
     */
    boolean existsByNomeAndEmpresaId(String nome, Long empresaId);

    /**
     * Lista tipos de produto por status (ativo/inativo) dentro do tenant, com paginação.
     *
     * @param ativo     Status do tipo.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de tipos de produto.
     */
    Page<ProdutoTipo> findByAtivoAndEmpresaId(Boolean ativo, Long empresaId, Pageable pageable);

}
