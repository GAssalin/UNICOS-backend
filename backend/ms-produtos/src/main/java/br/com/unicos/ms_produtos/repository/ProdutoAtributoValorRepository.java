package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.ProdutoAtributoValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link ProdutoAtributoValor}.
 *
 * <p>
 * Todas as consultas são restritas ao contexto da empresa (tenant),
 * garantindo isolamento entre valores de atributos de produtos
 * pertencentes a empresas diferentes.
 * </p>
 */
@Repository
public interface ProdutoAtributoValorRepository extends JpaRepository<ProdutoAtributoValor, Long> {

    /**
     * Retorna todos os valores de atributos de um produto específico
     * dentro do contexto de uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param produtoId ID do produto.
     * @return Lista de valores de atributos.
     */
    List<ProdutoAtributoValor> findByEmpresaIdAndProdutoId(
            Long empresaId,
            Long produtoId
    );

    /**
     * Busca o valor de um atributo específico de um produto,
     * restringindo a busca à empresa.
     *
     * @param empresaId  ID da empresa (tenant).
     * @param produtoId  ID do produto.
     * @param atributoId ID do atributo personalizado.
     * @return {@link Optional} contendo o valor correspondente, se existir.
     */
    Optional<ProdutoAtributoValor> findByEmpresaIdAndProdutoIdAndAtributoPersonalizadoId(
            Long empresaId,
            Long produtoId,
            Long atributoId
    );
}
