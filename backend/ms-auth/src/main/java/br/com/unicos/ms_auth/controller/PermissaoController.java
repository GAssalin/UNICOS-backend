package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_auth.service.interfaces.PermissaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das permissões do sistema.
 * <p>
 * Permite operações de criação, atualização, consulta e remoção
 * de permissões granulares utilizadas na composição de papéis (roles).
 */
@RestController
@RequestMapping("/v1/permissoes")
@RequiredArgsConstructor
public class PermissaoController {

    private final PermissaoService permissaoService;

    // ============================================================
    // 🔹 Criar nova permissão
    // ============================================================
    @PostMapping
    public ResponseEntity<PermissaoResponse> criar(@Valid @RequestBody PermissaoRequest request) {
        PermissaoResponse response = permissaoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Atualizar permissão existente
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<PermissaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PermissaoRequest request
    ) {
        PermissaoResponse response = permissaoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Buscar permissão por ID
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<PermissaoResponse> buscarPorId(@PathVariable Long id) {
        PermissaoResponse response = permissaoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Listar todas as permissões
    // ============================================================
    @GetMapping
    public ResponseEntity<List<PermissaoResponse>> listarTodas() {
        return ResponseEntity.ok(permissaoService.listarTodas());
    }

    // ============================================================
    // 🔹 Deletar permissão
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        permissaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
