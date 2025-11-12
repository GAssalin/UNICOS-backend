package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.ProdutoAtributoValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link ProdutoAtributoValor}.
 * <p>
 * Permite consultas pelos valores de atributos de um produto.
 */
@Repository
public interface ProdutoAtributoValorRepository extends JpaRepository<ProdutoAtributoValor, Long> {

    /**
     * Retorna todos os valores de atributos de um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de valores de atributos.
     */
    List<ProdutoAtributoValor> findByProdutoId(Long produtoId);

    /**
     * Busca o valor de um atributo específico de um produto.
     *
     * @param produtoId  ID do produto.
     * @param atributoId ID do atributo personalizado.
     * @return Valor correspondente, se existir.
     */
    Optional<ProdutoAtributoValor> findByProdutoIdAndAtributoPersonalizadoId(Long produtoId, Long atributoId);
}
