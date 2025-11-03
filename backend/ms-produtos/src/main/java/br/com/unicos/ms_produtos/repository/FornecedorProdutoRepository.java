package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.FornecedorProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados de FornecedorProduto.
 */
@Repository
public interface FornecedorProdutoRepository extends JpaRepository<FornecedorProduto, Long> {

    /**
     * Busca todos os registros vinculados a um determinado produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de FornecedorProduto.
     */
    List<FornecedorProduto> findByProdutoId(Long produtoId);

    /**
     * Busca todos os registros vinculados a um determinado fornecedor.
     *
     * @param fornecedorId ID do fornecedor.
     * @return Lista de FornecedorProduto.
     */
    List<FornecedorProduto> findByFornecedorId(Long fornecedorId);

    /**
     * Busca um vínculo específico entre fornecedor e produto.
     *
     * @param fornecedorId ID do fornecedor.
     * @param produtoId ID do produto.
     * @return Registro, se existir.
     */
    Optional<FornecedorProduto> findByFornecedorIdAndProdutoId(Long fornecedorId, Long produtoId);

    /**
     * Verifica se já existe um vínculo entre fornecedor e produto.
     *
     * @param fornecedorId ID do fornecedor.
     * @param produtoId ID do produto.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByFornecedorIdAndProdutoId(Long fornecedorId, Long produtoId);

    /**
     * Exclui todos os vínculos de um determinado fornecedor.
     *
     * @param fornecedorId ID do fornecedor.
     */
    void deleteByFornecedorId(Long fornecedorId);
}