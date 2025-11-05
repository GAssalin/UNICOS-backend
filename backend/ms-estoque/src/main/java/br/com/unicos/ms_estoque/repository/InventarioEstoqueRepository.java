package br.com.unicos.ms_estoque.repository;

import br.com.unicos.ms_estoque.enums.StatusInventario;
import br.com.unicos.ms_estoque.model.InventarioEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventarioEstoqueRepository extends JpaRepository<InventarioEstoque, Long> {

    List<InventarioEstoque> findByStatus(StatusInventario status);

    /**
     * Busca inventários realizados dentro de um intervalo de datas.
     */
    List<InventarioEstoque> findByDataInicioBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Verifica se há inventário em aberto para um local de estoque específico.
     */
    boolean existsByEstoqueLocalIdAndStatus(Long estoqueLocalId, StatusInventario status);
}
