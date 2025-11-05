package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.LoteSerieRequest;
import br.com.unicos.ms_estoque.dto.LoteSerieResponse;
import br.com.unicos.ms_estoque.service.LoteSerieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos lotes e séries de produtos.
 * <p>
 * Permite o registro, atualização, exclusão e consulta de lotes,
 * incluindo verificações de validade e rastreabilidade.
 */
@RestController
@RequestMapping("/v1/lotes-series")
@RequiredArgsConstructor
public class LoteSerieController {

    private final LoteSerieService loteSerieService;

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo lote de produto.
     *
     * @param request DTO com os dados do lote.
     * @return Lote criado.
     */
    @PostMapping
    public ResponseEntity<LoteSerieResponse> criar(@Valid @RequestBody LoteSerieRequest request) {
        LoteSerieResponse response = loteSerieService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de um lote existente.
     *
     * @param id      ID do lote.
     * @param request DTO com novos dados.
     * @return Lote atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoteSerieResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteSerieRequest request
    ) {
        LoteSerieResponse response = loteSerieService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um lote de produto pelo ID.
     *
     * @param id ID do lote.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        loteSerieService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista todos os lotes cadastrados.
     *
     * @return Lista completa de lotes.
     */
    @GetMapping
    public ResponseEntity<List<LoteSerieResponse>> listarTodos() {
        return ResponseEntity.ok(loteSerieService.listarTodos());
    }

    /**
     * Busca um lote pelo ID.
     *
     * @param id ID do lote.
     * @return Dados completos do lote.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoteSerieResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(loteSerieService.buscarPorId(id));
    }

    /**
     * Busca um lote pelo código.
     *
     * @param codigo Código único do lote.
     * @return Lote correspondente ao código informado.
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<LoteSerieResponse> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(loteSerieService.buscarPorCodigo(codigo));
    }

    /**
     * Lista os lotes com validade próxima (dentro de N dias).
     *
     * @param dias Número de dias para considerar como “próximo do vencimento”.
     * @return Lista de lotes próximos do vencimento.
     */
    @GetMapping("/vencimento/proximos")
    public ResponseEntity<List<LoteSerieResponse>> listarLotesProximosDoVencimento(
            @RequestParam(defaultValue = "30") int dias
    ) {
        return ResponseEntity.ok(loteSerieService.listarLotesProximosDoVencimento(dias));
    }

    /**
     * Lista todos os lotes que já estão vencidos.
     *
     * @return Lista de lotes vencidos.
     */
    @GetMapping("/vencimento/vencidos")
    public ResponseEntity<List<LoteSerieResponse>> listarLotesVencidos() {
        return ResponseEntity.ok(loteSerieService.listarLotesVencidos());
    }
}
