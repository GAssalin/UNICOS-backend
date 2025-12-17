package br.com.unicos.ms_auth.controller;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_auth.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_auth.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_auth.service.UsuarioEmailVerificacaoService;
import br.com.unicos.ms_auth.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento dos usuários autenticáveis do sistema.
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta, desativação
 * e exclusão definitiva de usuários.
 */
@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
@Tag(
        name = "Usuários",
        description = "Endpoints de criação, atualização, consulta, listagem e remoção de usuários autenticáveis."
)
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioEmailVerificacaoService verificacaoService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        UsuarioResponse response = usuarioService.salvar(request, empresaId);

        verificacaoService.gerarTokenParaUsuario(response.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request, TenantContext.getEmpresaId()));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id, TenantContext.getEmpresaId()));
    }

    // =============================================================
    // GET BY LOGIN
    // =============================================================

    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioResponse> buscarPorLogin(@PathVariable String login) {
        return ResponseEntity.ok(usuarioService.buscarPorLogin(login, TenantContext.getEmpresaId()));
    }

    // =============================================================
    // LISTAGENS ADMINISTRATIVAS (PAGINADAS)
    // =============================================================

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(TenantContext.getEmpresaId(), pageable));
    }

    @GetMapping("/ativos")
    public ResponseEntity<Page<UsuarioResponse>> listarAtivos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarAtivos(TenantContext.getEmpresaId(), pageable));
    }

    @GetMapping("/inativos")
    public ResponseEntity<Page<UsuarioResponse>> listarInativos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarInativos(TenantContext.getEmpresaId(), pageable)
        );
    }

    // =============================================================
    // STATUS / DELETE
    // =============================================================

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        usuarioService.desativar(id, TenantContext.getEmpresaId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id, TenantContext.getEmpresaId());
        return ResponseEntity.noContent().build();
    }
}
