package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link Categoria}.
 * <p>
 * Fornece métodos de consulta personalizados para busca, ordenação
 * e verificação de existência de categorias.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca uma categoria pelo nome exato, ignorando diferenças de maiúsculas e minúsculas.
     *
     * @param nome Nome da categoria.
     * @return {@link Optional} contendo a categoria correspondente, caso exista.
     */
    Optional<Categoria> findByNomeIgnoreCase(String nome);

    /**
     * Busca todas as categorias cujo nome contenha o termo informado (case insensitive).
     * Útil para pesquisas parciais em telas de listagem e autocomplete.
     *
     * @param nome Parte do nome da categoria.
     * @return Lista de categorias correspondentes.
     */
    List<Categoria> findByNomeContainingIgnoreCase(String nome);

    /**
     * Verifica se já existe uma categoria cadastrada com o nome informado.
     * Ignora diferenças de maiúsculas e minúsculas.
     *
     * @param nome Nome da categoria a verificar.
     * @return {@code true} se já existir uma categoria com o mesmo nome, caso contrário {@code false}.
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Retorna todas as categorias ordenadas alfabeticamente pelo nome.
     *
     * @return Lista de categorias em ordem crescente de nome.
     */
    List<Categoria> findAllByOrderByNomeAsc();
}
