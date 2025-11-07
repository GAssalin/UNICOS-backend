package br.com.unicos.ms_compras.service.requisicao;

import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraListDTO;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraRequest;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraResponse;
import br.com.unicos.ms_compras.enums.TipoRequisicaoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.requisicao.RequisicaoCompra}.
 *
 * <p>
 * Controla o ciclo de vida das requisições de compra internas,
 * desde a criação por departamentos até a conversão em cotações.
 * </p>
 */
public interface RequisicaoCompraService {

    /**
     * Cria uma nova requisição de compra.
     *
     * @param request dados da requisição
     * @return requisição criada
     */
    RequisicaoCompraResponse criar(RequisicaoCompraRequest request);

    /**
     * Atualiza uma requisição existente.
     *
     * @param id      identificador da requisição
     * @param request dados atualizados
     * @return requisição atualizada
     */
    RequisicaoCompraResponse atualizar(Long id, RequisicaoCompraRequest request);

    /**
     * Busca uma requisição de compra pelo identificador.
     *
     * @param id identificador da requisição
     * @return requisição correspondente, se existir
     */
    Optional<RequisicaoCompraResponse> buscarPorId(Long id);

    /**
     * Lista todas as requisições com suporte a paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página de requisições
     */
    Page<RequisicaoCompraListDTO> listar(Pageable pageable);

    /**
     * Lista requisições por tipo.
     *
     * @param tipo tipo de requisição (interna, reposição, urgência, etc.)
     * @return lista de requisições
     */
    List<RequisicaoCompraListDTO> listarPorTipo(TipoRequisicaoCompra tipo);

    /**
     * Lista requisições por período.
     *
     * @param inicio data inicial
     * @param fim    data final
     * @return lista de requisições dentro do período informado
     */
    List<RequisicaoCompraListDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Lista requisições feitas por um determinado solicitante.
     *
     * @param solicitanteId identificador do solicitante
     * @return lista de requisições do solicitante
     */
    List<RequisicaoCompraListDTO> listarPorSolicitante(Long solicitanteId);

    /**
     * Exclui uma requisição de compra.
     *
     * @param id identificador da requisição
     */
    void deletar(Long id);
}
