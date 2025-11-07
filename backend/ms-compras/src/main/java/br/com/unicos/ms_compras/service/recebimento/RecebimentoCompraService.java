package br.com.unicos.ms_compras.service.recebimento;

import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraListDTO;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraRequest;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusRecebimentoCompra;
import br.com.unicos.ms_compras.enums.TipoRecebimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.recebimento.RecebimentoCompra}.
 *
 * <p>
 * Gerencia o ciclo de vida dos recebimentos de mercadorias, incluindo
 * integração com pedidos de compra e notas fiscais associadas.
 * </p>
 */
public interface RecebimentoCompraService {

    /**
     * Cria um novo registro de recebimento.
     *
     * @param request dados do recebimento
     * @return recebimento criado
     */
    RecebimentoCompraResponse criar(RecebimentoCompraRequest request);

    /**
     * Atualiza um recebimento existente.
     *
     * @param id      identificador do recebimento
     * @param request dados atualizados
     * @return recebimento atualizado
     */
    RecebimentoCompraResponse atualizar(Long id, RecebimentoCompraRequest request);

    /**
     * Busca um recebimento pelo identificador.
     *
     * @param id identificador do recebimento
     * @return recebimento correspondente, se existir
     */
    Optional<RecebimentoCompraResponse> buscarPorId(Long id);

    /**
     * Lista todos os recebimentos com suporte a paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página de recebimentos
     */
    Page<RecebimentoCompraListDTO> listar(Pageable pageable);

    /**
     * Lista recebimentos por tipo.
     *
     * @param tipo tipo de recebimento
     * @return lista de recebimentos do tipo informado
     */
    List<RecebimentoCompraListDTO> listarPorTipo(TipoRecebimento tipo);

    /**
     * Lista recebimentos por período.
     *
     * @param inicio data inicial
     * @param fim    data final
     * @return lista de recebimentos dentro do intervalo informado
     */
    List<RecebimentoCompraListDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Atualiza o status de um recebimento.
     *
     * @param id     identificador do recebimento
     * @param status novo status
     */
    void atualizarStatus(Long id, StatusRecebimentoCompra status);

    /**
     * Exclui um registro de recebimento.
     *
     * @param id identificador do recebimento
     */
    void deletar(Long id);
}
