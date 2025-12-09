package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link AtributoPersonalizado}.
 *
 * <p>
 * Fornece métodos específicos de consulta e verificação
 * para atributos personalizados associados a categorias de produtos.
 * </p>
 */
@Repository
public interface AtributoPersonalizadoRepository extends JpaRepository<AtributoPersonalizado, Long> {

    // ======================================================
    // 🔹 CONSULTAS POR CATEGORIA
    // ======================================================

    /**
     * Retorna todos os atributos personalizados associados a uma categoria.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de atributos personalizados vinculados à categoria.
     */
    List<AtributoPersonalizado> findByCategoriaId(Long categoriaId);

    /**
     * Busca um atributo personalizado pelo nome e categoria.
     * Útil para verificar duplicidade de nomes dentro da mesma categoria.
     *
     * @param categoriaId ID da categoria.
     * @param nome        Nome do atributo.
     * @return Optional contendo o atributo, se encontrado.
     */
    Optional<AtributoPersonalizado> findByCategoriaIdAndNomeIgnoreCase(Long categoriaId, String nome);

    /**
     * Verifica se já existe um atributo com o mesmo nome dentro de uma categoria.
     *
     * @param categoriaId ID da categoria.
     * @param nome        Nome do atributo.
     * @return {@code true} se já existir, {@code false} caso contrário.
     */
    boolean existsByCategoriaIdAndNomeIgnoreCase(Long categoriaId, String nome);

    // ======================================================
    // 🔹 CONSULTAS GERAIS
    // ======================================================

    /**
     * Busca todos os atributos cujo nome contenha o termo informado (case insensitive).
     *
     * @param nome Parte do nome do atributo.
     * @return Lista de atributos correspondentes à busca.
     */
    List<AtributoPersonalizado> findByNomeContainingIgnoreCase(String nome);

    /**
     * Retorna todos os atributos ordenados alfabeticamente por nome.
     *
     * @return Lista ordenada de atributos personalizados.
     */
    List<AtributoPersonalizado> findAllByOrderByNomeAsc();

    /**
     * Busca atributos personalizados filtrando por uma lista de categorias.
     *
     * @param categoriaIds Lista de IDs de categorias.
     * @return Lista de atributos pertencentes às categorias informadas.
     */
    List<AtributoPersonalizado> findByCategoriaIdIn(List<Long> categoriaIds);

    /**
     * Busca todos os atributos de uma categoria cujo nome contenha o termo informado.
     * Útil para filtros contextuais por categoria.
     *
     * @param categoriaId ID da categoria.
     * @param nome        Parte do nome do atributo.
     * @return Lista de atributos da categoria correspondente ao filtro.
     */
    List<AtributoPersonalizado> findByCategoriaIdAndNomeContainingIgnoreCase(Long categoriaId, String nome);
}
