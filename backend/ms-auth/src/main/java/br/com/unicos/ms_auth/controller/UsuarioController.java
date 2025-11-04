package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.UsuarioRequest;
import br.com.unicos.ms_auth.dto.UsuarioResponse;
import br.com.unicos.ms_auth.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos usuários.
 * <p>
 * Fornece endpoints para operações de CRUD e consultas específicas.
 */
@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Injeta a dependência do serviço de usuários.
     *
     * @param usuarioService Serviço responsável pelas regras de negócio de Usuário.
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo usuário.
     *
     * @param request Dados do usuário a ser criado.
     * @return Usuário criado com ID gerado.
     */
    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody UsuarioRequest request) {
        // Chama a camada de serviço para persistir o novo usuário
        UsuarioResponse response = usuarioService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um usuário existente.
     *
     * @param id      Identificador do usuário.
     * @param request Dados atualizados.
     * @return Usuário atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id,
                                                            @Valid @RequestBody UsuarioRequest request) {
        // Atualiza dados do usuário, incluindo roles, email e status
        UsuarioResponse response = usuarioService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id Identificador do usuário.
     * @return Usuário encontrado, se existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        // Retorna 404 caso o usuário não exista
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os usuários.
     *
     * @return Lista completa de usuários.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        // Ideal para telas de administração
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Lista usuários ativos.
     *
     * @return Lista de usuários com campo "ativo" = true.
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<UsuarioResponse>> listarAtivos() {
        List<UsuarioResponse> usuarios = usuarioService.listarAtivos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Lista usuários inativos.
     *
     * @return Lista de usuários com campo "ativo" = false.
     */
    @GetMapping("/inativos")
    public ResponseEntity<List<UsuarioResponse>> listarInativos() {
        List<UsuarioResponse> usuarios = usuarioService.listarInativos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Desativa um usuário (soft disable).
     *
     * @param id ID do usuário a ser desativado.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
        // Marca o usuário como inativo, sem removê-lo do banco
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exclui um usuário permanentemente.
     *
     * @param id ID do usuário.
     * @return Resposta 204 (sem conteúdo) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        // Remove definitivamente o registro do usuário
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
