package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Pessoa}.
 * <p>
 * Fornece operações CRUD padrão e consultas customizadas que permitem
 * localizar pessoas por nome, tipo ou outras classificações relevantes
 * dentro do UniCoS.
 */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    /**
     * Busca pessoas pelo nome exato.
     *
     * @param nome Nome da pessoa.
     * @return Lista de pessoas com o nome informado.
     */
    List<Pessoa> findByNome(String nome);

    /**
     * Busca pessoas cujo nome contenha o termo informado,
     * ignorando diferenciação de maiúsculas/minúsculas.
     *
     * @param nome Parte do nome da pessoa.
     * @return Lista de pessoas que contenham o termo informado.
     */
    List<Pessoa> findByNomeContainingIgnoreCase(String nome);

    /**
     * Lista todas as pessoas de um determinado tipo
     * (Pessoa Física ou Pessoa Jurídica).
     *
     * @param tipo Tipo da pessoa.
     * @return Lista de pessoas do tipo informado.
     */
    List<Pessoa> findByTipoPessoa(TipoPessoa tipo);
}
