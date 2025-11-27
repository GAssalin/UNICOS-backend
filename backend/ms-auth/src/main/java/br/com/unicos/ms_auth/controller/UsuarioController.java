package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_auth.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_auth.service.interfaces.UsuarioEmailVerificacaoService;
import br.com.unicos.ms_auth.service.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos usuários autenticáveis do sistema.
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta, desativação
 * e exclusão definitiva de usuários.
 */
@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioEmailVerificacaoService verificacaoService;

    // =============================================================
    // 🔹 Criar usuário + gerar token de verificação
    // =============================================================
    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioRequest request) {

        UsuarioResponse response = usuarioService.salvar(request);

        // TODO: Gera o token de verificação (e-mail service enviará o e-mail)
        verificacaoService.gerarTokenParaUsuario(response.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // 🔹 Atualizar usuário
    // =============================================================
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request
    ) {
        UsuarioResponse response = usuarioService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // =============================================================
    // 🔹 Buscar por ID
    // =============================================================
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse response = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // =============================================================
    // 🔹 Buscar por login
    // =============================================================
    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioResponse> buscarPorLogin(@PathVariable String login) {
        UsuarioResponse response = usuarioService.buscarPorLogin(login);
        return ResponseEntity.ok(response);
    }

    // =============================================================
    // 🔹 Listar todos
    // =============================================================
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // =============================================================
    // 🔹 Listar ativos
    // =============================================================
    @GetMapping("/ativos")
    public ResponseEntity<List<UsuarioResponse>> listarAtivos() {
        return ResponseEntity.ok(usuarioService.listarAtivos());
    }

    // =============================================================
    // 🔹 Listar inativos
    // =============================================================
    @GetMapping("/inativos")
    public ResponseEntity<List<UsuarioResponse>> listarInativos() {
        return ResponseEntity.ok(usuarioService.listarInativos());
    }

    // =============================================================
    // 🔹 Desativar usuário
    // =============================================================
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    // =============================================================
    // 🔹 Excluir permanentemente
    // =============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
