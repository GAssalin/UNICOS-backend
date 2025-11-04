package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Setor}.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {

    /**
     * Lista todos os setores pertencentes a um determinado departamento.
     *
     * @param departamentoId ID do departamento.
     * @return Lista de setores vinculados ao departamento informado.
     */
    List<Setor> findByDepartamentoId(Long departamentoId);

    /**
     * Busca setores cujo nome contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome Termo de busca parcial.
     * @return Lista de setores correspondentes ao termo informado.
     */
    List<Setor> findByNomeContainingIgnoreCase(String nome);

    /**
     * Lista todos os setores ativos.
     *
     * @return Lista de setores com o campo "ativo" igual a true.
     */
    List<Setor> findByAtivoTrue();

    /**
     * Lista todos os setores inativos.
     *
     * @return Lista de setores com o campo "ativo" igual a false.
     */
    List<Setor> findByAtivoFalse();

    /**
     * Lista todos os setores ativos de um determinado departamento.
     *
     * @param departamentoId ID do departamento.
     * @return Lista de setores ativos vinculados ao departamento informado.
     */
    List<Setor> findByDepartamentoIdAndAtivoTrue(Long departamentoId);

    /**
     * Lista todos os setores inativos de um determinado departamento.
     *
     * @param departamentoId ID do departamento.
     * @return Lista de setores inativos vinculados ao departamento informado.
     */
    List<Setor> findByDepartamentoIdAndAtivoFalse(Long departamentoId);

    /**
     * Busca um setor pelo nome exato dentro de um departamento específico.
     *
     * @param nome           Nome do setor.
     * @param departamentoId ID do departamento.
     * @return Optional contendo o setor, se encontrado.
     */
    Optional<Setor> findByNomeAndDepartamentoId(String nome, Long departamentoId);

    /**
     * Verifica se já existe um setor com o mesmo nome dentro de um departamento.
     *
     * @param nome           Nome do setor.
     * @param departamentoId ID do departamento.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByNomeAndDepartamentoId(String nome, Long departamentoId);

    /**
     * Lista todos os setores ordenados alfabeticamente pelo nome.
     *
     * @return Lista de setores ordenada por nome.
     */
    List<Setor> findAllByOrderByNomeAsc();

    /**
     * Lista todos os setores de um determinado departamento,
     * ordenados alfabeticamente pelo nome.
     *
     * @param departamentoId ID do departamento.
     * @return Lista de setores ordenada por nome.
     */
    List<Setor> findByDepartamentoIdOrderByNomeAsc(Long departamentoId);

    /**
     * Lista setores cujo nome contenha o termo informado
     * e que estejam ativos.
     *
     * @param nome Termo parcial de busca.
     * @return Lista de setores ativos correspondentes ao termo informado.
     */
    List<Setor> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
}
