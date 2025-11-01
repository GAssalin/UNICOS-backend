package br.com.erp.ms_ativos.controller;

import br.com.erp.ms_ativos.dto.TransferenciaAtivoRequest;
import br.com.erp.ms_ativos.dto.TransferenciaAtivoResponse;
import br.com.erp.ms_ativos.service.TransferenciaAtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de transferências de ativos.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de transferências.
 */
@RestController
@RequestMapping("/v1/transferencias")
@RequiredArgsConstructor
public class TransferenciaAtivoController {

    private final TransferenciaAtivoService transferenciaAtivoService;

    @PostMapping
    public ResponseEntity<TransferenciaAtivoResponse> criar(@Valid @RequestBody TransferenciaAtivoRequest request) {
        TransferenciaAtivoResponse response = transferenciaAtivoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaAtivoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TransferenciaAtivoRequest request) {
        TransferenciaAtivoResponse response = transferenciaAtivoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaAtivoResponse> buscarPorId(@PathVariable Long id) {
        return transferenciaAtivoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaAtivoResponse>> listarTodas() {
        List<TransferenciaAtivoResponse> lista = transferenciaAtivoService.listarTodas();
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/ativo")
    public ResponseEntity<List<TransferenciaAtivoResponse>> buscarPorAtivo(@RequestParam Long ativoId) {
        List<TransferenciaAtivoResponse> lista = transferenciaAtivoService.buscarPorAtivo(ativoId);
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/origem")
    public ResponseEntity<List<TransferenciaAtivoResponse>> buscarPorOrigem(@RequestParam Long origemId) {
        List<TransferenciaAtivoResponse> lista = transferenciaAtivoService.buscarPorOrigem(origemId);
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/destino")
    public ResponseEntity<List<TransferenciaAtivoResponse>> buscarPorDestino(@RequestParam Long destinoId) {
        List<TransferenciaAtivoResponse> lista = transferenciaAtivoService.buscarPorDestino(destinoId);
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/periodo")
    public ResponseEntity<List<TransferenciaAtivoResponse>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<TransferenciaAtivoResponse> lista = transferenciaAtivoService.buscarPorPeriodo(inicio, fim);
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transferenciaAtivoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}