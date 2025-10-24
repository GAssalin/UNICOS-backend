package br.com.erp.ms_produtos.repository;

import br.com.erp.ms_produtos.model.ProdutoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoUnidadeRepository extends JpaRepository<ProdutoUnidade, Long> {

    List<ProdutoUnidade> findByProdutoId(Long produtoId);
}