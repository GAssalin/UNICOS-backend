package br.com.unicos.ms_estoque.repository;

import br.com.unicos.ms_estoque.model.ProdutoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoEstoqueRepository extends JpaRepository<ProdutoEstoque, Long> {

    List<ProdutoEstoque> findByEstoqueLocalId(Long estoqueLocalId);

    /**
     * Busca um produto específico dentro de um local de estoque.
     */
    Optional<ProdutoEstoque> findByProdutoIdAndEstoqueLocalId(Long produtoId, Long estoqueLocalId);

    /**
     * Retorna todos os produtos com saldo abaixo da quantidade mínima.
     */
    @Query("""
            SELECT p FROM ProdutoEstoque p
            WHERE p.quantidade < p.quantidadeMinima
            """)
    List<ProdutoEstoque> findProdutosComEstoqueBaixo();

    /**
     * Retorna todos os produtos com saldo acima da quantidade máxima definida.
     */
    @Query("""
            SELECT p FROM ProdutoEstoque p
            WHERE p.quantidade > p.quantidadeMaxima
            """)
    List<ProdutoEstoque> findProdutosComEstoqueExcedente();
}
