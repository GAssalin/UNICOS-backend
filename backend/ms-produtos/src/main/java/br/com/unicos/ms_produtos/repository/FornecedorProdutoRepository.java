package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.model.FornecedorProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade
 * {@link FornecedorProduto}.
 *
 * <p>
 * Todas as operações são restritas ao contexto de uma empresa (tenant),
 * garantindo isolamento total entre fornecedores e produtos
 * de empresas diferentes.
 * </p>
 */
@Repository
public interface FornecedorProdutoRepository extends JpaRepository<FornecedorProduto, Long> {

    /**
     * Busca todos os vínculos entre fornecedores e um produto específico
     * dentro de uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param produtoId ID do produto.
     * @return Lista de vínculos fornecedor–produto.
     */
    List<FornecedorProduto> findByEmpresaIdAndProdutoId(
            Long empresaId,
            Long produtoId
    );

    /**
     * Busca um fornecedor pelo ID dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID do fornecedor.
     * @return {@link Optional} contendo o Fornecedor, se existir.
     */
    Optional<FornecedorProduto> findByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Busca todos os vínculos de um fornecedor dentro de uma empresa.
     *
     * @param empresaId    ID da empresa (tenant).
     * @param fornecedorId ID do fornecedor.
     * @return Lista de vínculos do fornecedor.
     */
    List<FornecedorProduto> findByEmpresaIdAndFornecedorId(
            Long empresaId,
            Long fornecedorId
    );

    /**
     * Busca um vínculo específico entre fornecedor e produto,
     * no contexto de uma empresa.
     *
     * @param empresaId    ID da empresa (tenant).
     * @param fornecedorId ID do fornecedor.
     * @param produtoId    ID do produto.
     * @return {@link Optional} contendo o vínculo, se existir.
     */
    Optional<FornecedorProduto> findByEmpresaIdAndFornecedorIdAndProdutoId(
            Long empresaId,
            Long fornecedorId,
            Long produtoId
    );

    /**
     * Verifica se já existe um vínculo entre fornecedor e produto
     * dentro de uma empresa.
     *
     * @param empresaId    ID da empresa (tenant).
     * @param fornecedorId ID do fornecedor.
     * @param produtoId    ID do produto.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndFornecedorIdAndProdutoId(
            Long empresaId,
            Long fornecedorId,
            Long produtoId
    );

    /**
     * Remove todos os vínculos de um fornecedor específico
     * dentro do contexto de uma empresa.
     *
     * @param empresaId    ID da empresa (tenant).
     * @param fornecedorId ID do fornecedor.
     */
    void deleteByEmpresaIdAndFornecedorId(
            Long empresaId,
            Long fornecedorId
    );
}
