package br.com.unicos.ms_compras.service.cotacao;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorResponse;
import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço responsável pelas regras de negócio da entidade {@link br.com.unicos.ms_compras.model.cotacao.CotacaoFornecedor}.
 *
 * <p>Gerencia as propostas enviadas pelos fornecedores em uma cotação de compra.</p>
 */
public interface CotacaoFornecedorService {

    /**
     * Cria uma nova proposta de fornecedor dentro de uma cotação.
     *
     * @param request dados da proposta
     * @return proposta criada
     */
    CotacaoFornecedorResponse criar(CotacaoFornecedorRequest request);

    /**
     * Atualiza uma proposta de fornecedor existente.
     *
     * @param id      identificador da proposta
     * @param request dados atualizados
     * @return proposta atualizada
     */
    CotacaoFornecedorResponse atualizar(Long id, CotacaoFornecedorRequest request);

    /**
     * Busca uma proposta específica.
     *
     * @param id identificador da proposta
     * @return proposta correspondente
     */
    Optional<CotacaoFornecedorResponse> buscarPorId(Long id);

    /**
     * Lista todas as propostas vinculadas a uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @return lista de propostas
     */
    List<CotacaoFornecedorListDTO> listarPorCotacao(Long cotacaoCompraId);

    /**
     * Atualiza o status da proposta de um fornecedor.
     *
     * @param id     identificador da proposta
     * @param status novo status
     */
    void atualizarStatus(Long id, StatusFornecedorCotacao status);

    /**
     * Exclui uma proposta de fornecedor.
     *
     * @param id identificador da proposta
     */
    void deletar(Long id);
}
