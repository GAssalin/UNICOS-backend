package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Permissao.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    /**
     * Busca uma permissão pelo codigo.
     *
     * @param codigo Codigo da permissão.
     * @return Optional contendo a permissão, se encontrada.
     */
    Optional<Permissao> findByCodigo(String codigo);

    /**
     * Verifica se já existe uma permissão com o codigo informado.
     *
     * @param codigo Codigo da permissão.
     * @return true se existir, false caso contrário.
     */
    boolean existsByCodigo(String codigo);
}
