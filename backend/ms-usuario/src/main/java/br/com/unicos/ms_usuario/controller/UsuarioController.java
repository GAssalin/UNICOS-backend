package br.com.unicos.ms_usuario.controller;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_usuario.service.UsuarioEmailVerificacaoService;
import br.com.unicos.ms_usuario.service.UsuarioService;
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

    @PreAuthorize("hasPermission(null, 'USUARIO_CRIAR')")
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

    @PreAuthorize("hasPermission(null, 'USUARIO_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request, TenantContext.getEmpresaId()));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @PreAuthorize("hasPermission(null, 'USUARIO_LISTAR')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // =============================================================
    // GET BY LOGIN
    // =============================================================

    @PreAuthorize("hasPermission(null, 'USUARIO_LISTAR')")
    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioResponse> buscarPorLogin(@PathVariable String login) {
        return ResponseEntity.ok(usuarioService.buscarPorLogin(login, TenantContext.getEmpresaId()));
    }

    // =============================================================
    // LISTAGENS ADMINISTRATIVAS (PAGINADAS)
    // =============================================================

    @PreAuthorize("hasPermission(null, 'USUARIO_LISTAR')")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(TenantContext.getEmpresaId(), pageable));
    }

    @PreAuthorize("hasPermission(null, 'USUARIO_LISTAR')")
    @GetMapping("/ativos")
    public ResponseEntity<Page<UsuarioResponse>> listarAtivos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarAtivos(TenantContext.getEmpresaId(), pageable));
    }

    @PreAuthorize("hasPermission(null, 'USUARIO_LISTAR')")
    @GetMapping("/inativos")
    public ResponseEntity<Page<UsuarioResponse>> listarInativos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarInativos(TenantContext.getEmpresaId(), pageable)
        );
    }

    // =============================================================
    // STATUS / DELETE
    // =============================================================

    @PreAuthorize("hasPermission(null, 'USUARIO_EXCLUIR')")
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasPermission(null, 'USUARIO_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
