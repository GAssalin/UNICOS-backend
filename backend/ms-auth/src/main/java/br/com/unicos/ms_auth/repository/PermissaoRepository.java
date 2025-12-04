package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelo acesso aos dados da entidade Permissao.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    /**
     * Verifica se já existe uma permissão com o nome informado.
     *
     * @param nome Nome da permissão.
     * @return true se existir, false caso contrário.
     */
    boolean existsByNome(String nome);
}
