package br.com.unicos.ms_filial.controller;

import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialResponse;
import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialUpdateRequest;
import br.com.unicos.ms_filial.service.EnderecoFilialService;
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

@RestController
@RequestMapping("/v1/enderecos-filial")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Endereços de Filial",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de endereços de filiais."
)
public class EnderecoFilialController {

    private final EnderecoFilialService enderecoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar endereço de filial",
            description = "Cria um novo endereço para uma filial.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Endereço criado com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<EnderecoFilialResponse> salvar(@Valid @RequestBody EnderecoFilialCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.salvar(request));
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar endereço de filial",
            description = "Atualiza os dados de um endereço de filial existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoFilialResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EnderecoFilialUpdateRequest request
    ) {
        return ResponseEntity.ok(enderecoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar endereço de filial por ID",
            description = "Retorna os dados de um endereço de filial específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoFilialResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoFilialResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(enderecoService.buscarPorId(id));
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar endereços por filial",
            description = "Lista os endereços vinculados a uma filial de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = EnderecoFilialResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<Page<EnderecoFilialResponse>> listarPorFilial(
            @PathVariable Long filialId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(enderecoService.listarPorFilial(filialId, pageable));
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover endereço de filial",
            description = "Remove um endereço de filial pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
