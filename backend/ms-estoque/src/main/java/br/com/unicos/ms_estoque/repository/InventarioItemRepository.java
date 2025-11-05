package br.com.unicos.ms_estoque.repository;

import br.com.unicos.ms_estoque.model.InventarioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioItemRepository extends JpaRepository<InventarioItem, Long> {

    List<InventarioItem> findByInventarioId(Long inventarioId);

    /**
     * Busca itens divergentes (diferença entre quantidade contada e registrada).
     */
    @Query("""
            SELECT i FROM InventarioItem i
            WHERE i.inventario.id = :inventarioId
              AND i.quantidadeContada <> i.quantidadeRegistrada
            """)
    List<InventarioItem> findItensDivergentes(Long inventarioId);

    /**
     * Calcula a diferença total de estoque em um inventário.
     */
    @Query("""
            SELECT SUM(i.quantidadeContada - i.quantidadeRegistrada)
            FROM InventarioItem i
            WHERE i.inventario.id = :inventarioId
            """)
    Double calcularDiferencaTotal(Long inventarioId);
}
