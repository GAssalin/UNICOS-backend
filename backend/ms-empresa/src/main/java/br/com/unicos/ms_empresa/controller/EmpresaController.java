package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.EmpresaRequest;
import br.com.unicos.ms_empresa.dto.EmpresaResponse;
import br.com.unicos.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.unicos.ms_empresa.dto.FilialRequest;
import br.com.unicos.ms_empresa.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento de empresas.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de empresas.
 */
@RestController
@RequestMapping("/v1/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    /**
     * Cria uma nova empresa juntamente com seu endereço e filial matriz.
     *
     * @param request          dados da empresa
     * @param enderecoRequest  dados do endereço da empresa
     * @param filialRequest    dados da filial matriz
     * @return empresa criada
     */
    @PostMapping
    public ResponseEntity<EmpresaResponse> criar(
            @Valid @RequestBody EmpresaRequest request,
            @Valid @RequestParam EnderecoEmpresaRequest enderecoRequest,
            @Valid @RequestParam FilialRequest filialRequest) {

        EmpresaResponse response = empresaService.salvar(request, enderecoRequest, filialRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de uma empresa existente.
     *
     * @param id identificador da empresa
     * @param request novos dados da empresa
     * @return empresa atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequest request) {
        EmpresaResponse response = empresaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma empresa pelo seu ID.
     *
     * @param id identificador da empresa
     * @return empresa encontrada, se existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca uma empresa pelo CNPJ.
     *
     * @param cnpj número do CNPJ
     * @return empresa encontrada, se existir
     */
    @GetMapping("/buscar/cnpj")
    public ResponseEntity<EmpresaResponse> buscarPorCnpj(@RequestParam String cnpj) {
        return empresaService.buscarPorCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as empresas cadastradas.
     *
     * @return lista de empresas
     */
    @GetMapping
    public ResponseEntity<List<EmpresaResponse>> listarTodas() {
        List<EmpresaResponse> empresas = empresaService.listarTodas();
        if (empresas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(empresas);
    }

    /**
     * Busca empresas pela razão social (contém).
     *
     * @param razaoSocial termo de busca
     * @return lista de empresas correspondentes
     */
    @GetMapping("/buscar/razao-social")
    public ResponseEntity<List<EmpresaResponse>> buscarPorRazaoSocial(@RequestParam String razaoSocial) {
        List<EmpresaResponse> empresas = empresaService.buscarPorRazaoSocial(razaoSocial);
        if (empresas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(empresas);
    }

    /**
     * Busca empresas pelo nome fantasia (contém).
     *
     * @param nomeFantasia termo de busca
     * @return lista de empresas correspondentes
     */
    @GetMapping("/buscar/nome-fantasia")
    public ResponseEntity<List<EmpresaResponse>> buscarPorNomeFantasia(@RequestParam String nomeFantasia) {
        List<EmpresaResponse> empresas = empresaService.buscarPorNomeFantasia(nomeFantasia);
        if (empresas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(empresas);
    }

    /**
     * Exclui uma empresa pelo seu ID.
     *
     * @param id identificador da empresa
     * @return resposta sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        empresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}