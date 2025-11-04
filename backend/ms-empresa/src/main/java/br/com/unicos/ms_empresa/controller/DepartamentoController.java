package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.DepartamentoRequest;
import br.com.unicos.ms_empresa.dto.DepartamentoResponse;
import br.com.unicos.ms_empresa.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de departamentos.
 */
@RestController
@RequestMapping("/v1/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    /**
     * Cria um departamento.
     *
     * @param request dados do departamento
     * @return departamento criado
     * @status 201 Created
     */
    @PostMapping
    public ResponseEntity<DepartamentoResponse> criar(@Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.salvar(request));
    }

    /**
     * Atualiza um departamento.
     *
     * @param id      id do departamento
     * @param request novos dados
     * @return departamento atualizado
     * @status 200 OK / 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.ok(departamentoService.atualizar(id, request));
    }

    /**
     * Lista todos os departamentos.
     *
     * @return lista de departamentos
     * @status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<DepartamentoResponse>> listar() {
        return ResponseEntity.ok(departamentoService.listarTodos());
    }

    /**
     * Busca departamento por ID.
     *
     * @param id id do departamento
     * @return departamento encontrado (ou 404)
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> buscarPorId(@PathVariable Long id) {
        return departamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove um departamento.
     *
     * @param id id do departamento
     * @return 204 sem conteúdo
     * @status 204 No Content / 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        departamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
