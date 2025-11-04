package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.TokenAcesso;
import br.com.unicos.ms_auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade TokenAcesso.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface TokenAcessoRepository extends JpaRepository<TokenAcesso, Long> {

    /**
     * Busca um token de acesso pelo valor do token JWT.
     *
     * @param token Valor do token JWT.
     * @return Optional contendo o token, se encontrado.
     */
    Optional<TokenAcesso> findByToken(String token);

    /**
     * Lista todos os tokens válidos de um usuário.
     *
     * @param usuario Entidade Usuario.
     * @return Lista de tokens válidos.
     */
    List<TokenAcesso> findByUsuarioAndValidoTrue(Usuario usuario);

    /**
     * Verifica se existe um token ativo para determinado valor.
     *
     * @param token Valor do token JWT.
     * @return true se o token for válido e ativo, false caso contrário.
     */
    boolean existsByTokenAndValidoTrue(String token);
}
