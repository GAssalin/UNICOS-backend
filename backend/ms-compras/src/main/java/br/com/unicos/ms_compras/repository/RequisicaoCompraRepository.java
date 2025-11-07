package br.com.unicos.ms_compras.repository;

import br.com.unicos.ms_compras.enums.TipoRequisicaoCompra;
import br.com.unicos.ms_compras.model.requisicao.RequisicaoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pela persistência e consultas de {@link RequisicaoCompra}.
 */
@Repository
public interface RequisicaoCompraRepository extends JpaRepository<RequisicaoCompra, Long> {

    // -----------------------------------------------------------------------
    // Identificação e unicidade
    // -----------------------------------------------------------------------

    /**
     * Busca uma requisição de compra pelo seu código identificador único.
     *
     * @param codigo código da requisição
     * @return requisição correspondente, se existir
     */
    Optional<RequisicaoCompra> findByCodigo(String codigo);

    /**
     * Verifica se já existe uma requisição com o código informado.
     *
     * @param codigo código da requisição
     * @return true se existir, false caso contrário
     */
    boolean existsByCodigo(String codigo);

    // -----------------------------------------------------------------------
    // Consultas diretas por domínio
    // -----------------------------------------------------------------------

    /**
     * Lista todas as requisições de um determinado tipo (reposição, interna, urgência etc.).
     *
     * @param tipo tipo da requisição
     * @return lista de requisições
     */
    List<RequisicaoCompra> findByTipoRequisicao(TipoRequisicaoCompra tipo);

    /**
     * Lista todas as requisições abertas por um solicitante específico.
     *
     * @param solicitanteId ID do solicitante
     * @return lista de requisições
     */
    List<RequisicaoCompra> findBySolicitanteId(Long solicitanteId);

    /**
     * Lista requisições abertas dentro de um intervalo de datas.
     *
     * @param inicio data inicial (inclusive)
     * @param fim    data final (inclusive)
     * @return lista de requisições no período
     */
    List<RequisicaoCompra> findByDataAberturaBetween(LocalDate inicio, LocalDate fim);

    // -----------------------------------------------------------------------
    // Consultas textuais e paginadas
    // -----------------------------------------------------------------------

    /**
     * Pesquisa paginada de requisições por código ou observação (case-insensitive).
     *
     * @param termo    termo textual
     * @param pageable parâmetros de paginação
     * @return página de resultados
     */
    @Query("""
           SELECT r
             FROM RequisicaoCompra r
            WHERE LOWER(r.codigo) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(r.observacao) LIKE LOWER(CONCAT('%', :termo, '%'))
           """)
    Page<RequisicaoCompra> search(String termo, Pageable pageable);

    // -----------------------------------------------------------------------
    // Contagens e métricas
    // -----------------------------------------------------------------------

    /**
     * Conta quantas requisições foram abertas por um determinado solicitante.
     *
     * @param solicitanteId ID do solicitante
     * @return total de requisições abertas pelo solicitante
     */
    long countBySolicitanteId(Long solicitanteId);

    /**
     * Conta quantas requisições existem de um determinado tipo.
     *
     * @param tipo tipo da requisição
     * @return total de requisições do tipo informado
     */
    long countByTipoRequisicao(TipoRequisicaoCompra tipo);
}
