package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados de histórico
 * de preços dos produtos.
 *
 * <p>
 * Todas as consultas são restritas ao contexto da empresa (tenant),
 * identificado pelo {@code empresaId}, garantindo isolamento total
 * entre históricos de empresas diferentes.
 * </p>
 */
@Repository
public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, Long> {

    /**
     * Busca um registro de histórico de preço pelo ID,
     * restringindo ao contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID do histórico.
     * @return Optional contendo o histórico, se existir.
     */
    Optional<HistoricoPreco> findByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Verifica se um registro de histórico de preço existe
     * dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID do histórico.
     * @return true se existir, false caso contrário.
     */
    boolean existsByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Busca todos os registros de histórico de preço de um produto específico,
     * pertencente a uma empresa, ordenados do mais recente para o mais antigo.
     */
    List<HistoricoPreco> findByEmpresaIdAndProdutoIdOrderByDataAlteracaoDesc(
            Long empresaId,
            Long produtoId
    );

    /**
     * Retorna os últimos 10 registros de alterações de preço de um produto
     * dentro do contexto de uma empresa.
     */
    List<HistoricoPreco> findTop10ByEmpresaIdAndProdutoIdOrderByDataAlteracaoDesc(
            Long empresaId,
            Long produtoId
    );

    /**
     * Retorna todos os históricos de preço pertencentes a uma empresa,
     * ordenados por data de alteração (mais recentes primeiro).
     */
    List<HistoricoPreco> findByEmpresaIdOrderByDataAlteracaoDesc(
            Long empresaId
    );
}
