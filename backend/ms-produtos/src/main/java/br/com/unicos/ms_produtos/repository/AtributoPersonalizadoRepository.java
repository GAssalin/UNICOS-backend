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
 * Todas as consultas deste repositório são restritas ao contexto
 * de uma empresa (tenant), identificado pelo {@code empresaId}.
 * </p>
 *
 * <p>
 * Esse repositório garante que atributos personalizados:
 * <ul>
 *     <li>não sejam compartilhados entre empresas</li>
 *     <li>não tenham nomes duplicados dentro da mesma categoria e empresa</li>
 * </ul>
 * </p>
 */
@Repository
public interface AtributoPersonalizadoRepository extends JpaRepository<AtributoPersonalizado, Long> {

    // ======================================================
    // 🔹 CONSULTAS POR CATEGORIA (TENANT-AWARE)
    // ======================================================

    /**
     * Retorna todos os atributos personalizados associados
     * a uma categoria específica dentro de uma empresa.
     *
     * @param empresaId   ID da empresa (tenant).
     * @param categoriaId ID da categoria.
     * @return Lista de atributos personalizados da categoria.
     */
    List<AtributoPersonalizado> findByEmpresaIdAndCategoriaId(
            Long empresaId,
            Long categoriaId
    );

    /**
     * Busca um atributo personalizado pelo nome e categoria,
     * restringindo a busca ao contexto da empresa.
     *
     * <p>
     * Utilizado principalmente para validação de duplicidade
     * de nomes dentro da mesma categoria.
     * </p>
     *
     * @param empresaId   ID da empresa (tenant).
     * @param categoriaId ID da categoria.
     * @param nome        Nome do atributo.
     * @return {@link Optional} contendo o atributo, se existir.
     */
    Optional<AtributoPersonalizado> findByEmpresaIdAndCategoriaIdAndNomeIgnoreCase(
            Long empresaId,
            Long categoriaId,
            String nome
    );

    /**
     * Verifica se já existe um atributo personalizado com o mesmo nome
     * dentro de uma categoria e empresa.
     *
     * @param empresaId   ID da empresa (tenant).
     * @param categoriaId ID da categoria.
     * @param nome        Nome do atributo.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndCategoriaIdAndNomeIgnoreCase(
            Long empresaId,
            Long categoriaId,
            String nome
    );

    // ======================================================
    // 🔹 CONSULTAS GERAIS (TENANT-AWARE)
    // ======================================================

    /**
     * Busca atributos personalizados cujo nome contenha o termo informado,
     * restringindo o resultado à empresa informada.
     *
     * @param empresaId ID da empresa (tenant).
     * @param nome      Parte do nome do atributo.
     * @return Lista de atributos correspondentes ao filtro.
     */
    List<AtributoPersonalizado> findByEmpresaIdAndNomeContainingIgnoreCase(
            Long empresaId,
            String nome
    );

    /**
     * Retorna todos os atributos personalizados de uma empresa,
     * ordenados alfabeticamente pelo nome.
     *
     * @param empresaId ID da empresa (tenant).
     * @return Lista ordenada de atributos personalizados.
     */
    List<AtributoPersonalizado> findByEmpresaIdOrderByNomeAsc(
            Long empresaId
    );

    /**
     * Busca atributos personalizados pertencentes a uma lista
     * específica de categorias dentro de uma empresa.
     *
     * @param empresaId    ID da empresa (tenant).
     * @param categoriaIds Lista de IDs de categorias.
     * @return Lista de atributos encontrados.
     */
    List<AtributoPersonalizado> findByEmpresaIdAndCategoriaIdIn(
            Long empresaId,
            List<Long> categoriaIds
    );

    /**
     * Busca atributos personalizados de uma categoria específica
     * cujo nome contenha o termo informado.
     *
     * @param empresaId   ID da empresa (tenant).
     * @param categoriaId ID da categoria.
     * @param nome        Parte do nome do atributo.
     * @return Lista de atributos filtrados.
     */
    List<AtributoPersonalizado> findByEmpresaIdAndCategoriaIdAndNomeContainingIgnoreCase(
            Long empresaId,
            Long categoriaId,
            String nome
    );
}
