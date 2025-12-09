package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link PessoaFisica}.
 * <p>
 * Permite consultas específicas relacionadas a pessoas físicas, como pesquisa por CPF,
 * nome social e filtros complementares utilizados nos módulos do UniCoS.
 */
@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    /**
     * Busca uma Pessoa Física pelo CPF.
     *
     * @param cpf CPF sem formatação.
     * @return Pessoa Física correspondente, caso exista.
     */
    Optional<PessoaFisica> findByCpf(String cpf);

    /**
     * Lista todas as pessoas físicas com o nome social informado.
     *
     * @param nomeSocial Nome social da pessoa.
     * @return Lista de pessoas físicas com o nome social indicado.
     */
    List<PessoaFisica> findByNomeSocial(String nomeSocial);

    /**
     * Busca pessoas físicas cujo nome contenha o termo informado.
     *
     * @param nome Parte do nome da pessoa.
     * @return Lista de pessoas físicas que contenham o nome indicado.
     */
    List<PessoaFisica> findByNomeContainingIgnoreCase(String nome);
}
