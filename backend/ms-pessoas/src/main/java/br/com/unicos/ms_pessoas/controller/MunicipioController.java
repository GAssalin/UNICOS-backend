package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.service.MunicipioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de municípios.
 *
 * <p>
 * Disponibiliza endpoints para criação, atualização, consulta,
 * listagem e exclusão de municípios, incluindo filtros por nome,
 * UF e código IBGE.
 * </p>
 */
@RestController
@RequestMapping("/v1/municipios")
@RequiredArgsConstructor
@Tag(
        name = "Municípios",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de municípios."
)
public class MunicipioController {

    private final MunicipioService municipioService;

    // =============================================================
    // CREATE
    // =============================================================

    @PostMapping
    public ResponseEntity<MunicipioResponse> criar(@Valid @RequestBody MunicipioRequest request) {
        MunicipioResponse response = municipioService.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================

    @PutMapping("/{id}")
    public ResponseEntity<MunicipioResponse> atualizar(@PathVariable Long id, @Valid @RequestBody MunicipioRequest request) {
        return ResponseEntity.ok(municipioService.atualizar(id, request));
    }

    // =============================================================
    // GET BY ID
    // =============================================================

    @GetMapping("/{id}")
    public ResponseEntity<MunicipioResponse> buscarPorId(@PathVariable Long id) {
        Optional<MunicipioResponse> response = municipioService.buscarPorId(id);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // LISTAGENS
    // =============================================================

    /**
     * Lista todos os municípios cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<MunicipioListDTO>> listarTodos() {
        return ResponseEntity.ok(municipioService.listarTodos());
    }

    /**
     * Lista municípios filtrando por nome (contains ignore case).
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<MunicipioListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(municipioService.listarPorNome(nome));
    }

    /**
     * Lista municípios filtrando por UF.
     */
    @GetMapping("/uf/{uf}")
    public ResponseEntity<List<MunicipioListDTO>> listarPorUf(@PathVariable String uf) {
        return ResponseEntity.ok(municipioService.listarPorUf(uf));
    }

    // =============================================================
    // BUSCA POR CÓDIGO IBGE
    // =============================================================

    @GetMapping("/ibge/{codigoIbge}")
    public ResponseEntity<MunicipioResponse> buscarPorCodigoIbge(@PathVariable String codigoIbge) {
        Optional<MunicipioResponse> response =
                municipioService.buscarPorCodigoIbge(codigoIbge);
        return response
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =============================================================
    // DELETE
    // =============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        municipioService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
