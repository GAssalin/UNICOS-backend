package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoListDTO;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoRequest;
import br.com.unicos.ms_produtos.dto.atributoPersonalizado.AtributoPersonalizadoResponse;
import br.com.unicos.ms_produtos.service.AtributoPersonalizadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento dos atributos personalizados
 * associados às categorias de produtos.
 * <p>
 * Expõe endpoints para criação, atualização, exclusão, listagem e consulta
 * de atributos vinculados às categorias.
 */
@RestController
@RequestMapping("/v1/atributos-personalizados")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Atributos Personalizados",
        description = "Gerencia atributos customizados associados às categorias de produtos do UniCoS."
)
public class AtributoPersonalizadoController {

    private final AtributoPersonalizadoService atributoPersonalizadoService;

    // ============================================================
    // Criar
    // ============================================================

    @PreAuthorize("hasAuthority('ATRIBUTO_PERSONALIZADO_CRIAR')")
    @Operation(
            summary = "Criar novo atributo personalizado",
            description = "Registra um atributo personalizado vinculado a uma categoria de produto.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Atributo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = AtributoPersonalizadoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<AtributoPersonalizadoResponse> criar(
            @Valid @RequestBody AtributoPersonalizadoRequest request) {

        AtributoPersonalizadoResponse response = atributoPersonalizadoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @PreAuthorize("hasAuthority('ATRIBUTO_PERSONALIZADO_EDITAR')")
    @Operation(
            summary = "Atualizar atributo personalizado",
            description = "Atualiza as informações de um atributo previamente cadastrado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Atributo atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = AtributoPersonalizadoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    ),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtributoPersonalizadoRequest request) {

        AtributoPersonalizadoResponse response = atributoPersonalizadoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @PreAuthorize("hasAuthority('ATRIBUTO_PERSONALIZADO_EXCLUIR')")
    @Operation(
            summary = "Excluir atributo personalizado",
            description = "Remove um atributo pelo ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Atributo removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        atributoPersonalizadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @PreAuthorize("hasAuthority('ATRIBUTO_PERSONALIZADO_LISTAR')")
    @Operation(
            summary = "Buscar atributo personalizado por ID",
            description = "Retorna os dados completos de um atributo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Atributo encontrado",
                            content = @Content(schema = @Schema(implementation = AtributoPersonalizadoResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Atributo não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AtributoPersonalizadoResponse> buscarPorId(@PathVariable Long id) {

        Optional<AtributoPersonalizadoResponse> resultado =
                atributoPersonalizadoService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar todos
    // ============================================================

    @PreAuthorize("hasAuthority('ATRIBUTO_PERSONALIZADO_LISTAR')")
    @Operation(
            summary = "Listar todos os atributos personalizados",
            description = "Retorna todos os atributos registrados no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AtributoPersonalizadoListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<AtributoPersonalizadoListDTO>> listarTodos() {
        return ResponseEntity.ok(atributoPersonalizadoService.listarTodos());
    }

    // ============================================================
    // Listar por categoria
    // ============================================================

    @PreAuthorize("hasAuthority('ATRIBUTO_PERSONALIZADO_LISTAR')")
    @Operation(
            summary = "Listar atributos por categoria de produto",
            description = "Retorna os atributos personalizados associados a uma categoria específica.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AtributoPersonalizadoListDTO.class)))
                    ),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada ou sem atributos")
            }
    )
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<AtributoPersonalizadoListDTO>> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(atributoPersonalizadoService.listarPorCategoria(categoriaId));
    }
}
