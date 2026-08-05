package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.enums.TipoContato;
import br.com.unicos.ms_pessoas.model.Contato;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Contato}.
 * <p>
 * Centraliza consultas relacionadas aos meios de contato de uma pessoa,
 * respeitando o contexto multi-tenant (empresa).
 * </p>
 *
 * <p>
 * Este repositório é amplamente utilizado em fluxos de:
 * <ul>
 *     <li>Cadastro e manutenção de pessoas</li>
 *     <li>Definição de contato principal</li>
 *     <li>Processos de autenticação e notificação</li>
 * </ul>
 * </p>
 */
@Repository
public interface ContatoRepository extends BaseTenantRepository<Contato, Long> {

    /**
     * Lista os contatos de uma pessoa pertencente a uma empresa,
     * com suporte à paginação.
     *
     * @param pessoa    Pessoa dona dos contatos.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação e ordenação.
     * @return Página de contatos associados à pessoa.
     */
    Page<Contato> findByPessoaAndEmpresaId(Pessoa pessoa, Long empresaId, Pageable pageable);

    /**
     * Lista os contatos de uma pessoa filtrando pelo tipo de contato,
     * respeitando o contexto multi-tenant.
     *
     * @param pessoa    Pessoa dona dos contatos.
     * @param tipo      Tipo do contato (telefone, celular, e-mail).
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação.
     * @return Página de contatos filtrados por tipo.
     */
    Page<Contato> findByPessoaAndTipoAndEmpresaId(
            Pessoa pessoa,
            TipoContato tipo,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Recupera o contato principal de uma pessoa dentro de uma empresa.
     *
     * @param pessoa    Pessoa dona do contato.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o contato principal, se existir.
     */
    Optional<Contato> findByPessoaAndPrincipalTrueAndEmpresaId(Pessoa pessoa, Long empresaId);

    /**
     * Verifica se já existe um contato com o mesmo valor para uma pessoa
     * dentro da empresa, evitando duplicidade de cadastro.
     *
     * @param pessoa    Pessoa dona do contato.
     * @param valor     Valor do contato (telefone, celular ou e-mail).
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se o contato já existir; {@code false} caso contrário.
     */
    boolean existsByPessoaAndValorAndEmpresaId(Pessoa pessoa, String valor, Long empresaId);

    /**
     * Remove todos os contatos associados a uma pessoa dentro de uma empresa.
     * <p>
     * Normalmente utilizado em processos de exclusão lógica ou física
     * da entidade {@link Pessoa}.
     * </p>
     *
     * @param pessoa    Pessoa dona dos contatos.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByPessoaAndEmpresaId(Pessoa pessoa, Long empresaId);
}
