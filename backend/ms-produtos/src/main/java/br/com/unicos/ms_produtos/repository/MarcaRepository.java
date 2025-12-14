package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Marca}.
 *
 * <p>
 * Todas as consultas são restritas ao contexto da empresa (tenant),
 * identificado pelo {@code empresaId}.
 * </p>
 */
@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    /**
     * Busca uma marca pelo nome exato, ignorando maiúsculas e minúsculas,
     * dentro do contexto de uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Nome da marca.
     * @return {@link Optional} contendo a marca, se encontrada.
     */
    Optional<Marca> findByEmpresaIdAndNomeIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Retorna uma lista de marcas cujo nome contenha o termo informado,
     * restringindo a busca à empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Termo de busca.
     * @return Lista de marcas correspondentes.
     */
    List<Marca> findByEmpresaIdAndNomeContainingIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Verifica se já existe uma marca com o nome informado
     * dentro de uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Nome da marca.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndNomeIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Lista todas as marcas de uma empresa ordenadas
     * alfabeticamente pelo nome.
     *
     * @param empresaId ID da empresa (tenant).
     * @return Lista de marcas ordenadas por nome.
     */
    List<Marca> findByEmpresaIdOrderByNomeAsc(
            Long empresaId
    );

    /**
     * Busca um registro de marca pelo ID,
     * restringindo ao contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID da marca.
     * @return Optional contendo o marca, se existir.
     */
    Optional<Marca> findByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Verifica se um registro de marca de preço existe
     * dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID da marca.
     * @return true se existir, false caso contrário.
     */
    boolean existsByEmpresaIdAndId(
            Long empresaId,
            Long id
    );
}
