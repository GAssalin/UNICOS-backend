package br.com.unicos.ms_compras.controller.cotacao;

import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraListDTO;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraRequest;
import br.com.unicos.ms_compras.dto.cotacao.CotacaoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusCotacao;
import br.com.unicos.ms_compras.service.cotacao.CotacaoCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento das cotações de compra.
 *
 * <p>Permite a criação, atualização, listagem, busca e exclusão de cotações,
 * bem como a alteração do status de cada uma.</p>
 */
@RestController
@RequestMapping("/v1/cotacoes-compras")
@RequiredArgsConstructor
public class CotacaoCompraController {

    private final CotacaoCompraService cotacaoCompraService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria uma nova cotação de compra.
     *
     * @param request DTO contendo os dados da cotação.
     * @return A cotação criada.
     */
    @PostMapping
    public ResponseEntity<CotacaoCompraResponse> criar(@Valid @RequestBody CotacaoCompraRequest request) {
        CotacaoCompraResponse response = cotacaoCompraService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma cotação existente.
     *
     * @param id      ID da cotação.
     * @param request DTO com os novos dados.
     * @return A cotação atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CotacaoCompraResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CotacaoCompraRequest request) {
        CotacaoCompraResponse response = cotacaoCompraService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma cotação pelo seu ID.
     *
     * @param id ID da cotação.
     * @return A cotação correspondente, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CotacaoCompraResponse> buscarPorId(@PathVariable Long id) {
        return cotacaoCompraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca uma cotação pelo código único.
     *
     * @param codigo Código da cotação.
     * @return A cotação correspondente, caso exista.
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CotacaoCompraResponse> buscarPorCodigo(@PathVariable String codigo) {
        return cotacaoCompraService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as cotações paginadas.
     *
     * @param pageable informações de paginação.
     * @return Página com as cotações encontradas.
     */
    @GetMapping
    public ResponseEntity<Page<CotacaoCompraListDTO>> listar(Pageable pageable) {
        Page<CotacaoCompraListDTO> page = cotacaoCompraService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Atualiza o status de uma cotação.
     *
     * @param id     ID da cotação.
     * @param status Novo status a ser definido.
     * @return Resposta sem conteúdo.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusCotacao status) {
        cotacaoCompraService.atualizarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove uma cotação de compra existente.
     *
     * @param id ID da cotação a ser removida.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cotacaoCompraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
