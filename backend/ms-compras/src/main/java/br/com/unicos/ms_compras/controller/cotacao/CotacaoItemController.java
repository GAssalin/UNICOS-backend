package br.com.unicos.ms_compras.controller.cotacao;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoItemResponse;
import br.com.unicos.ms_compras.service.cotacao.CotacaoItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos itens de cotação.
 *
 * <p>Permite criar, atualizar, listar e remover itens vinculados às propostas
 * enviadas por fornecedores em uma cotação de compra.</p>
 */
@RestController
@RequestMapping("/v1/cotacoes-itens")
@RequiredArgsConstructor
public class CotacaoItemController {

    private final CotacaoItemService cotacaoItemService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria um novo item vinculado a uma proposta de fornecedor.
     *
     * @param request DTO contendo as informações do item.
     * @return O item criado.
     */
    @PostMapping
    public ResponseEntity<CotacaoItemResponse> criar(@Valid @RequestBody CotacaoItemRequest request) {
        CotacaoItemResponse response = cotacaoItemService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um item de cotação existente.
     *
     * @param id      ID do item de cotação.
     * @param request DTO contendo os novos dados.
     * @return O item atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CotacaoItemResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CotacaoItemRequest request) {
        CotacaoItemResponse response = cotacaoItemService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um item de cotação pelo seu ID.
     *
     * @param id ID do item.
     * @return O item encontrado, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CotacaoItemResponse> buscarPorId(@PathVariable Long id) {
        return cotacaoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os itens vinculados a um fornecedor específico.
     *
     * @param cotacaoFornecedorId ID do fornecedor participante da cotação.
     * @return Lista de itens associados.
     */
    @GetMapping("/fornecedor/{cotacaoFornecedorId}")
    public ResponseEntity<List<CotacaoItemListDTO>> listarPorFornecedor(@PathVariable Long cotacaoFornecedorId) {
        List<CotacaoItemListDTO> lista = cotacaoItemService.listarPorFornecedor(cotacaoFornecedorId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Remove um item de cotação.
     *
     * @param id ID do item a ser removido.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cotacaoItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
