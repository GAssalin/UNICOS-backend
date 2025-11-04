package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.FilialListDTO;
import br.com.unicos.ms_empresa.dto.FilialRequest;
import br.com.unicos.ms_empresa.dto.FilialResponse;
import br.com.unicos.ms_empresa.service.FilialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de filiais.
 */
@RestController
@RequestMapping("/v1/filiais")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    /**
     * Cria uma filial.
     *
     * @param request dados da filial
     * @return filial criada
     * @status 201 Created
     */
    @PostMapping
    public ResponseEntity<FilialResponse> criar(@Valid @RequestBody FilialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filialService.salvar(request));
    }

    /**
     * Atualiza uma filial.
     *
     * @param id      id da filial
     * @param request novos dados
     * @return filial atualizada
     * @status 200 OK / 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<FilialResponse> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody FilialRequest request) {
        return ResponseEntity.ok(filialService.atualizar(id, request));
    }

    /**
     * Lista todas as filiais (visão resumida).
     *
     * @return lista de filiais
     * @status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<FilialListDTO>> listarTodas() {
        return ResponseEntity.ok(filialService.listarTodas());
    }

    /**
     * Busca filial por ID.
     *
     * @param id id da filial
     * @return filial encontrada (ou 404)
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<FilialResponse> buscarPorId(@PathVariable Long id) {
        return filialService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove uma filial.
     *
     * @param id id da filial
     * @return 204 sem conteúdo
     * @status 204 No Content / 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        filialService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
