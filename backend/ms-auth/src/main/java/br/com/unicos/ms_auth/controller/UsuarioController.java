package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_auth.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_auth.service.interfaces.UsuarioEmailVerificacaoService;
import br.com.unicos.ms_auth.service.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Usuários",
        description = "Endpoints de criação, atualização, consulta, listagem e remoção de usuários autenticáveis."
)
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioEmailVerificacaoService verificacaoService;

    // =============================================================
    // 🔹 Criar usuário + gerar token de verificação
    // =============================================================

    @Operation(
            summary = "Criar novo usuário",
            description = """
                    Cria um novo usuário autenticável no sistema e gera automaticamente \
                    um token de verificação de e-mail para ativação da conta.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário criado com sucesso",
                            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados para criação",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioRequest request) {

        UsuarioResponse response = usuarioService.salvar(request);

        // Gera token de verificação (o serviço de e-mail enviará o e-mail)
        verificacaoService.gerarTokenParaUsuario(response.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // 🔹 Atualizar usuário
    // =============================================================

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados de um usuário existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados enviados inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content
                    )
            }
    )
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

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados do usuário correspondente ao ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado",
                            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse response = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // =============================================================
    // 🔹 Buscar por login
    // =============================================================

    @Operation(
            summary = "Buscar usuário por login",
            description = "Consulta os dados do usuário com base no login informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado",
                            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content
                    )
            }
    )
    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioResponse> buscarPorLogin(@PathVariable String login) {
        UsuarioResponse response = usuarioService.buscarPorLogin(login);
        return ResponseEntity.ok(response);
    }

    // =============================================================
    // 🔹 Listar todos
    // =============================================================

    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna todos os usuários cadastrados, sem filtro de status.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista obtida com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // =============================================================
    // 🔹 Listar ativos
    // =============================================================

    @Operation(
            summary = "Listar usuários ativos",
            description = "Retorna somente os usuários com status ativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de usuários ativos retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)))
                    )
            }
    )
    @GetMapping("/ativos")
    public ResponseEntity<List<UsuarioResponse>> listarAtivos() {
        return ResponseEntity.ok(usuarioService.listarAtivos());
    }

    // =============================================================
    // 🔹 Listar inativos
    // =============================================================

    @Operation(
            summary = "Listar usuários inativos",
            description = "Retorna somente os usuários com status inativo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de usuários inativos retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)))
                    )
            }
    )
    @GetMapping("/inativos")
    public ResponseEntity<List<UsuarioResponse>> listarInativos() {
        return ResponseEntity.ok(usuarioService.listarInativos());
    }

    // =============================================================
    // 🔹 Desativar usuário
    // =============================================================

    @Operation(
            summary = "Desativar usuário",
            description = "Define o status do usuário para inativo, impedindo seu login.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Usuário desativado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado"
                    )
            }
    )
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    // =============================================================
    // 🔹 Excluir permanentemente
    // =============================================================

    @Operation(
            summary = "Excluir usuário permanentemente",
            description = "Remove o usuário definitivamente do sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Usuário removido com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
