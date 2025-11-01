package br.com.erp.ms_ativos.controller;

import br.com.erp.ms_ativos.dto.ManutencaoAtivoRequest;
import br.com.erp.ms_ativos.dto.ManutencaoAtivoResponse;
import br.com.erp.ms_ativos.enums.StatusManutencao;
import br.com.erp.ms_ativos.enums.TipoManutencao;
import br.com.erp.ms_ativos.service.ManutencaoAtivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de manutenções de ativos.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de manutenções.
 */
@RestController
@RequestMapping("/v1/manutencoes")
@RequiredArgsConstructor
public class ManutencaoAtivoController {

    private final ManutencaoAtivoService manutencaoAtivoService;

    @PostMapping
    public ResponseEntity<ManutencaoAtivoResponse> criar(@Valid @RequestBody ManutencaoAtivoRequest request) {
        ManutencaoAtivoResponse response = manutencaoAtivoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManutencaoAtivoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ManutencaoAtivoRequest request) {
        ManutencaoAtivoResponse response = manutencaoAtivoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManutencaoAtivoResponse> buscarPorId(@PathVariable Long id) {
        return manutencaoAtivoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ManutencaoAtivoResponse>> listarTodas() {
        List<ManutencaoAtivoResponse> lista = manutencaoAtivoService.listarTodas();
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/ativo")
    public ResponseEntity<List<ManutencaoAtivoResponse>> buscarPorAtivo(@RequestParam Long ativoId) {
        List<ManutencaoAtivoResponse> lista = manutencaoAtivoService.buscarPorAtivo(ativoId);
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/tipo")
    public ResponseEntity<List<ManutencaoAtivoResponse>> buscarPorTipo(@RequestParam TipoManutencao tipo) {
        List<ManutencaoAtivoResponse> lista = manutencaoAtivoService.buscarPorTipo(tipo);
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/status")
    public ResponseEntity<List<ManutencaoAtivoResponse>> buscarPorStatus(@RequestParam StatusManutencao status) {
        List<ManutencaoAtivoResponse> lista = manutencaoAtivoService.buscarPorStatus(status);
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar/periodo")
    public ResponseEntity<List<ManutencaoAtivoResponse>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<ManutencaoAtivoResponse> lista = manutencaoAtivoService.buscarPorPeriodo(inicio, fim);
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        manutencaoAtivoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}