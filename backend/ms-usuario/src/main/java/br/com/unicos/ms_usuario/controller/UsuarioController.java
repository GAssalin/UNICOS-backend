package br.com.unicos.ms_usuario.controller;

import br.com.unicos.ms_usuario.dto.usuario.UsuarioRequest;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioResponse;
import br.com.unicos.ms_usuario.model.Usuario;
import br.com.unicos.ms_usuario.service.UsuarioEmailVerificacaoService;
import br.com.unicos.ms_usuario.service.UsuarioService;
import br.com.unicos.ms_usuario.service.UtilsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento dos usuários autenticáveis do sistema.
 */
@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Usuários",
        description = "Endpoints de criação, atualização, consulta, listagem e remoção de usuários autenticáveis."
)
public class UsuarioController {

    private final UtilsService utilsService;
    private final UsuarioService usuarioService;
    private final UsuarioEmailVerificacaoService verificacaoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Criar usuário",
            description = "Cadastra um novo usuário autenticável e gera token de verificação de e-mail.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário criado com sucesso",
                            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioRequest request) {
        if (utilsService.verificarPermissao("USUARIO_CRIAR")) {
            UsuarioResponse response = usuarioService.salvar(request);
            verificacaoService.gerarTokenParaUsuario(response.id());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados cadastrais de um usuário existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário atualizado",
                            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request
    ) {
        if (utilsService.verificarPermissao("USUARIO_EDITAR"))
            return ResponseEntity.ok(usuarioService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário pelo identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado",
                            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("USUARIO_LISTAR"))
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY LOGIN
    // =============================================================

    @Operation(
            summary = "Buscar usuário por login",
            description = "Retorna os dados de um usuário pelo login.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado",
                            content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioResponse> buscarPorLogin(@PathVariable String login) {
        if (utilsService.verificarPermissao("USUARIO_LISTAR"))
            return ResponseEntity.ok(usuarioService.buscarPorLogin(login));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS ADMINISTRATIVAS (PAGINADAS)
    // =============================================================

    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna lista paginada de todos os usuários.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarTodos(@ParameterObject Pageable pageable) {
        if (utilsService.verificarPermissao("USUARIO_LISTAR"))
            return ResponseEntity.ok(usuarioService.listarTodos(pageable));
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar usuários ativos",
            description = "Retorna lista paginada de usuários ativos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @GetMapping("/ativos")
    public ResponseEntity<Page<UsuarioResponse>> listarAtivos(@ParameterObject Pageable pageable) {
        if (utilsService.verificarPermissao("USUARIO_LISTAR"))
            return ResponseEntity.ok(usuarioService.listarAtivos(pageable));
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar usuários inativos",
            description = "Retorna lista paginada de usuários inativos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @GetMapping("/inativos")
    public ResponseEntity<Page<UsuarioResponse>> listarInativos(@ParameterObject Pageable pageable) {
        if (utilsService.verificarPermissao("USUARIO_LISTAR"))
            return ResponseEntity.ok(usuarioService.listarInativos(pageable));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // STATUS / DELETE
    // =============================================================

    @Operation(
            summary = "Desativar usuário",
            description = "Desativa um usuário ativo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário desativado"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "422", description = "Usuário já está inativo"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        if (utilsService.verificarPermissao("USUARIO_EDITAR")) {
            Usuario usuario = usuarioService.desativar(id);

            if (usuario == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            if (!usuario.getAtivo())
                return ResponseEntity.unprocessableEntity().build();

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Exclui definitivamente um usuário do sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário excluído"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "422", description = "Usuário não pode ser excluído"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (utilsService.verificarPermissao("USUARIO_EXCLUIR")) {
            Usuario usuario = usuarioService.deletar(id);

            if (usuario == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            if (usuario.getId() > 0)
                return ResponseEntity.unprocessableEntity().build();

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
