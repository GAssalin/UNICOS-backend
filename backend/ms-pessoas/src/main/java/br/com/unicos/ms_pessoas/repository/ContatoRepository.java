package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.enums.TipoContato;
import br.com.unicos.ms_pessoas.model.Contato;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Contato}.
 * <p>
 * Permite consultas relacionadas aos meios de comunicação de uma pessoa,
 * como telefone, celular e e-mail, amplamente utilizados em processos de
 * autenticação, notificações e atendimento dentro do UniCoS.
 */
@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    /**
     * Lista todos os contatos associados a uma pessoa.
     *
     * @param pessoa Pessoa proprietária dos contatos.
     * @return Lista de contatos dessa pessoa.
     */
    List<Contato> findByPessoa(Pessoa pessoa);

    /**
     * Lista contatos de uma pessoa filtrados por tipo.
     *
     * @param pessoa Pessoa proprietária dos contatos.
     * @param tipo   Tipo de contato (telefone, celular, e-mail).
     * @return Lista de contatos do tipo informado.
     */
    List<Contato> findByPessoaAndTipo(Pessoa pessoa, TipoContato tipo);

    /**
     * Busca o contato principal de uma pessoa, caso exista.
     *
     * @param pessoa Pessoa proprietária.
     * @return Contato principal.
     */
    Optional<Contato> findByPessoaAndPrincipalTrue(Pessoa pessoa);

    /**
     * Verifica se já existe um contato com o valor informado.
     *
     * @param valor E-mail ou número do contato.
     * @return Contato encontrado, caso exista.
     */
    Optional<Contato> findByValor(String valor);
}
