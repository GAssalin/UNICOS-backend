package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.UnidadeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade UnidadeMedida.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além das operações CRUD padrão fornecidas pelo JpaRepository.
 */
@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Long> {

    /**
     * Busca uma unidade de medida pelo nome (ignora maiúsculas e minúsculas).
     *
     * @param nome Nome da unidade de medida.
     * @return Optional contendo a unidade, se encontrada.
     */
    Optional<UnidadeMedida> findByNomeIgnoreCase(String nome);

    /**
     * Busca uma unidade de medida pela sigla (ignora maiúsculas e minúsculas).
     *
     * @param sigla Sigla da unidade de medida.
     * @return Optional contendo a unidade, se encontrada.
     */
    Optional<UnidadeMedida> findBySiglaIgnoreCase(String sigla);

    /**
     * Retorna uma lista de unidades de medida cujo nome contenha o termo informado.
     *
     * @param nome Termo de busca (parte do nome).
     * @return Lista de unidades correspondentes.
     */
    List<UnidadeMedida> findByNomeContainingIgnoreCase(String nome);

    /**
     * Verifica se já existe uma unidade de medida com a sigla informada (ignora maiúsculas e minúsculas).
     *
     * @param sigla Sigla da unidade de medida.
     * @return true se já existir, false caso contrário.
     */
    boolean existsBySiglaIgnoreCase(String sigla);

    /**
     * Lista todas as unidades de medida ordenadas alfabeticamente pelo nome.
     *
     * @return Lista ordenada de unidades de medida.
     */
    List<UnidadeMedida> findAllByOrderByNomeAsc();
}