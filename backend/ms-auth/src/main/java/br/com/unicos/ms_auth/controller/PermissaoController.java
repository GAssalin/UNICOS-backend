package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_auth.service.PermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento das permissões do sistema.
 * <p>
 * Permite operações de criação, atualização, consulta e remoção
 * de permissões granulares utilizadas na composição de papéis (roles).
 */
@RestController
@RequestMapping("/v1/permissoes")
@RequiredArgsConstructor
@Tag(
        name = "Permissões",
        description = "Endpoints para criação, edição, busca e exclusão de permissões do sistema."
)
public class PermissaoController {

    private final PermissaoService service;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(summary = "Criar nova permissão")
    @PreAuthorize("hasPermission(null, 'PERMISSAO_CRIAR')")
    @PostMapping
    public ResponseEntity<PermissaoResponse> criar(@Valid @RequestBody PermissaoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(request));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(summary = "Atualizar permissão")
    @PreAuthorize("hasPermission(null, 'PERMISSAO_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<PermissaoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PermissaoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Operation(summary = "Buscar permissão por ID")
    @PreAuthorize("hasPermission(null, 'PERMISSAO_LISTAR')")
    @GetMapping("/{id}")
    public ResponseEntity<PermissaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // ============================================================
    // LISTAGEM ADMINISTRATIVA (PAGINADA)
    // ============================================================

    @Operation(summary = "Listar permissões (paginado)")
    @PreAuthorize("hasPermission(null, 'PERMISSAO_LISTAR')")
    @GetMapping
    public ResponseEntity<Page<PermissaoResponse>> listar(@RequestParam(required = false) String nome, Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, pageable)
        );
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(summary = "Deletar permissão")
    @PreAuthorize("hasPermission(null, 'PERMISSAO_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
