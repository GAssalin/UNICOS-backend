package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.model.ContatoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ContatoEmpresa}.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface ContatoEmpresaRepository extends JpaRepository<ContatoEmpresa, Long> {

    /**
     * Lista todos os contatos vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de contatos pertencentes à empresa informada.
     */
    List<ContatoEmpresa> findByEmpresaId(Long empresaId);

    /**
     * Busca contatos cujo nome contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nomeContato Termo de busca.
     * @return Lista de contatos correspondentes ao termo informado.
     */
    List<ContatoEmpresa> findByNomeContatoContainingIgnoreCase(String nomeContato);

    /**
     * Busca contatos cujo cargo contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param cargo Termo de busca.
     * @return Lista de contatos correspondentes ao cargo informado.
     */
    List<ContatoEmpresa> findByCargoContainingIgnoreCase(String cargo);

    /**
     * Busca um contato pelo endereço de e-mail.
     *
     * @param email E-mail do contato.
     * @return Optional contendo o contato, se encontrado.
     */
    Optional<ContatoEmpresa> findByEmail(String email);

    /**
     * Verifica se já existe um contato cadastrado com o mesmo e-mail.
     *
     * @param email E-mail do contato.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByEmail(String email);

    /**
     * Busca contatos cujo telefone ou celular contenham um determinado número.
     *
     * @param telefone Número parcial ou completo do telefone.
     * @param celular  Número parcial ou completo do celular.
     * @return Lista de contatos correspondentes ao número informado.
     */
    List<ContatoEmpresa> findByTelefoneContainingOrCelularContaining(String telefone, String celular);

    /**
     * Lista todos os contatos ativos no sistema.
     *
     * @return Lista de contatos com status ativo = true.
     */
    List<ContatoEmpresa> findByAtivoTrue();

    /**
     * Lista todos os contatos inativos no sistema.
     *
     * @return Lista de contatos com status ativo = false.
     */
    List<ContatoEmpresa> findByAtivoFalse();

    /**
     * Lista contatos ativos de uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de contatos ativos da empresa informada.
     */
    List<ContatoEmpresa> findByEmpresaIdAndAtivoTrue(Long empresaId);

    /**
     * Lista contatos inativos de uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de contatos inativos da empresa informada.
     */
    List<ContatoEmpresa> findByEmpresaIdAndAtivoFalse(Long empresaId);

    /**
     * Busca contatos de uma empresa com base em parte do e-mail informado.
     *
     * @param empresaId ID da empresa.
     * @param email     Termo parcial de e-mail.
     * @return Lista de contatos da empresa cujo e-mail corresponda ao termo informado.
     */
    List<ContatoEmpresa> findByEmpresaIdAndEmailContainingIgnoreCase(Long empresaId, String email);

    /**
     * Lista todos os contatos ordenados pelo nome do contato (ordem ascendente).
     *
     * @return Lista de contatos ordenados alfabeticamente.
     */
    List<ContatoEmpresa> findAllByOrderByNomeContatoAsc();

    /**
     * Lista contatos de uma empresa ordenados pelo nome do contato.
     *
     * @param empresaId ID da empresa.
     * @return Lista de contatos ordenada pelo nome.
     */
    List<ContatoEmpresa> findByEmpresaIdOrderByNomeContatoAsc(Long empresaId);
}
