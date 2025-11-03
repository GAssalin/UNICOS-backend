package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.model.ContatoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade ContatoEmpresa.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
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
     * Busca contatos cujo telefone ou celular contenham um determinado número.
     *
     * @param telefone Número parcial ou completo de telefone/celular.
     * @return Lista de contatos correspondentes ao número informado.
     */
    List<ContatoEmpresa> findByTelefoneContainingOrCelularContaining(String telefone, String celular);

    /**
     * Verifica se já existe um contato cadastrado com o mesmo e-mail.
     *
     * @param email E-mail do contato.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByEmail(String email);
}