package br.com.erp.ms_produtos.repository;

import br.com.erp.ms_produtos.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, Long> {

    List<HistoricoPreco> findByProdutoIdOrderByDataAlteracaoDesc(Long produtoId);
}