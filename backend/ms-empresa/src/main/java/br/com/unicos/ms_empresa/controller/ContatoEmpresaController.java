package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.ContatoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.ContatoEmpresaResponse;
import br.com.unicos.ms_empresa.service.ContatoEmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de contatos corporativos.
 */
@RestController
@RequestMapping("/v1/contatos")
public class ContatoEmpresaController {

    private final ContatoEmpresaService contatoEmpresaService;

    public ContatoEmpresaController(ContatoEmpresaService contatoEmpresaService) {
        this.contatoEmpresaService = contatoEmpresaService;
    }

    /**
     * Cria um contato corporativo.
     *
     * @param request dados do contato
     * @return contato criado
     * @status 201 Created
     */
    @PostMapping
    public ResponseEntity<ContatoEmpresaResponse> criar(@Valid @RequestBody ContatoEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoEmpresaService.salvar(request));
    }

    /**
     * Atualiza um contato corporativo.
     *
     * @param id      id do contato
     * @param request novos dados
     * @return contato atualizado
     * @status 200 OK / 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContatoEmpresaResponse> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody ContatoEmpresaRequest request) {
        return ResponseEntity.ok(contatoEmpresaService.atualizar(id, request));
    }

    /**
     * Lista todos os contatos.
     *
     * @return lista de contatos
     * @status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<ContatoEmpresaResponse>> listar() {
        return ResponseEntity.ok(contatoEmpresaService.listarTodos());
    }

    /**
     * Busca contato por ID.
     *
     * @param id id do contato
     * @return contato encontrado (ou 404)
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContatoEmpresaResponse> buscarPorId(@PathVariable Long id) {
        return contatoEmpresaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove um contato.
     *
     * @param id id do contato
     * @return 204 sem conteúdo
     * @status 204 No Content / 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contatoEmpresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
