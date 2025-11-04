package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.EmpresaRequest;
import br.com.unicos.ms_empresa.dto.EmpresaResponse;
import br.com.unicos.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.FilialRequest;
import br.com.unicos.ms_empresa.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de empresas.
 * <p>
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão.
 */
@RestController
@RequestMapping("/v1/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    /**
     * Cria uma nova empresa com sua filial matriz e endereço principal.
     *
     * @param empresaRequest  dados da empresa
     * @param enderecoRequest dados do endereço principal
     * @param filialRequest   dados da filial matriz
     * @return empresa criada
     * @status 201 Created
     */
    @PostMapping
    public ResponseEntity<EmpresaResponse> criar(@Valid @RequestBody EmpresaRequest empresaRequest,
                                                 @Valid @RequestBody EnderecoEmpresaRequest enderecoRequest,
                                                 @Valid @RequestBody FilialRequest filialRequest) {
        EmpresaResponse response = empresaService.salvar(empresaRequest, enderecoRequest, filialRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza dados de uma empresa existente.
     *
     * @param id      id da empresa
     * @param request novos dados
     * @return empresa atualizada
     * @status 200 OK / 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody EmpresaRequest request) {
        return ResponseEntity.ok(empresaService.atualizar(id, request));
    }

    /**
     * Lista todas as empresas.
     *
     * @return lista de empresas
     * @status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<EmpresaResponse>> listar() {
        return ResponseEntity.ok(empresaService.listarTodas());
    }

    /**
     * Busca empresa por ID.
     *
     * @param id id da empresa
     * @return empresa encontrada (ou 404)
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca empresa por CNPJ.
     *
     * @param cnpj CNPJ
     * @return empresa encontrada (ou 404)
     * @status 200 OK / 404 Not Found
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<EmpresaResponse> buscarPorCnpj(@PathVariable String cnpj) {
        return empresaService.buscarPorCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove uma empresa pelo ID.
     *
     * @param id id da empresa
     * @return 204 sem conteúdo
     * @status 204 No Content / 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        empresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
