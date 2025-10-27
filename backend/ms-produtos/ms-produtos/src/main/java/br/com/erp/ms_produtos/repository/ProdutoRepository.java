package br.com.erp.ms_produtos.repository;

import br.com.erp.ms_produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findBySku(String sku);

    List<Produto> findByAtivoTrue();

    List<Produto> findByCategoriaId(Long categoriaId);

    List<Produto> findByMarcaId(Long marcaId);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByAtivoFalse();

    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);
}