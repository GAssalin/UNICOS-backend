package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link ImagemProduto}.
 * <p>
 * Fornece métodos para buscar imagens de produtos e suas ordens de exibição.
 */
@Repository
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

    /**
     * Retorna todas as imagens de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de imagens associadas.
     */
    List<ImagemProduto> findByProdutoId(Long produtoId);

    /**
     * Retorna as imagens ativas de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de imagens ativas.
     */
    List<ImagemProduto> findByProdutoIdAndAtivoTrue(Long produtoId);

    /**
     * Busca a imagem principal de um produto.
     *
     * @param produtoId ID do produto.
     * @return Imagem principal, se houver.
     */
    Optional<ImagemProduto> findFirstByProdutoIdAndPrincipalTrue(Long produtoId);

    /**
     * Retorna imagens ordenadas conforme a posição definida.
     *
     * @param produtoId ID do produto.
     * @return Lista de imagens ordenadas por ordem de exibição.
     */
    List<ImagemProduto> findByProdutoIdOrderByOrdemExibicaoAsc(Long produtoId);
}
