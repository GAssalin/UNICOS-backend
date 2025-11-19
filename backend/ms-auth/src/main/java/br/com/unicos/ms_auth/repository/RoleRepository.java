package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Role.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca um papel pelo codigo.
     *
     * @param codigo Codigo do papel.
     * @return Optional contendo o papel, se encontrado.
     */
    Optional<Role> findByCodigo(String codigo);

    /**
     * Verifica se já existe um papel com o codigo informado.
     *
     * @param codigo Codigo do papel.
     * @return true se existir, false caso contrário.
     */
    boolean existsByCodigo(String codigo);
}
