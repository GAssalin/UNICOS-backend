package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.HistoricoPrecoResponse;
import br.com.unicos.ms_produtos.service.HistoricoPrecoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos históricos de preço dos produtos.
 *
 * Fornece endpoints para criação, listagem e consulta dos registros de alterações de preço.
 */
@RestController
@RequestMapping("/v1/historicos-preco")
public class HistoricoPrecoController {

    private final HistoricoPrecoService historicoPrecoService;

    public HistoricoPrecoController(HistoricoPrecoService historicoPrecoService) {
        this.historicoPrecoService = historicoPrecoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Registra uma nova alteração de preço para um produto.
     *
     * @param request Dados do histórico de preço.
     * @return HistoricoPrecoResponse criado.
     */
    @PostMapping
    public ResponseEntity<HistoricoPrecoResponse> criarHistorico(@Valid @RequestBody HistoricoPrecoRequest request) {
        HistoricoPrecoResponse response = historicoPrecoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca um histórico de preço pelo ID.
     *
     * @param id Identificador do histórico.
     * @return HistoricoPrecoResponse, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoPrecoResponse> buscarPorId(@PathVariable Long id) {
        return historicoPrecoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os registros de histórico de preço.
     *
     * @return Lista completa de históricos.
     */
    @GetMapping
    public ResponseEntity<List<HistoricoPrecoResponse>> listarTodos() {
        List<HistoricoPrecoResponse> historicos = historicoPrecoService.listarTodos();
        return ResponseEntity.ok(historicos);
    }

    /**
     * Lista todos os históricos de preço de um produto específico.
     *
     * @param produtoId ID do produto.
     * @return Lista de históricos desse produto.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<HistoricoPrecoResponse>> listarPorProduto(@PathVariable Long produtoId) {
        List<HistoricoPrecoResponse> historicos = historicoPrecoService.listarPorProduto(produtoId);
        return ResponseEntity.ok(historicos);
    }

    /**
     * Lista os 10 registros mais recentes de alteração de preço de um produto.
     *
     * @param produtoId ID do produto.
     * @return Lista resumida com os últimos registros.
     */
    @GetMapping("/produto/{produtoId}/ultimos")
    public ResponseEntity<List<HistoricoPrecoListDTO>> listarUltimosPorProduto(@PathVariable Long produtoId) {
        List<HistoricoPrecoListDTO> ultimos = historicoPrecoService.listarUltimosPorProduto(produtoId);
        return ResponseEntity.ok(ultimos);
    }

    /**
     * Exclui um histórico de preço pelo ID.
     *
     * @param id Identificador do histórico.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarHistorico(@PathVariable Long id) {
        historicoPrecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}