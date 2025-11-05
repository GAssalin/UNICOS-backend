package br.com.unicos.ms_estoque.repository;

import br.com.unicos.ms_estoque.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByTransacaoId(Long transacaoId);

    /**
     * Busca todas as movimentações relacionadas a um produto específico.
     */
    @Query("""
            SELECT m FROM MovimentacaoEstoque m
            WHERE m.produtoEstoque.id = :produtoEstoqueId
            """)
    List<MovimentacaoEstoque> findByProdutoEstoqueId(Long produtoEstoqueId);

    /**
     * Retorna o total movimentado de um produto no período.
     */
    @Query("""
            SELECT SUM(m.quantidade) FROM MovimentacaoEstoque m
            WHERE m.produtoEstoque.id = :produtoEstoqueId
              AND m.transacao.data BETWEEN :inicio AND :fim
            """)
    Double calcularTotalMovimentado(Long produtoEstoqueId, LocalDateTime inicio, LocalDateTime fim);
}
