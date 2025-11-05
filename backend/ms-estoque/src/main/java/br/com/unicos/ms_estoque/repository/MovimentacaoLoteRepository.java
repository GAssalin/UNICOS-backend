package br.com.unicos.ms_estoque.repository;

import br.com.unicos.ms_estoque.model.MovimentacaoLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoLoteRepository extends JpaRepository<MovimentacaoLote, Long> {

    List<MovimentacaoLote> findByMovimentacaoId(Long movimentacaoId);

    /**
     * Retorna todas as movimentações associadas a um determinado lote.
     */
    List<MovimentacaoLote> findByLoteId(Long loteId);

    /**
     * Soma a quantidade total movimentada de um lote.
     */
    @Query("""
            SELECT SUM(m.quantidade) FROM MovimentacaoLote m
            WHERE m.lote.id = :loteId
            """)
    Double calcularQuantidadeMovimentadaPorLote(Long loteId);
}
