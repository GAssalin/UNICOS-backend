package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.SetorListDTO;
import br.com.unicos.ms_empresa.dto.SetorRequest;
import br.com.unicos.ms_empresa.dto.SetorResponse;
import br.com.unicos.ms_empresa.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de setores.
 */
@RestController
@RequestMapping("/v1/setores")
public class SetorController {

    private final SetorService setorService;

    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    /**
     * Cria um setor.
     *
     * @param request dados do setor
     * @return setor criado
     * @status 201 Created
     */
    @PostMapping
    public ResponseEntity<SetorResponse> criar(@Valid @RequestBody SetorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(setorService.salvar(request));
    }

    /**
     * Atualiza um setor.
     *
     * @param id      id do setor
     * @param request novos dados
     * @return setor atualizado
     * @status 200 OK / 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<SetorResponse> atualizar(@PathVariable Long id,
                                                   @Valid @RequestBody SetorRequest request) {
        return ResponseEntity.ok(setorService.atualizar(id, request));
    }

    /**
     * Lista setores (visão resumida).
     *
     * @return lista de setores
     * @status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<SetorListDTO>> listarTodos() {
        return ResponseEntity.ok(setorService.listarTodos());
    }

    /**
     * Busca setor por ID.
     *
     * @param id id do setor
     * @return setor encontrado (ou 404)
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<SetorResponse> buscarPorId(@PathVariable Long id) {
        return setorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove um setor.
     *
     * @param id id do setor
     * @return 204 sem conteúdo
     * @status 204 No Content / 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        setorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
