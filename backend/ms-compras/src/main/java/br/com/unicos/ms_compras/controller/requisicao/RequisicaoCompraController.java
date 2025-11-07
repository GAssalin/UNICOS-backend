package br.com.unicos.ms_compras.controller.requisicao;

import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraListDTO;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraRequest;
import br.com.unicos.ms_compras.dto.requisicao.RequisicaoCompraResponse;
import br.com.unicos.ms_compras.enums.TipoRequisicaoCompra;
import br.com.unicos.ms_compras.service.requisicao.RequisicaoCompraService;
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
 * Controlador REST responsável pelo gerenciamento das requisições internas de compra.
 *
 * <p>Permite criar, atualizar, listar, buscar e excluir requisições,
 * além de filtrar por tipo, solicitante e período.</p>
 */
@RestController
@RequestMapping("/v1/requisicoes-compras")
@RequiredArgsConstructor
public class RequisicaoCompraController {

    private final RequisicaoCompraService requisicaoCompraService;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================

    /**
     * Cria uma nova requisição de compra.
     *
     * @param request DTO contendo as informações da requisição.
     * @return A requisição criada.
     */
    @PostMapping
    public ResponseEntity<RequisicaoCompraResponse> criar(@Valid @RequestBody RequisicaoCompraRequest request) {
        RequisicaoCompraResponse response = requisicaoCompraService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma requisição de compra existente.
     *
     * @param id      ID da requisição.
     * @param request DTO com os novos dados.
     * @return A requisição atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequisicaoCompraResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RequisicaoCompraRequest request) {
        RequisicaoCompraResponse response = requisicaoCompraService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma requisição de compra pelo seu ID.
     *
     * @param id ID da requisição.
     * @return A requisição correspondente, caso exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequisicaoCompraResponse> buscarPorId(@PathVariable Long id) {
        return requisicaoCompraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as requisições com paginação.
     *
     * @param pageable informações de paginação.
     * @return Página de requisições encontradas.
     */
    @GetMapping
    public ResponseEntity<Page<RequisicaoCompraListDTO>> listar(Pageable pageable) {
        Page<RequisicaoCompraListDTO> page = requisicaoCompraService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Lista as requisições de compra filtradas por tipo.
     *
     * @param tipo Tipo da requisição (ex: MATERIA_PRIMA, SERVICO, EQUIPAMENTO).
     * @return Lista de requisições do tipo informado.
     */
    @GetMapping("/tipo")
    public ResponseEntity<List<RequisicaoCompraListDTO>> listarPorTipo(@RequestParam TipoRequisicaoCompra tipo) {
        List<RequisicaoCompraListDTO> lista = requisicaoCompraService.listarPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista as requisições abertas dentro de um determinado período.
     *
     * @param inicio Data inicial (formato ISO: yyyy-MM-dd).
     * @param fim    Data final (formato ISO: yyyy-MM-dd).
     * @return Lista de requisições dentro do período.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<RequisicaoCompraListDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<RequisicaoCompraListDTO> lista = requisicaoCompraService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista as requisições de compra realizadas por um solicitante específico.
     *
     * @param solicitanteId ID do solicitante.
     * @return Lista de requisições do solicitante informado.
     */
    @GetMapping("/solicitante/{solicitanteId}")
    public ResponseEntity<List<RequisicaoCompraListDTO>> listarPorSolicitante(@PathVariable Long solicitanteId) {
        List<RequisicaoCompraListDTO> lista = requisicaoCompraService.listarPorSolicitante(solicitanteId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Remove uma requisição de compra.
     *
     * @param id ID da requisição a ser removida.
     * @return Resposta sem conteúdo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        requisicaoCompraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
