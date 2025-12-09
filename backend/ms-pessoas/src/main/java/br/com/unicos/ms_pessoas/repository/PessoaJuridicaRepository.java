package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link PessoaJuridica}.
 * <p>
 * Fornece consultas customizadas relacionadas a empresas, como busca por CNPJ,
 * razão social e nome fantasia, amplamente utilizadas em módulos financeiros,
 * fiscais e de contratos dentro do UniCoS.
 */
@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {

    /**
     * Busca uma Pessoa Jurídica pelo CNPJ.
     *
     * @param cnpj CNPJ sem formatação.
     * @return Pessoa Jurídica correspondente, caso exista.
     */
    Optional<PessoaJuridica> findByCnpj(String cnpj);

    /**
     * Busca empresas pela razão social exata.
     *
     * @param razaoSocial Razão social completa.
     * @return Lista de empresas com a razão social informada.
     */
    List<PessoaJuridica> findByRazaoSocial(String razaoSocial);

    /**
     * Busca empresas cujo nome fantasia contenha o termo informado.
     *
     * @param nomeFantasia Parte do nome fantasia.
     * @return Lista de empresas que contenham o termo informado.
     */
    List<PessoaJuridica> findByNomeFantasiaContainingIgnoreCase(String nomeFantasia);

    /**
     * Busca pessoa jurídica pelo nome fantasia (exato).
     */
    List<PessoaJuridica> findByNomeFantasia(String nomeFantasia);

    /**
     * Lista empresas cujo nome contenha o texto informado,
     * ignorando maiúsculas e minúsculas.
     */
    List<PessoaJuridica> findByNomeContainingIgnoreCase(String nome);
}
