package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link Categoria}.
 *
 * <p>
 * Todas as consultas são realizadas dentro do contexto
 * de uma empresa (tenant), identificado pelo {@code empresaId}.
 * </p>
 *
 * <p>
 * Garante que categorias:
 * <ul>
 *     <li>possam ter nomes repetidos entre empresas</li>
 *     <li>não tenham duplicidade de nomes dentro da mesma empresa</li>
 * </ul>
 * </p>
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca uma categoria pelo nome exato, ignorando diferenças
     * de maiúsculas e minúsculas, no contexto de uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Nome da categoria.
     * @return {@link Optional} contendo a categoria, se existir.
     */
    Optional<Categoria> findByEmpresaIdAndNomeIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Busca uma categoria pelo ID dentro do contexto da empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param id        ID da categoria.
     * @return {@link Optional} contendo a categoria, se existir.
     */
    Optional<Categoria> findByEmpresaIdAndId(
            Long empresaId,
            Long id
    );

    /**
     * Retorna todas as categorias pertencentes a uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @return Lista de categorias da empresa.
     */
    List<Categoria> findByEmpresaId(
            Long empresaId
    );

    /**
     * Busca categorias cujo nome contenha o termo informado,
     * restringindo o resultado à empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Parte do nome da categoria.
     * @return Lista de categorias encontradas.
     */
    List<Categoria> findByEmpresaIdAndNomeContainingIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Verifica se já existe uma categoria com o nome informado
     * dentro de uma empresa.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Nome da categoria.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndNomeIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Retorna todas as categorias de uma empresa,
     * ordenadas alfabeticamente pelo nome.
     *
     * @param empresaId ID da empresa (tenant).
     * @return Lista ordenada de categorias.
     */
    List<Categoria> findByEmpresaIdOrderByNomeAsc(
            Long empresaId
    );

    /**
     * Retorna todas as categorias raiz (sem categoria pai)
     * pertencentes a uma empresa.
     *
     * <p>
     * Muito utilizado para montagem de árvores hierárquicas
     * no frontend.
     * </p>
     *
     * @param empresaId ID da empresa (tenant).
     * @return Lista de categorias raiz.
     */
    List<Categoria> findByEmpresaIdAndCategoriaPaiIsNull(
            Long empresaId
    );
}
