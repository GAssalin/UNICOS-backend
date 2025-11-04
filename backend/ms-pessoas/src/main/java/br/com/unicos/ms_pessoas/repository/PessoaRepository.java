package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Pessoa}.
 */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    /**
     * Busca todas as pessoas pelo tipo informado.
     *
     * @param tipoPessoa tipo da pessoa (FÍSICA ou JURÍDICA)
     * @return lista de pessoas do tipo especificado
     */
    List<Pessoa> findByTipoPessoa(TipoPessoa tipoPessoa);

    /**
     * Verifica se existe uma pessoa ativa com o nome informado.
     *
     * @param nome nome da pessoa
     * @return true se existir
     */
    boolean existsByNomeIgnoreCaseAndAtivoTrue(String nome);
}
