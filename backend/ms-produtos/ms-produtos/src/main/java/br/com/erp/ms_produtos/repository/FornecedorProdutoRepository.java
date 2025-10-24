package br.com.erp.ms_produtos.repository;

import br.com.erp.ms_produtos.model.FornecedorProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorProdutoRepository extends JpaRepository<FornecedorProduto, Long> {

    List<FornecedorProduto> findByProdutoId(Long produtoId);

    Optional<FornecedorProduto> findByFornecedorIdAndProdutoId(Long fornecedorId, Long produtoId);
}