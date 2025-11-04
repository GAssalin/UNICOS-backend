package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pela persistência de {@link PessoaJuridica}.
 */
@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {

    /**
     * Busca uma pessoa jurídica pelo CNPJ.
     *
     * @param cnpj CNPJ no formato 00.000.000/0000-00
     * @return pessoa jurídica, se encontrada
     */
    Optional<PessoaJuridica> findByCnpj(String cnpj);

    /**
     * Verifica se já existe uma empresa com o CNPJ informado.
     */
    boolean existsByCnpj(String cnpj);
}
