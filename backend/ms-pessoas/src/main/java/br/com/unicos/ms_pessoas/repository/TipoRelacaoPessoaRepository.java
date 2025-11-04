package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo gerenciamento de {@link TipoRelacaoPessoa}.
 */
@Repository
public interface TipoRelacaoPessoaRepository extends JpaRepository<TipoRelacaoPessoa, Long> {

    /**
     * Busca um tipo de relação pelo código.
     */
    Optional<TipoRelacaoPessoa> findByCodigoIgnoreCase(String codigo);

    /**
     * Verifica se já existe um tipo de relação com o código informado.
     */
    boolean existsByCodigoIgnoreCase(String codigo);
}
