package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.service.interfaces.MunicipioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de municípios dentro do UniCoS.
 * <p>
 * Permite operações de criação, atualização, exclusão e consultas
 * por nome, UF, código IBGE e ID.
 */
@RestController
@RequestMapping("/v1/municipios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Municípios",
        description = "Operações de gestão e consulta de municípios (UF, IBGE, nome, ID)."
)
public class MunicipioController {

    private final MunicipioService service;

    // ============================================================
    // Criar
    // ============================================================

    @PreAuthorize("hasAuthority('MUNICIPIO_CRIAR')")
    @Operation(
            summary = "Criar município",
            description = "Registra um novo município no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Município criado com sucesso",
                            content = @Content(schema = @Schema(implementation = MunicipioResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados enviados são inválidos"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<MunicipioResponse> criar(@RequestBody MunicipioRequest request) {
        MunicipioResponse response = service.criar(request);
        return ResponseEntity
                .created(URI.create("/v1/municipios/" + response.id()))
                .body(response);
    }

    // ============================================================
    // Atualizar
    // ============================================================

    @PreAuthorize("hasAuthority('MUNICIPIO_EDITAR')")
    @Operation(
            summary = "Atualizar município",
            description = "Atualiza os dados de um município existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Município atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = MunicipioResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Município não encontrado"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<MunicipioResponse> atualizar(
            @PathVariable Long id,
            @RequestBody MunicipioRequest request) {

        MunicipioResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Excluir
    // ============================================================

    @PreAuthorize("hasAuthority('MUNICIPIO_EXCLUIR')")
    @Operation(
            summary = "Excluir município",
            description = "Remove um município do sistema pelo ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Município excluído com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Município não encontrado"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    @PreAuthorize("hasAuthority('MUNICIPIO_LISTAR')")
    @Operation(
            summary = "Buscar município por ID",
            description = "Retorna o município correspondente ao ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Município encontrado",
                            content = @Content(schema = @Schema(implementation = MunicipioResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Município não encontrado"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MunicipioResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    @PreAuthorize("hasAuthority('MUNICIPIO_LISTAR')")
    @Operation(
            summary = "Listar todos os municípios",
            description = "Retorna todos os municípios cadastrados no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MunicipioListDTO.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<MunicipioListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar por Nome
    // ============================================================

    @PreAuthorize("hasAuthority('MUNICIPIO_LISTAR')")
    @Operation(
            summary = "Listar municípios por nome",
            description = "Retorna municípios cujo nome contenha o termo informado (ignore case).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista filtrada retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MunicipioListDTO.class)))
                    )
            }
    )
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<MunicipioListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }

    // ============================================================
    // Listar por UF
    // ============================================================

    @PreAuthorize("hasAuthority('MUNICIPIO_LISTAR')")
    @Operation(
            summary = "Listar municípios por UF",
            description = "Retorna todos os municípios pertencentes à unidade federativa informada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MunicipioListDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "UF inválida"
                    )
            }
    )
    @GetMapping("/uf/{uf}")
    public ResponseEntity<List<MunicipioListDTO>> listarPorUf(@PathVariable String uf) {
        return ResponseEntity.ok(service.listarPorUf(uf));
    }

    // ============================================================
    // Buscar por Código IBGE
    // ============================================================

    @PreAuthorize("hasAuthority('MUNICIPIO_LISTAR')")
    @Operation(
            summary = "Buscar município por código IBGE",
            description = "Consulta um município pelo seu código IBGE oficial.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Município encontrado",
                            content = @Content(schema = @Schema(implementation = MunicipioResponse.class)))
                    ,
                    @ApiResponse(
                            responseCode = "404",
                            description = "Código IBGE não encontrado"
                    )
            }
    )
    @GetMapping("/ibge/{codigoIbge}")
    public ResponseEntity<MunicipioResponse> buscarPorCodigoIbge(@PathVariable String codigoIbge) {
        return service.buscarPorCodigoIbge(codigoIbge)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
