package br.com.unicos.ms_compras.service.cotacao;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusCotacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade {@link br.com.unicos.ms_compras.model.cotacao.CotacaoCompra}.
 *
 * <p>Controla a criação, atualização, listagem e gerenciamento do ciclo de vida das cotações de compra.</p>
 */
public interface CotacaoCompraService {

    /**
     * Cria uma nova cotação de compra.
     *
     * @param request dados da cotação a ser criada
     * @return cotação criada
     */
    CotacaoCompraResponse criar(CotacaoCompraRequest request);

    /**
     * Atualiza uma cotação existente.
     *
     * @param id      identificador da cotação
     * @param request dados atualizados
     * @return cotação atualizada
     */
    CotacaoCompraResponse atualizar(Long id, CotacaoCompraRequest request);

    /**
     * Busca uma cotação pelo ID.
     *
     * @param id identificador da cotação
     * @return cotação correspondente
     */
    Optional<CotacaoCompraResponse> buscarPorId(Long id);

    /**
     * Busca uma cotação pelo código identificador.
     *
     * @param codigo código da cotação
     * @return cotação correspondente
     */
    Optional<CotacaoCompraResponse> buscarPorCodigo(String codigo);

    /**
     * Lista todas as cotações, com ou sem paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página de cotações
     */
    Page<CotacaoCompraListDTO> listar(Pageable pageable);

    /**
     * Atualiza o status de uma cotação.
     *
     * @param id     identificador da cotação
     * @param status novo status
     */
    void atualizarStatus(Long id, StatusCotacao status);

    /**
     * Remove uma cotação de compra.
     *
     * @param id identificador da cotação
     */
    void deletar(Long id);
}
