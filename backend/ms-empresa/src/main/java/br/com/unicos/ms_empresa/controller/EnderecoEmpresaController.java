package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.EnderecoEmpresaResponse;
import br.com.unicos.ms_empresa.service.EnderecoEmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de endereços empresariais.
 */
@RestController
@RequestMapping("/v1/enderecos")
public class EnderecoEmpresaController {

    private final EnderecoEmpresaService enderecoEmpresaService;

    public EnderecoEmpresaController(EnderecoEmpresaService enderecoEmpresaService) {
        this.enderecoEmpresaService = enderecoEmpresaService;
    }

    /**
     * Cria um endereço empresarial.
     *
     * @param request dados do endereço
     * @return endereço criado
     * @status 201 Created
     */
    @PostMapping
    public ResponseEntity<EnderecoEmpresaResponse> criar(@Valid @RequestBody EnderecoEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoEmpresaService.salvar(request));
    }

    /**
     * Atualiza um endereço empresarial.
     *
     * @param id      id do endereço
     * @param request novos dados
     * @return endereço atualizado
     * @status 200 OK / 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoEmpresaResponse> atualizar(@PathVariable Long id,
                                                             @Valid @RequestBody EnderecoEmpresaRequest request) {
        return ResponseEntity.ok(enderecoEmpresaService.atualizar(id, request));
    }

    /**
     * Busca um endereço por ID.
     *
     * @param id id do endereço
     * @return endereço encontrado (ou 404)
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoEmpresaResponse> buscarPorId(@PathVariable Long id) {
        return enderecoEmpresaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os endereços (ordenados por UF e cidade, caso a service implemente).
     *
     * @return lista de endereços
     * @status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<?>> listarTodos() {
        return ResponseEntity.ok(enderecoEmpresaService.listarOrdenadosPorEstadoECidade());
    }

    /**
     * Remove um endereço.
     *
     * @param id id do endereço
     * @return 204 sem conteúdo
     * @status 204 No Content / 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoEmpresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
