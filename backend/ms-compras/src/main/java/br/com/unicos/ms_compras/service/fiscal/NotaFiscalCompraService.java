package br.com.unicos.ms_compras.service.fiscal;

import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraListDTO;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraRequest;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraResponse;
import br.com.unicos.ms_compras.enums.StatusNotaFiscalCompra;
import br.com.unicos.ms_compras.enums.TipoNotaFiscal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade
 * {@link br.com.unicos.ms_compras.model.fiscal.NotaFiscalCompra}.
 *
 * <p>
 * Controla o ciclo de vida das notas fiscais de compra,
 * desde o registro até a integração com o recebimento de mercadorias.
 * </p>
 */
public interface NotaFiscalCompraService {

    /**
     * Cria uma nova nota fiscal de compra.
     *
     * @param request dados da nota fiscal
     * @return nota fiscal criada
     */
    NotaFiscalCompraResponse criar(NotaFiscalCompraRequest request);

    /**
     * Atualiza uma nota fiscal existente.
     *
     * @param id      identificador da nota fiscal
     * @param request dados atualizados
     * @return nota fiscal atualizada
     */
    NotaFiscalCompraResponse atualizar(Long id, NotaFiscalCompraRequest request);

    /**
     * Busca uma nota fiscal pelo seu identificador.
     *
     * @param id identificador da nota fiscal
     * @return nota fiscal correspondente, se existir
     */
    Optional<NotaFiscalCompraResponse> buscarPorId(Long id);

    /**
     * Busca uma nota fiscal pela chave de acesso da NFe.
     *
     * @param chaveAcesso chave de acesso da nota fiscal eletrônica
     * @return nota fiscal correspondente, se existir
     */
    Optional<NotaFiscalCompraResponse> buscarPorChaveAcesso(String chaveAcesso);

    /**
     * Lista todas as notas fiscais de compra, com suporte a paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página de notas fiscais
     */
    Page<NotaFiscalCompraListDTO> listar(Pageable pageable);

    /**
     * Lista todas as notas fiscais de um determinado fornecedor.
     *
     * @param fornecedorId identificador do fornecedor
     * @return lista de notas fiscais
     */
    List<NotaFiscalCompraListDTO> listarPorFornecedor(Long fornecedorId);

    /**
     * Atualiza o status de uma nota fiscal.
     *
     * @param id     identificador da nota fiscal
     * @param status novo status
     */
    void atualizarStatus(Long id, StatusNotaFiscalCompra status);

    /**
     * Lista notas fiscais por tipo e período.
     *
     * @param tipo   tipo da nota fiscal
     * @param inicio data inicial
     * @param fim    data final
     * @return lista de notas fiscais no período informado
     */
    List<NotaFiscalCompraListDTO> listarPorTipoEPeriodo(TipoNotaFiscal tipo, LocalDate inicio, LocalDate fim);

    /**
     * Exclui uma nota fiscal de compra.
     *
     * @param id identificador da nota fiscal
     */
    void deletar(Long id);
}
