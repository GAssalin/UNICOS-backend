package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.service.MunicipioService;
import br.com.unicos.ms_pessoas.service.UtilsService;
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

@RestController
@RequestMapping("/v1/municipios")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Municípios",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de municípios."
)
public class MunicipioController {

    private final UtilsService utilsService;
    private final MunicipioService municipioService;

    // =============================================================
    // CREATE
    // =============================================================

    @Operation(
            summary = "Cadastrar município",
            description = "Cria um novo município no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Município criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = MunicipioResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<MunicipioResponse> criar(@Valid @RequestBody MunicipioRequest request) {
        if (utilsService.verificarPermissao("PESSOA_MUNICIPIO_CRIAR"))
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(municipioService.criar(request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @Operation(
            summary = "Atualizar município",
            description = "Atualiza os dados de um município existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Município atualizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = MunicipioResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Município não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<MunicipioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MunicipioRequest request
    ) {
        if (utilsService.verificarPermissao("PESSOA_MUNICIPIO_EDITAR"))
            return ResponseEntity.ok(municipioService.atualizar(id, request));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @Operation(
            summary = "Buscar município por ID",
            description = "Retorna os dados de um município específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = MunicipioResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Município não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MunicipioResponse> buscarPorId(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_MUNICIPIO_LISTAR"))
            return municipioService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    @Operation(
            summary = "Listar todos os municípios",
            description = "Retorna todos os municípios cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MunicipioListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<List<MunicipioListDTO>> listarTodos() {
        if (utilsService.verificarPermissao("PESSOA_MUNICIPIO_LISTAR"))
            return ResponseEntity.ok(municipioService.listarTodos());
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar municípios por nome",
            description = "Retorna municípios cujo nome contenha o valor informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MunicipioListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<MunicipioListDTO>> listarPorNome(@PathVariable String nome) {
        if (utilsService.verificarPermissao("PESSOA_MUNICIPIO_LISTAR"))
            return ResponseEntity.ok(municipioService.listarPorNome(nome));
        else
            return ResponseEntity.status(403).build();
    }

    @Operation(
            summary = "Listar municípios por UF",
            description = "Retorna municípios filtrados pela UF.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MunicipioListDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/uf/{uf}")
    public ResponseEntity<List<MunicipioListDTO>> listarPorUf(@PathVariable String uf) {
        if (utilsService.verificarPermissao("PESSOA_MUNICIPIO_LISTAR"))
            return ResponseEntity.ok(municipioService.listarPorUf(uf));
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // BUSCA POR CÓDIGO IBGE
    // =============================================================

    @Operation(
            summary = "Buscar município por código IBGE",
            description = "Retorna um município com base no código IBGE.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = MunicipioResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Município não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/ibge/{codigoIbge}")
    public ResponseEntity<MunicipioResponse> buscarPorCodigoIbge(@PathVariable String codigoIbge) {
        if (utilsService.verificarPermissao("PESSOA_MUNICIPIO_LISTAR"))
            return municipioService.buscarPorCodigoIbge(codigoIbge)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        else
            return ResponseEntity.status(403).build();
    }

    // =============================================================
    // DELETE
    // =============================================================

    @Operation(
            summary = "Remover município",
            description = "Remove um município pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Município removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Município não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (utilsService.verificarPermissao("PESSOA_MUNICIPIO_EXCLUIR")) {
            municipioService.excluir(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
