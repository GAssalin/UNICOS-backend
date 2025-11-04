package br.com.unicos.ms_empresa.repository;

import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.model.EnderecoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EnderecoEmpresa}.
 * <p>
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo {@link JpaRepository}.
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
     * Lista todos os endereços vinculados a uma filial específica.
     *
     * @param filialId ID da filial.
     * @return Lista de endereços pertencentes à filial informada.
     */
    List<EnderecoEmpresa> findByFilialId(Long filialId);

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
     * Lista todos os endereços de um determinado estado (UF).
     *
     * @param estado Sigla ou nome do estado.
     * @return Lista de endereços localizados na UF informada.
     */
    List<EnderecoEmpresa> findByEstadoIgnoreCase(String estado);

    /**
     * Busca endereços de um tipo específico (ex: MATRIZ, FATURAMENTO, ENTREGA).
     *
     * @param tipoEndereco Tipo de endereço.
     * @return Lista de endereços correspondentes ao tipo informado.
     */
    List<EnderecoEmpresa> findByTipoEndereco(TipoEnderecoEmpresa tipoEndereco);

    /**
     * Verifica se uma empresa já possui um endereço cadastrado de um tipo específico.
     *
     * @param empresaId    ID da empresa.
     * @param tipoEndereco Tipo de endereço.
     * @return true se já existir, false caso contrário.
     */
    boolean existsByEmpresaIdAndTipoEndereco(Long empresaId, TipoEnderecoEmpresa tipoEndereco);

    /**
     * Lista endereços filtrados por tipo e cidade.
     *
     * @param tipoEndereco Tipo de endereço.
     * @param cidade       Nome da cidade.
     * @return Lista de endereços correspondentes ao tipo e cidade informados.
     */
    List<EnderecoEmpresa> findByTipoEnderecoAndCidadeIgnoreCase(TipoEnderecoEmpresa tipoEndereco, String cidade);

    /**
     * Lista endereços filtrados por tipo e estado.
     *
     * @param tipoEndereco Tipo de endereço.
     * @param estado       Sigla do estado.
     * @return Lista de endereços correspondentes ao tipo e estado informados.
     */
    List<EnderecoEmpresa> findByTipoEnderecoAndEstadoIgnoreCase(TipoEnderecoEmpresa tipoEndereco, String estado);

    /**
     * Lista todos os endereços de uma empresa, ordenados alfabeticamente pela cidade.
     *
     * @param empresaId ID da empresa.
     * @return Lista de endereços ordenada por cidade.
     */
    List<EnderecoEmpresa> findByEmpresaIdOrderByCidadeAsc(Long empresaId);

    /**
     * Lista todos os endereços ordenados por estado e cidade.
     *
     * @return Lista de endereços ordenada por estado e cidade.
     */
    List<EnderecoEmpresa> findAllByOrderByEstadoAscCidadeAsc();
}
