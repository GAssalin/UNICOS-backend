package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pela persistência de {@link PessoaFisica}.
 */
@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    /**
     * Busca uma pessoa física pelo CPF.
     *
     * @param cpf CPF no formato 000.000.000-00
     * @return pessoa física, se encontrada
     */
    Optional<PessoaFisica> findByCpf(String cpf);

    /**
     * Verifica se já existe uma pessoa física com o CPF informado.
     */
    boolean existsByCpf(String cpf);
}
