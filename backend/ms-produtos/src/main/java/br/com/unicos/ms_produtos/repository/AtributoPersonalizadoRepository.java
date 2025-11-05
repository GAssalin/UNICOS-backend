package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.AtributoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade AtributoPersonalizado.
 * <p>
 * Fornece métodos específicos de consulta para atributos vinculados a produtos,
 * além das operações CRUD padrão do JpaRepository.
 */
@Repository
public interface AtributoPersonalizadoRepository extends JpaRepository<AtributoPersonalizado, Long> {

    /**
     * Lista todos os atributos personalizados associados a um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de atributos personalizados vinculados ao produto informado.
     */
    List<AtributoPersonalizado> findByProdutoId(Long produtoId);

    /**
     * Busca um atributo personalizado pelo nome e ID do produto.
     * Útil para verificar duplicidade de nomes dentro do mesmo produto.
     *
     * @param produtoId ID do produto.
     * @param nome      Nome do atributo.
     * @return Optional contendo o atributo, se encontrado.
     */
    Optional<AtributoPersonalizado> findByProdutoIdAndNomeIgnoreCase(Long produtoId, String nome);

    /**
     * Busca todos os atributos cujo nome contenha o termo informado.
     *
     * @param nome Parte do nome do atributo.
     * @return Lista de atributos que correspondem à busca.
     */
    List<AtributoPersonalizado> findByNomeContainingIgnoreCase(String nome);

    /**
     * Verifica se já existe um atributo com o mesmo nome dentro de um produto.
     *
     * @param produtoId ID do produto.
     * @param nome      Nome do atributo.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByProdutoIdAndNomeIgnoreCase(Long produtoId, String nome);
}