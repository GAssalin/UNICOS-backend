package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import br.com.unicos.ms_pessoas.model.Endereco;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Endereco}.
 * <p>
 * Oferece consultas específicas para gerenciamento de endereços relacionados
 * a pessoas físicas e jurídicas, incluindo filtros por tipo, município e
 * identificação do endereço principal.
 */
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    /**
     * Retorna todos os endereços pertencentes a uma pessoa.
     *
     * @param pessoa Pessoa proprietária dos endereços.
     * @return Lista de endereços associados.
     */
    List<Endereco> findByPessoa(Pessoa pessoa);

    /**
     * Busca endereços de uma pessoa filtrados por tipo (residencial, comercial etc.).
     *
     * @param pessoa Pessoa proprietária.
     * @param tipo   Tipo do endereço.
     * @return Lista de endereços do tipo informado.
     */
    List<Endereco> findByPessoaAndTipo(Pessoa pessoa, TipoEndereco tipo);

    /**
     * Retorna o endereço principal de uma pessoa, caso exista.
     *
     * @param pessoa Pessoa proprietária.
     * @return Endereço principal.
     */
    Optional<Endereco> findByPessoaAndPrincipalTrue(Pessoa pessoa);

    /**
     * Lista todos os endereços localizados em um município.
     *
     * @param municipio Município desejado.
     * @return Lista de endereços associados ao município.
     */
    List<Endereco> findByMunicipio(Municipio municipio);

    /**
     * Lista endereços filtrados por CEP.
     *
     * @param cep Código postal.
     * @return Lista de endereços com o CEP informado.
     */
    List<Endereco> findByCep(String cep);
}
