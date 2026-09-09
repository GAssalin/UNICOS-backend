package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa.EmpresaCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResponse;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaListDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaUpdateRequest;
import br.com.unicos.ms_empresa.enums.StatusEmpresa;
import br.com.unicos.ms_empresa.enums.TipoEmpresa;
import br.com.unicos.ms_empresa.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de gerenciamento de empresas.
 */
@RestController
@RequestMapping({"/v1/empresas", "/api/empresas"})
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    /**
     * Cadastra uma nova empresa.
     *
     * @param request dados para criação da empresa
     * @return empresa cadastrada
     */
    @PostMapping
    public ResponseEntity<EmpresaResponse> criar(@Valid @RequestBody EmpresaCreateRequest request) {
        EmpresaResponse response = empresaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca uma empresa pelo ID.
     *
     * @param id identificador da empresa
     * @return empresa encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable Long id) {
        EmpresaResponse response = empresaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma empresa pelo CNPJ.
     *
     * @param cnpj CNPJ da empresa
     * @return empresa encontrada
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<EmpresaResponse> buscarPorCnpj(@PathVariable String cnpj) {
        EmpresaResponse response = empresaService.buscarPorCnpj(cnpj);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as empresas de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @GetMapping
    public ResponseEntity<Page<EmpresaListDTO>> listarTodas(Pageable pageable) {
        Page<EmpresaListDTO> response = empresaService.listarTodas(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista empresas por status.
     *
     * @param statusEmpresa status da empresa
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @GetMapping("/status/{statusEmpresa}")
    public ResponseEntity<Page<EmpresaListDTO>> listarPorStatus(
            @PathVariable StatusEmpresa statusEmpresa,
            Pageable pageable) {
        Page<EmpresaListDTO> response = empresaService.listarPorStatus(statusEmpresa, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista empresas por tipo.
     *
     * @param tipoEmpresa tipo da empresa
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @GetMapping("/tipo/{tipoEmpresa}")
    public ResponseEntity<Page<EmpresaListDTO>> listarPorTipo(
            @PathVariable TipoEmpresa tipoEmpresa,
            Pageable pageable) {
        Page<EmpresaListDTO> response = empresaService.listarPorTipo(tipoEmpresa, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista empresas vinculadas a uma matriz.
     *
     * @param matrizId identificador da matriz
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @GetMapping("/matriz/{matrizId}")
    public ResponseEntity<Page<EmpresaListDTO>> listarPorMatriz(
            @PathVariable Long matrizId,
            Pageable pageable) {
        Page<EmpresaListDTO> response = empresaService.listarPorMatriz(matrizId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista empresas por matriz e tipo.
     *
     * @param matrizId identificador da matriz
     * @param tipoEmpresa tipo da empresa
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @GetMapping("/matriz/{matrizId}/tipo/{tipoEmpresa}")
    public ResponseEntity<Page<EmpresaListDTO>> listarPorMatrizETipo(
            @PathVariable Long matrizId,
            @PathVariable TipoEmpresa tipoEmpresa,
            Pageable pageable) {
        Page<EmpresaListDTO> response = empresaService.listarPorMatrizETipo(matrizId, tipoEmpresa, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista empresas por matriz e status.
     *
     * @param matrizId identificador da matriz
     * @param statusEmpresa status da empresa
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @GetMapping("/matriz/{matrizId}/status/{statusEmpresa}")
    public ResponseEntity<Page<EmpresaListDTO>> listarPorMatrizEStatus(
            @PathVariable Long matrizId,
            @PathVariable StatusEmpresa statusEmpresa,
            Pageable pageable) {
        Page<EmpresaListDTO> response = empresaService.listarPorMatrizEStatus(matrizId, statusEmpresa, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza os dados de uma empresa.
     *
     * @param id identificador da empresa
     * @param request dados para atualização
     * @return empresa atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaUpdateRequest request) {
        EmpresaResponse response = empresaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove uma empresa pelo ID.
     *
     * @param id identificador da empresa
     * @return resposta sem conteúdo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        empresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}