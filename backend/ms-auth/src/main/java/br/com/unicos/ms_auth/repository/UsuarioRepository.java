package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Usuario.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo nome de usuário.
     *
     * @param username Nome de usuário.
     * @return Optional contendo o usuário, se encontrado.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email E-mail do usuário.
     * @return Optional contendo o usuário, se encontrado.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Lista todos os usuários ativos.
     *
     * @return Lista de usuários com o campo "ativo" igual a true.
     */
    List<Usuario> findByAtivoTrue();

    /**
     * Lista todos os usuários inativos.
     *
     * @return Lista de usuários com o campo "ativo" igual a false.
     */
    List<Usuario> findByAtivoFalse();
}
