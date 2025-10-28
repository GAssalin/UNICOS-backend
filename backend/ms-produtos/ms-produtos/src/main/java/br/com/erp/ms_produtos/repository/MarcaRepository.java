package br.com.erp.ms_produtos.repository;

import br.com.erp.ms_produtos.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Marca.
 */
@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    /**
     * Busca uma marca pelo nome, ignorando maiúsculas e minúsculas.
     *
     * @param nome Nome da marca.
     * @return Optional contendo a marca, se encontrada.
     */
    Optional<Marca> findByNomeIgnoreCase(String nome);

    /**
     * Retorna uma lista de marcas cujo nome contenha o termo informado (busca parcial).
     *
     * @param nome Termo de busca.
     * @return Lista de marcas correspondentes.
     */
    List<Marca> findByNomeContainingIgnoreCase(String nome);

    /**
     * Verifica se já existe uma marca com o nome informado (case-insensitive).
     *
     * @param nome Nome da marca.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Lista todas as marcas ordenadas alfabeticamente pelo nome.
     *
     * @return Lista de marcas ordenadas por nome.
     */
    List<Marca> findAllByOrderByNomeAsc();
}