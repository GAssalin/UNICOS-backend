package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.ProdutoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ProdutoUnidade}.
 *
 * <p>
 * Todas as operações são realizadas dentro do contexto de uma empresa (tenant),
 * identificado pelo campo {@code empresaId}, garantindo isolamento total
 * entre os dados de empresas diferentes.
 * </p>
 *
 * <p>
 * Este repositório gerencia os vínculos entre produtos e unidades de medida,
 * permitindo consultas específicas para regras de negócio e validações.
 * </p>
 */
@Repository
public interface ProdutoUnidadeRepository extends JpaRepository<ProdutoUnidade, Long> {

    /**
     * Busca um vínculo produto–unidade pelo ID,
     * restringindo ao contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID do vínculo.
     * @return {@link Optional} contendo o vínculo, se existir.
     */
    Optional<ProdutoUnidade> findByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Verifica se um vínculo produto–unidade existe
     * dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID do vínculo.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Retorna todos os vínculos produto–unidade
     * pertencentes a uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @return Lista de vínculos.
     */
    List<ProdutoUnidade> findByEmpresaId(
            Long empresaId
    );

    /**
     * Lista todas as unidades vinculadas a um produto específico
     * dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param produtoId ID do produto.
     * @return Lista de vínculos produto–unidade.
     */
    List<ProdutoUnidade> findByEmpresaIdAndProdutoId(
            Long empresaId,
            Long produtoId
    );

    /**
     * Lista todos os vínculos associados a uma unidade de medida
     * específica dentro da empresa.
     *
     * @param empresaId       ID da empresa (tenant).
     * @param unidadeMedidaId ID da unidade de medida.
     * @return Lista de vínculos produto–unidade.
     */
    List<ProdutoUnidade> findByEmpresaIdAndUnidadeMedidaId(
            Long empresaId,
            Long unidadeMedidaId
    );

    /**
     * Verifica se já existe um vínculo entre um produto e uma unidade de medida
     * dentro do contexto da empresa.
     *
     * @param empresaId       ID da empresa (tenant).
     * @param produtoId       ID do produto.
     * @param unidadeMedidaId ID da unidade de medida.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndProdutoIdAndUnidadeMedidaId(
            Long empresaId,
            Long produtoId,
            Long unidadeMedidaId
    );
}
