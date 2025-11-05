package br.com.unicos.ms_estoque.repository;

import br.com.unicos.ms_estoque.model.LoteSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoteSerieRepository extends JpaRepository<LoteSerie, Long> {

    Optional<LoteSerie> findByCodigo(String codigo);

    /**
     * Retorna todos os lotes com validade próxima (até N dias).
     */
    @Query("""
            SELECT l FROM LoteSerie l
            WHERE l.dataValidade <= CURRENT_DATE + :dias
            """)
    List<LoteSerie> findLotesProximosDoVencimento(int dias);

    /**
     * Busca lotes já vencidos.
     */
    @Query("""
            SELECT l FROM LoteSerie l
            WHERE l.dataValidade < CURRENT_DATE
            """)
    List<LoteSerie> findLotesVencidos();
}
