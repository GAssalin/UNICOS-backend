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
     * Busca um papel pelo nome.
     *
     * @param nome Nome do papel.
     * @return Optional contendo o papel, se encontrado.
     */
    Optional<Role> findByNome(String nome);

    /**
     * Verifica se já existe um papel com o nome informado.
     *
     * @param nome Nome do papel.
     * @return true se existir, false caso contrário.
     */
    boolean existsByNome(String nome);
}
