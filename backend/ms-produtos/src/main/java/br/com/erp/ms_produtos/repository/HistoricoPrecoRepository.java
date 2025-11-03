package br.com.erp.ms_produtos.repository;

import br.com.erp.ms_produtos.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados de histórico de preços de produtos.
 */
@Repository
public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, Long> {

    /**
     * Busca todos os registros de histórico de preço de um produto específico,
     * ordenados do mais recente para o mais antigo.
     *
     * @param produtoId ID do produto.
     * @return Lista de históricos de preço ordenados por data de alteração decrescente.
     */
    List<HistoricoPreco> findByProdutoIdOrderByDataAlteracaoDesc(Long produtoId);

    /**
     * Retorna os últimos 10 registros de alterações de preço de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista com os 10 registros mais recentes.
     */
    List<HistoricoPreco> findTop10ByProdutoIdOrderByDataAlteracaoDesc(Long produtoId);

    /**
     * Retorna todos os históricos de preço, ordenados por data de alteração (mais recentes primeiro).
     *
     * @return Lista completa de históricos ordenados.
     */
    List<HistoricoPreco> findAllByOrderByDataAlteracaoDesc();
}