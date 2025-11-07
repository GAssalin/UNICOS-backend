package br.com.unicos.ms_compras.controller.recebimento;

import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraListDTO;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraRequest;
import br.com.unicos.ms_compras.dto.recebimento.RecebimentoCompraResponse;
import br.com.unicos.ms_compras.enums.StatusRecebimentoCompra;
import br.com.unicos.ms_compras.enums.TipoRecebimento;
import br.com.unicos.ms_compras.service.recebimento.RecebimentoCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos recebimentos de compra.
 *
 * <p>Permite criar, atualizar, listar, buscar e excluir recebimentos,
 * além de filtrar por tipo, período e atualizar status.</p>
 */
@RestController
@RequestMapping("/v1/recebimentos-compras")
@RequiredArgsConstructor
public class RecebimentoCompraController {

    private final RecebimentoCompraService recebimentoCompraService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria um novo registro de recebimento de compra.
     *
     * @param request DTO com as informações do recebimento.
     * @return O recebimento criado.
     */
    @PostMapping
    public ResponseEntity<RecebimentoCompraResponse> criar(@Valid @RequestBody RecebimentoCompraRequest request) {
        RecebimentoCompraResponse response = recebimentoCompraService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um recebimento de compra existente.
     *
     * @param id      ID do recebimento.
     * @param request DTO com os novos dados.
     * @return O recebimento atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecebimentoCompraResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecebimentoCompraRequest request) {
        RecebimentoCompraResponse response = recebimentoCompraService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um recebimento de compra pelo seu ID.
     *
     * @param id ID do recebimento.
     * @return O recebimento correspondente, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecebimentoCompraResponse> buscarPorId(@PathVariable Long id) {
        return recebimentoCompraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os recebimentos com paginação.
     *
     * @param pageable informações de paginação.
     * @return Página com os recebimentos encontrados.
     */
    @GetMapping
    public ResponseEntity<Page<RecebimentoCompraListDTO>> listar(Pageable pageable) {
        Page<RecebimentoCompraListDTO> page = recebimentoCompraService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Lista recebimentos filtrando por tipo de recebimento.
     *
     * @param tipo Tipo de recebimento (TOTAL, PARCIAL, DEVOLUCAO, etc.).
     * @return Lista de recebimentos correspondentes ao tipo.
     */
    @GetMapping("/tipo")
    public ResponseEntity<List<RecebimentoCompraListDTO>> listarPorTipo(@RequestParam TipoRecebimento tipo) {
        List<RecebimentoCompraListDTO> lista = recebimentoCompraService.listarPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista recebimentos ocorridos dentro de um período.
     *
     * @param inicio Data inicial (formato ISO: yyyy-MM-dd).
     * @param fim    Data final (formato ISO: yyyy-MM-dd).
     * @return Lista de recebimentos dentro do período informado.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<RecebimentoCompraListDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<RecebimentoCompraListDTO> lista = recebimentoCompraService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Atualiza o status de um recebimento de compra.
     *
     * @param id     ID do recebimento.
     * @param status Novo status (PENDENTE_CONFERENCIA, EM_CONFERENCIA, FINALIZADO, etc.).
     * @return Resposta sem conteúdo.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusRecebimentoCompra status) {
        recebimentoCompraService.atualizarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove um registro de recebimento de compra.
     *
     * @param id ID do recebimento a ser removido.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recebimentoCompraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
