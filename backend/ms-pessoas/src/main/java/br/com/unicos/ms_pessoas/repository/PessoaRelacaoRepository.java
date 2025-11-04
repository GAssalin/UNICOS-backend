package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.PessoaRelacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pela persistência de {@link PessoaRelacao}.
 */
@Repository
public interface PessoaRelacaoRepository extends JpaRepository<PessoaRelacao, Long> {

    /**
     * Retorna todas as relações de uma pessoa.
     */
    List<PessoaRelacao> findByPessoaId(Long pessoaId);

    /**
     * Busca relações ativas de uma pessoa.
     */
    List<PessoaRelacao> findByPessoaIdAndAtivoTrue(Long pessoaId);
}
