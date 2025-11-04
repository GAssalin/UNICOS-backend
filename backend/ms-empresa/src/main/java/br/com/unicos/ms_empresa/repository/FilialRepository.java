package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Filial}.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
 */
@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {

    /**
     * Lista todas as filiais associadas a uma empresa matriz específica.
     *
     * @param empresaId ID da empresa matriz.
     * @return Lista de filiais vinculadas à empresa informada.
     */
    List<Filial> findByEmpresaId(Long empresaId);

    /**
     * Lista todas as filiais ativas.
     *
     * @return Lista de filiais com o campo "ativo" igual a true.
     */
    List<Filial> findByAtivoTrue();

    /**
     * Lista todas as filiais inativas.
     *
     * @return Lista de filiais com o campo "ativo" igual a false.
     */
    List<Filial> findByAtivoFalse();

    /**
     * Lista todas as filiais de uma empresa específica que estejam ativas.
     *
     * @param empresaId ID da empresa matriz.
     * @return Lista de filiais ativas vinculadas à empresa informada.
     */
    List<Filial> findByEmpresaIdAndAtivoTrue(Long empresaId);

    /**
     * Busca uma filial pelo seu CNPJ.
     *
     * @param cnpj CNPJ da filial.
     * @return Optional contendo a filial, se encontrada.
     */
    Optional<Filial> findByCnpj(String cnpj);

    /**
     * Verifica se já existe uma filial cadastrada com o mesmo CNPJ.
     *
     * @param cnpj CNPJ da filial.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Busca filiais cujo nome contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome Termo parcial de busca.
     * @return Lista de filiais correspondentes ao termo informado.
     */
    List<Filial> findByNomeContainingIgnoreCase(String nome);

    /**
     * Lista todas as filiais de uma empresa cujo nome contenha um termo específico.
     *
     * @param empresaId ID da empresa matriz.
     * @param nome      Termo parcial de busca.
     * @return Lista de filiais correspondentes.
     */
    List<Filial> findByEmpresaIdAndNomeContainingIgnoreCase(Long empresaId, String nome);

    /**
     * Lista todas as filiais ordenadas alfabeticamente pelo nome.
     *
     * @return Lista de filiais ordenadas por nome.
     */
    List<Filial> findAllByOrderByNomeAsc();

    /**
     * Lista todas as filiais de uma empresa, ordenadas alfabeticamente pelo nome.
     *
     * @param empresaId ID da empresa.
     * @return Lista de filiais da empresa, ordenadas por nome.
     */
    List<Filial> findByEmpresaIdOrderByNomeAsc(Long empresaId);

    /**
     * Busca uma filial ativa pelo CNPJ.
     *
     * @param cnpj CNPJ da filial.
     * @return Optional contendo a filial ativa, se encontrada.
     */
    Optional<Filial> findByCnpjAndAtivoTrue(String cnpj);

    /**
     * Lista filiais cujo nome contenha o termo informado e que estejam ativas.
     *
     * @param nome Termo parcial do nome.
     * @return Lista de filiais ativas que correspondem ao termo informado.
     */
    List<Filial> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    /**
     * Busca filiais cujo nome fantasia contenha o termo informado.
     *
     * @param nomeFantasia Termo parcial de busca.
     * @return Lista de filiais correspondentes.
     */
    List<Filial> findByNomeFantasiaContainingIgnoreCase(String nomeFantasia);

    /**
     * Lista todas as filiais ordenadas alfabeticamente pela razão social.
     *
     * @return Lista ordenada por razão social.
     */
    List<Filial> findAllByOrderByRazaoSocialAsc();
}
