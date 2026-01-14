package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;
import br.com.unicos.ms_pessoas.service.EnderecoService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/enderecos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Endereços",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de endereços de pessoas."
)
public class EnderecoController {

    private final EnderecoService enderecoService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar endereço",
            description = "Cria um novo endereço vinculado a uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Endereço criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = EnderecoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<EnderecoResponse> criar(@Valid @RequestBody EnderecoRequest request) {
        EnderecoResponse response = enderecoService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar endereço",
            description = "Atualiza os dados de um endereço existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = EnderecoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EnderecoRequest request
    ) {
        return ResponseEntity.ok(enderecoService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar endereço por ID",
            description = "Retorna os dados de um endereço específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = EnderecoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> buscarPorId(@PathVariable Long id) {
        Optional<EnderecoResponse> response = enderecoService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todos os endereços",
            description = "Retorna todos os endereços cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EnderecoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<List<EnderecoListDTO>> listarTodos() {
        return ResponseEntity.ok(enderecoService.listarTodos());
    }

    @Operation(
            summary = "Listar endereços por pessoa",
            description = "Retorna os endereços vinculados a uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EnderecoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(enderecoService.listarPorPessoa(pessoaId));
    }

    @Operation(
            summary = "Listar endereços por pessoa e tipo",
            description = "Retorna os endereços de uma pessoa filtrando pelo tipo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EnderecoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/{pessoaId}/tipo/{tipo}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorPessoaETipo(
            @PathVariable Long pessoaId,
            @PathVariable String tipo
    ) {
        return ResponseEntity.ok(enderecoService.listarPorPessoaETipo(pessoaId, tipo));
    }

    @Operation(
            summary = "Listar endereços por município",
            description = "Retorna os endereços filtrados pelo município.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EnderecoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/municipio/{municipioId}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorMunicipio(@PathVariable Long municipioId) {
        return ResponseEntity.ok(enderecoService.listarPorMunicipio(municipioId));
    }

    @Operation(
            summary = "Listar endereços por CEP",
            description = "Retorna os endereços filtrados pelo CEP.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EnderecoListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/cep/{cep}")
    public ResponseEntity<List<EnderecoListDTO>> listarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(enderecoService.listarPorCep(cep));
    }

    @Operation(
            summary = "Buscar endereço principal da pessoa",
            description = "Retorna o endereço principal de uma pessoa.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = EnderecoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Endereço principal não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/pessoa/{pessoaId}/principal")
    public ResponseEntity<EnderecoResponse> buscarPrincipal(@PathVariable Long pessoaId) {
        Optional<EnderecoResponse> response = enderecoService.buscarPrincipal(pessoaId);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover endereço",
            description = "Remove um endereço pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        enderecoService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
