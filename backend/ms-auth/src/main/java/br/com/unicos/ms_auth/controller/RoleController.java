package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.RoleRequest;
import br.com.unicos.ms_auth.dto.RoleResponse;
import br.com.unicos.ms_auth.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos papéis (roles).
 * <p>
 * Fornece endpoints para operações de CRUD e consultas específicas.
 */
@RestController
@RequestMapping("/v1/roles")
public class RoleController {

    private final RoleService roleService;

    /**
     * Injeta a dependência do serviço de roles.
     *
     * @param roleService Serviço responsável pelas regras de negócio de Role.
     */
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo papel.
     *
     * @param request Dados do papel a ser criado.
     * @return Papel criado.
     */
    @PostMapping
    public ResponseEntity<RoleResponse> criarRole(@Valid @RequestBody RoleRequest request) {
        // Persiste a role e vincula permissões, se fornecidas
        RoleResponse response = roleService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um papel existente.
     *
     * @param id      Identificador do papel.
     * @param request Dados atualizados do papel.
     * @return Papel atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> atualizarRole(@PathVariable Long id,
                                                      @Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um papel pelo ID.
     *
     * @param id Identificador do papel.
     * @return Papel encontrado, se existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> buscarPorId(@PathVariable Long id) {
        // Retorna 404 se não encontrado
        return roleService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os papéis.
     *
     * @return Lista completa de papéis.
     */
    @GetMapping
    public ResponseEntity<List<RoleResponse>> listarTodos() {
        List<RoleResponse> roles = roleService.listarTodos();
        return ResponseEntity.ok(roles);
    }

    /**
     * Exclui um papel pelo ID.
     *
     * @param id Identificador do papel.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRole(@PathVariable Long id) {
        roleService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Verifica se já existe uma role com o nome informado.
     *
     * @param nome Nome do papel.
     * @return true se já existir, false caso contrário.
     */
    @GetMapping("/verificar-nome")
    public ResponseEntity<Boolean> verificarNome(@RequestParam String nome) {
        boolean existe = roleService.existePorNome(nome);
        return ResponseEntity.ok(existe);
    }
}
