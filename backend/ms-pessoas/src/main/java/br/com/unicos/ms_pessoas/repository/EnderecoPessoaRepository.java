package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.EnderecoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo gerenciamento de {@link EnderecoPessoa}.
 */
@Repository
public interface EnderecoPessoaRepository extends JpaRepository<EnderecoPessoa, Long> {

    /**
     * Busca todos os endereços de uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return lista de endereços associados
     */
    List<EnderecoPessoa> findByPessoaId(Long pessoaId);

    /**
     * Retorna o endereço principal de uma pessoa, se existir.
     */
    EnderecoPessoa findFirstByPessoaIdAndPrincipalTrue(Long pessoaId);
}
