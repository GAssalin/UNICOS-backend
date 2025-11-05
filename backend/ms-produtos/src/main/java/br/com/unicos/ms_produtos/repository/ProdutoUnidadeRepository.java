package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.ProdutoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade ProdutoUnidade.
 * <p>
 * Permite gerenciar as relações entre produtos e suas unidades de medida,
 * além de fornecer métodos de consulta específicos para operações de negócio.
 */
@Repository
public interface ProdutoUnidadeRepository extends JpaRepository<ProdutoUnidade, Long> {

    /**
     * Lista todas as unidades vinculadas a um determinado produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de ProdutoUnidade associadas ao produto informado.
     */
    List<ProdutoUnidade> findByProdutoId(Long produtoId);

    /**
     * Lista todos os vínculos associados a uma determinada unidade de medida.
     *
     * @param unidadeMedidaId ID da unidade de medida.
     * @return Lista de ProdutoUnidade associadas à unidade informada.
     */
    List<ProdutoUnidade> findByUnidadeMedidaId(Long unidadeMedidaId);

    /**
     * Verifica se já existe um vínculo entre um produto e uma unidade de medida específicos.
     *
     * @param produtoId       ID do produto.
     * @param unidadeMedidaId ID da unidade de medida.
     * @return Optional contendo o vínculo, se encontrado.
     */
    Optional<ProdutoUnidade> findByProdutoIdAndUnidadeMedidaId(Long produtoId, Long unidadeMedidaId);
}