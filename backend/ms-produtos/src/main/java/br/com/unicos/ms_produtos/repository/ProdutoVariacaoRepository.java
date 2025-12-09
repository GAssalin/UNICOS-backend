package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.ProdutoVariacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link ProdutoVariacao}.
 * <p>
 * Permite consultas por produto, SKU e atributos específicos.
 */
@Repository
public interface ProdutoVariacaoRepository extends JpaRepository<ProdutoVariacao, Long> {

    /**
     * Retorna todas as variações de um produto.
     *
     * @param produtoId ID do produto principal.
     * @return Lista de variações associadas.
     */
    List<ProdutoVariacao> findByProdutoId(Long produtoId);

    /**
     * Busca uma variação pelo SKU.
     *
     * @param sku Código SKU único.
     * @return Variação correspondente, se existir.
     */
    Optional<ProdutoVariacao> findBySku(String sku);

    /**
     * Retorna todas as variações ativas.
     *
     * @return Lista de variações ativas.
     */
    List<ProdutoVariacao> findByAtivoTrue();

    /**
     * Busca variações com base em cor e tamanho.
     *
     * @param cor     Cor da variação.
     * @param tamanho Tamanho da variação.
     * @return Lista de variações correspondentes.
     */
    List<ProdutoVariacao> findByCorIgnoreCaseAndTamanhoIgnoreCase(String cor, String tamanho);

    /**
     * Busca variações por produto e status.
     *
     * @param produtoId ID do produto principal.
     * @param ativo     Status da variação.
     * @return Lista de variações conforme o filtro.
     */
    List<ProdutoVariacao> findByProdutoIdAndAtivo(Long produtoId, Boolean ativo);
}
