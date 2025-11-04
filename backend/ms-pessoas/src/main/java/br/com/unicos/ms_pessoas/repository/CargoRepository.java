package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pela persistência de {@link Cargo}.
 */
@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    /**
     * Verifica se já existe um cargo com o nome informado.
     */
    boolean existsByNomeIgnoreCase(String nome);
}
