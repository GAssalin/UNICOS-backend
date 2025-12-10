package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.municipio.MunicipioListDTO;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioRequest;
import br.com.unicos.ms_pessoas.dto.municipio.MunicipioResponse;
import br.com.unicos.ms_pessoas.service.interfaces.MunicipioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de municípios dentro do UniCoS.
 *
 * <p>Permite operações de criação, atualização, exclusão e consultas
 * por nome, UF, código IBGE e ID.</p>
 */
@RestController
@RequestMapping("/v1/municipios")
@RequiredArgsConstructor
public class MunicipioController {

    private final MunicipioService service;

    // ============================================================
    // Criar
    // ============================================================

    /**
     * Cadastra um novo município.
     *
     * @param request dados do município a ser criado.
     * @return ResponseEntity contendo o município criado.
     */
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

    /**
     * Atualiza os dados de um município existente.
     *
     * @param id      identificador do município.
     * @param request dados atualizados.
     * @return ResponseEntity com os dados atualizados.
     */
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

    /**
     * Exclui um município pelo ID.
     *
     * @param id identificador do município.
     * @return ResponseEntity vazio (204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Buscar por ID
    // ============================================================

    /**
     * Busca um município pelo seu identificador.
     *
     * @param id identificador do município.
     * @return ResponseEntity contendo o município ou 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MunicipioResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // Listar Todos
    // ============================================================

    /**
     * Lista todos os municípios cadastrados.
     *
     * @return lista simplificada de municípios.
     */
    @GetMapping
    public ResponseEntity<List<MunicipioListDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ============================================================
    // Listar por Nome
    // ============================================================

    /**
     * Lista municípios cujo nome contenha o termo informado (ignore case).
     *
     * @param nome parte do nome a ser pesquisado.
     * @return lista correspondente de municípios.
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<MunicipioListDTO>> listarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }

    // ============================================================
    // Listar por UF
    // ============================================================

    /**
     * Lista todos os municípios pertencentes à UF informada.
     *
     * @param uf sigla da unidade federativa.
     * @return lista de municípios da UF.
     */
    @GetMapping("/uf/{uf}")
    public ResponseEntity<List<MunicipioListDTO>> listarPorUf(@PathVariable String uf) {
        return ResponseEntity.ok(service.listarPorUf(uf));
    }

    // ============================================================
    // Buscar por Código IBGE
    // ============================================================

    /**
     * Consulta um município pelo seu código IBGE.
     *
     * @param codigoIbge código IBGE oficial.
     * @return ResponseEntity contendo o município ou 404.
     */
    @GetMapping("/ibge/{codigoIbge}")
    public ResponseEntity<MunicipioResponse> buscarPorCodigoIbge(@PathVariable String codigoIbge) {
        return service.buscarPorCodigoIbge(codigoIbge)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
