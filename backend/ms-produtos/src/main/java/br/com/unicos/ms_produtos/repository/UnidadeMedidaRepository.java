package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.UnidadeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link UnidadeMedida}.
 *
 * <p>
 * Todas as consultas são realizadas dentro do contexto de uma empresa (tenant),
 * identificado pelo {@code empresaId}, garantindo isolamento total entre
 * unidades de medida de empresas diferentes.
 * </p>
 *
 * <p>
 * Este repositório assegura que:
 * <ul>
 *     <li>unidades de medida não sejam compartilhadas entre empresas</li>
 *     <li>não exista duplicidade de sigla dentro da mesma empresa</li>
 * </ul>
 * </p>
 */
@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Long> {

    /**
     * Busca uma unidade de medida pelo seu identificador,
     * restringindo a consulta ao contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID da unidade de medida.
     * @return {@link Optional} contendo a unidade, se encontrada.
     */
    Optional<UnidadeMedida> findByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Busca uma unidade de medida pelo nome exato,
     * ignorando diferenças entre maiúsculas e minúsculas,
     * dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Nome da unidade de medida.
     * @return {@link Optional} contendo a unidade, se existir.
     */
    Optional<UnidadeMedida> findByEmpresaIdAndNomeIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Busca uma unidade de medida pela sigla exata,
     * ignorando diferenças entre maiúsculas e minúsculas,
     * dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param sigla     Sigla da unidade de medida.
     * @return {@link Optional} contendo a unidade, se existir.
     */
    Optional<UnidadeMedida> findByEmpresaIdAndSiglaIgnoreCase(
            Long empresaId,
            String sigla
    );

    /**
     * Retorna todas as unidades de medida cujo nome contenha
     * o termo informado, restringindo a busca à empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Parte do nome da unidade de medida.
     * @return Lista de unidades de medida correspondentes ao filtro.
     */
    List<UnidadeMedida> findByEmpresaIdAndNomeContainingIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Verifica se já existe uma unidade de medida com a sigla informada
     * dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param sigla     Sigla da unidade de medida.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndSiglaIgnoreCase(
            Long empresaId,
            String sigla
    );

    /**
     * Retorna todas as unidades de medida pertencentes a uma empresa,
     * ordenadas alfabeticamente pelo nome.
     *
     * <p>
     * Método amplamente utilizado para listagens simples,
     * seleção em formulários e combos no frontend.
     * </p>
     *
     * @param empresaId ID da empresa (tenant).
     * @return Lista ordenada de unidades de medida.
     */
    List<UnidadeMedida> findByEmpresaIdOrderByNomeAsc(
            Long empresaId
    );
}
