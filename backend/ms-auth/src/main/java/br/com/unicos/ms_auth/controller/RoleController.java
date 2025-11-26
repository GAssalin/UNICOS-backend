package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.role.RoleRequest;
import br.com.unicos.ms_auth.dto.role.RoleResponse;
import br.com.unicos.ms_auth.service.interfaces.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento de papéis (Roles)
 * dentro do módulo de autenticação.
 * <p>
 * Permite a criação, atualização, listagem e remoção de papéis,
 * além da consulta individual por ID.
 */
@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // ============================================================
    // 🔹 Criar Role
    // ============================================================
    @PostMapping
    public ResponseEntity<RoleResponse> criar(@Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Atualizar Role
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequest request
    ) {
        RoleResponse response = roleService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Buscar Role por ID
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> buscarPorId(@PathVariable Long id) {
        RoleResponse response = roleService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Listar todos os Roles
    // ============================================================
    @GetMapping
    public ResponseEntity<List<RoleResponse>> listarTodos() {
        List<RoleResponse> lista = roleService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // ============================================================
    // 🔹 Deletar Role
    // ============================================================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        roleService.deletar(id);
    }
}
