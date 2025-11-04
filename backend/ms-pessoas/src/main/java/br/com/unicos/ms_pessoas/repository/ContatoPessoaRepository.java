package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.ContatoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo gerenciamento de {@link ContatoPessoa}.
 */
@Repository
public interface ContatoPessoaRepository extends JpaRepository<ContatoPessoa, Long> {

    /**
     * Retorna todos os contatos de uma pessoa.
     */
    List<ContatoPessoa> findByPessoaId(Long pessoaId);

    /**
     * Retorna o contato principal da pessoa.
     */
    ContatoPessoa findFirstByPessoaIdAndPrincipalTrue(Long pessoaId);
}
