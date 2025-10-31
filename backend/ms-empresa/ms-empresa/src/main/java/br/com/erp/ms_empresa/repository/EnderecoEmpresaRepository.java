package br.com.erp.ms_empresa.repository;

import br.com.erp.ms_empresa.model.EnderecoEmpresa;
import br.com.erp.ms_empresa.model.TipoEnderecoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade EnderecoEmpresa.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface EnderecoEmpresaRepository extends JpaRepository<EnderecoEmpresa, Long> {

    /**
     * Lista todos os endereços vinculados a uma empresa específica.
     *
     * @param empresaId ID da empresa.
     * @return Lista de endereços pertencentes à empresa informada.
     */
    List<EnderecoEmpresa> findByEmpresaId(Long empresaId);

    /**
     * Busca um endereço pelo seu CEP.
     *
     * @param cep CEP do endereço.
     * @return Optional contendo o endereço, se encontrado.
     */
    Optional<EnderecoEmpresa> findByCep(String cep);

    /**
     * Busca todos os endereços de uma determinada cidade.
     *
     * @param cidade Nome da cidade.
     * @return Lista de endereços localizados na cidade informada.
     */
    List<EnderecoEmpresa> findByCidadeIgnoreCase(String cidade);

    /**
     * Lista todos os endereços de uma determinada unidade federativa (UF).
     *
     * @param uf Sigla do estado (ex: SP, RJ, MG).
     * @return Lista de endereços localizados na UF informada.
     */
    List<EnderecoEmpresa> findByUfIgnoreCase(String uf);

    /**
     * Busca endereços de um tipo específico (ex: MATRIZ, FATURAMENTO, ENTREGA).
     *
     * @param tipo Tipo de endereço.
     * @return Lista de endereços correspondentes ao tipo informado.
     */
    List<EnderecoEmpresa> findByTipo(TipoEnderecoEmpresa tipo);

    /**
     * Verifica se uma empresa já possui um endereço cadastrado de um tipo específico.
     *
     * @param empresaId ID da empresa.
     * @param tipo Tipo de endereço.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByEmpresaIdAndTipo(Long empresaId, TipoEnderecoEmpresa tipo);
}
