package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import br.com.unicos.ms_estoque.service.ResponsavelEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
 * Controller responsável pelos endpoints de Responsáveis por Estoque.
 */
@RestController
@RequestMapping("/v1/responsaveis-estoque")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Responsáveis de Estoque",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de responsáveis por estoque."
)
public class ResponsavelEstoqueController {

    private final ResponsavelEstoqueService responsavelService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar responsável por estoque",
            description = "Cria um novo responsável (gestor/ponto focal) para um estoque.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Responsável criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ResponsavelEstoqueResponseDto> salvar(
            @Valid @RequestBody ResponsavelEstoqueCreateRequestDto request
    ) {
        ResponsavelEstoqueResponseDto response = responsavelService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar responsável por estoque",
            description = "Atualiza os dados de um responsável por estoque existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Responsável atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponsavelEstoqueResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResponsavelEstoqueUpdateRequestDto request
    ) {
        return ResponseEntity.ok(responsavelService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar responsável por ID",
            description = "Retorna os dados de um responsável por estoque específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponsavelEstoqueResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(responsavelService.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar responsáveis por estoque",
            description = "Lista responsáveis vinculados a um estoque de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/estoque/{estoqueId}")
    public ResponseEntity<Page<ResponsavelEstoqueResponseDto>> listarPorEstoque(
            @PathVariable Long estoqueId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(responsavelService.listarPorEstoque(estoqueId, pageable));
    }

    @Operation(
            summary = "Listar responsáveis por estoque e status",
            description = "Lista responsáveis vinculados a um estoque filtrando por status, de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/estoque/{estoqueId}/status/{status}")
    public ResponseEntity<Page<ResponsavelEstoqueResponseDto>> listarPorEstoqueEStatus(
            @PathVariable Long estoqueId,
            @PathVariable StatusResponsavelEstoque status,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(responsavelService.listarPorEstoqueEStatus(estoqueId, status, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover responsável por estoque",
            description = "Remove um responsável por estoque pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responsável removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        responsavelService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
