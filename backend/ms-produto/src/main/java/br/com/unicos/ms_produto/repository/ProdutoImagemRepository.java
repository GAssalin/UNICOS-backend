package br.com.unicos.ms_produto.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_produto.model.ProdutoImagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ProdutoImagem}.
 *
 * <p>
 * Centraliza consultas relacionadas às imagens de produtos no catálogo,
 * garantindo isolamento por {@code empresaId}.
 * </p>
 *
 * <p>
 * Este repositório é utilizado em fluxos como:
 * <ul>
 *     <li>Gerenciamento de galeria de imagens do produto</li>
 *     <li>Definição e consulta de imagem principal</li>
 *     <li>Ordenação de exibição das imagens</li>
 * </ul>
 * </p>
 */
@Repository
public interface ProdutoImagemRepository extends BaseTenantRepository<ProdutoImagem, Long> {

    /**
     * Recupera uma imagem específica dentro do tenant.
     *
     * @param id        Identificador da imagem.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a imagem, se encontrada.
     */
    Optional<ProdutoImagem> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Lista as imagens de um produto dentro do tenant, com paginação.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de imagens do produto.
     */
    Page<ProdutoImagem> findByProdutoIdAndEmpresaId(Long produtoId, Long empresaId, Pageable pageable);

    /**
     * Recupera a imagem principal de um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo a imagem principal, se existente.
     */
    Optional<ProdutoImagem> findByProdutoIdAndPrincipalTrueAndEmpresaId(Long produtoId, Long empresaId);

    /**
     * Remove todas as imagens de um produto dentro do tenant.
     *
     * @param produtoId Identificador do produto.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);

}
