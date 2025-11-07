package br.com.unicos.ms_compras.controller.fiscal;

import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraListDTO;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraRequest;
import br.com.unicos.ms_compras.dto.fiscal.NotaFiscalCompraResponse;
import br.com.unicos.ms_compras.enums.StatusNotaFiscalCompra;
import br.com.unicos.ms_compras.enums.TipoNotaFiscal;
import br.com.unicos.ms_compras.service.fiscal.NotaFiscalCompraService;
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
 * Controlador REST responsável pelo gerenciamento das notas fiscais de compra.
 *
 * <p>Permite criar, atualizar, consultar, listar e excluir notas fiscais,
 * além de alterar seu status e realizar consultas por período e tipo.</p>
 */
@RestController
@RequestMapping("/v1/notas-fiscais-compras")
@RequiredArgsConstructor
public class NotaFiscalCompraController {

    private final NotaFiscalCompraService notaFiscalCompraService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria uma nova nota fiscal de compra.
     *
     * @param request DTO contendo as informações da nota fiscal.
     * @return A nota fiscal criada.
     */
    @PostMapping
    public ResponseEntity<NotaFiscalCompraResponse> criar(@Valid @RequestBody NotaFiscalCompraRequest request) {
        NotaFiscalCompraResponse response = notaFiscalCompraService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma nota fiscal existente.
     *
     * @param id      ID da nota fiscal.
     * @param request DTO com os novos dados.
     * @return A nota fiscal atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotaFiscalCompraResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody NotaFiscalCompraRequest request) {
        NotaFiscalCompraResponse response = notaFiscalCompraService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma nota fiscal pelo seu ID.
     *
     * @param id ID da nota fiscal.
     * @return A nota correspondente, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotaFiscalCompraResponse> buscarPorId(@PathVariable Long id) {
        return notaFiscalCompraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca uma nota fiscal pela chave de acesso.
     *
     * @param chaveAcesso Chave de acesso da nota fiscal.
     * @return A nota correspondente, caso exista.
     */
    @GetMapping("/chave/{chaveAcesso}")
    public ResponseEntity<NotaFiscalCompraResponse> buscarPorChaveAcesso(@PathVariable String chaveAcesso) {
        return notaFiscalCompraService.buscarPorChaveAcesso(chaveAcesso)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as notas fiscais com paginação.
     *
     * @param pageable informações de paginação.
     * @return Página com notas fiscais de compra.
     */
    @GetMapping
    public ResponseEntity<Page<NotaFiscalCompraListDTO>> listar(Pageable pageable) {
        Page<NotaFiscalCompraListDTO> page = notaFiscalCompraService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Lista todas as notas fiscais emitidas por um determinado fornecedor.
     *
     * @param fornecedorId ID do fornecedor.
     * @return Lista de notas fiscais do fornecedor.
     */
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<NotaFiscalCompraListDTO>> listarPorFornecedor(@PathVariable Long fornecedorId) {
        List<NotaFiscalCompraListDTO> lista = notaFiscalCompraService.listarPorFornecedor(fornecedorId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Atualiza o status de uma nota fiscal.
     *
     * @param id     ID da nota fiscal.
     * @param status Novo status da nota.
     * @return Resposta sem conteúdo.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusNotaFiscalCompra status) {
        notaFiscalCompraService.atualizarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista notas fiscais de um determinado tipo e dentro de um período.
     *
     * @param tipo   Tipo da nota fiscal.
     * @param inicio Data inicial do período.
     * @param fim    Data final do período.
     * @return Lista de notas dentro dos filtros especificados.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<NotaFiscalCompraListDTO>> listarPorTipoEPeriodo(
            @RequestParam TipoNotaFiscal tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<NotaFiscalCompraListDTO> lista = notaFiscalCompraService.listarPorTipoEPeriodo(tipo, inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Exclui uma nota fiscal de compra.
     *
     * @param id ID da nota a ser removida.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        notaFiscalCompraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
