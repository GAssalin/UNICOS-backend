package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link ImagemProduto}.
 *
 * <p>
 * Todas as consultas são restritas ao contexto da empresa (tenant),
 * garantindo que imagens de produtos não sejam compartilhadas
 * entre empresas diferentes.
 * </p>
 */
@Repository
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

    /**
     * Retorna todas as imagens associadas a um produto específico
     * dentro do contexto de uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param produtoId ID do produto.
     * @return Lista de imagens associadas ao produto.
     */
    List<ImagemProduto> findByEmpresaIdAndProdutoId(
            Long empresaId,
            Long produtoId
    );

    /**
     * Retorna todas as imagens ativas de um produto
     * pertencente a uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param produtoId ID do produto.
     * @return Lista de imagens ativas.
     */
    List<ImagemProduto> findByEmpresaIdAndProdutoIdAndAtivoTrue(
            Long empresaId,
            Long produtoId
    );

    /**
     * Busca a imagem principal de um produto dentro
     * do contexto de uma empresa.
     *
     * <p>
     * Caso existam múltiplas imagens marcadas como principais,
     * o retorno será a primeira encontrada.
     * </p>
     *
     * @param empresaId ID da empresa (tenant).
     * @param produtoId ID do produto.
     * @return {@link Optional} contendo a imagem principal, se existir.
     */
    Optional<ImagemProduto> findFirstByEmpresaIdAndProdutoIdAndPrincipalTrue(
            Long empresaId,
            Long produtoId
    );

    /**
     * Retorna as imagens de um produto ordenadas conforme
     * a ordem de exibição definida.
     *
     * @param empresaId ID da empresa (tenant).
     * @param produtoId ID do produto.
     * @return Lista de imagens ordenadas por ordem de exibição.
     */
    List<ImagemProduto> findByEmpresaIdAndProdutoIdOrderByOrdemExibicaoAsc(
            Long empresaId,
            Long produtoId
    );
}
