package br.com.unicos.ms_compras.controller.cotacao;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoFornecedorResponse;
import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;
import br.com.unicos.ms_compras.service.cotacao.CotacaoFornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das propostas de fornecedores
 * dentro de uma cotação de compra.
 *
 * <p>Permite cadastrar, atualizar, listar e excluir propostas, bem como alterar
 * o status de avaliação de cada fornecedor participante.</p>
 */
@RestController
@RequestMapping("/v1/cotacoes-fornecedores")
@RequiredArgsConstructor
public class CotacaoFornecedorController {

    private final CotacaoFornecedorService cotacaoFornecedorService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria uma nova proposta de fornecedor vinculada a uma cotação de compra.
     *
     * @param request DTO contendo os dados do fornecedor e da cotação.
     * @return A proposta criada.
     */
    @PostMapping
    public ResponseEntity<CotacaoFornecedorResponse> criar(@Valid @RequestBody CotacaoFornecedorRequest request) {
        CotacaoFornecedorResponse response = cotacaoFornecedorService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma proposta de fornecedor existente.
     *
     * @param id      ID da proposta de fornecedor.
     * @param request DTO contendo os novos dados.
     * @return A proposta atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CotacaoFornecedorResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CotacaoFornecedorRequest request) {
        CotacaoFornecedorResponse response = cotacaoFornecedorService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma proposta de fornecedor pelo seu ID.
     *
     * @param id ID da proposta.
     * @return A proposta correspondente, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CotacaoFornecedorResponse> buscarPorId(@PathVariable Long id) {
        return cotacaoFornecedorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as propostas de fornecedores vinculadas a uma cotação específica.
     *
     * @param cotacaoCompraId ID da cotação de compra.
     * @return Lista de propostas dos fornecedores.
     */
    @GetMapping("/cotacao/{cotacaoCompraId}")
    public ResponseEntity<List<CotacaoFornecedorListDTO>> listarPorCotacao(@PathVariable Long cotacaoCompraId) {
        List<CotacaoFornecedorListDTO> lista = cotacaoFornecedorService.listarPorCotacao(cotacaoCompraId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Atualiza o status de uma proposta de fornecedor.
     *
     * @param id     ID da proposta de fornecedor.
     * @param status Novo status a ser definido.
     * @return Resposta sem conteúdo.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusFornecedorCotacao status) {
        cotacaoFornecedorService.atualizarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove uma proposta de fornecedor.
     *
     * @param id ID da proposta a ser removida.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cotacaoFornecedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
