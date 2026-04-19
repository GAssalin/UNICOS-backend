package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa.EmpresaCreateRequestDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResponseDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResumoDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaUpdateRequestDTO;
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
@RequestMapping("/api/empresas")
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
    public ResponseEntity<EmpresaResponseDTO> criar(@Valid @RequestBody EmpresaCreateRequestDTO request) {
        EmpresaResponseDTO response = empresaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca uma empresa pelo ID.
     *
     * @param id identificador da empresa
     * @return empresa encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {
        EmpresaResponseDTO response = empresaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma empresa pelo CNPJ.
     *
     * @param cnpj CNPJ da empresa
     * @return empresa encontrada
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(@PathVariable String cnpj) {
        EmpresaResponseDTO response = empresaService.buscarPorCnpj(cnpj);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as empresas de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página com resumo das empresas
     */
    @GetMapping
    public ResponseEntity<Page<EmpresaResumoDTO>> listarTodas(Pageable pageable) {
        Page<EmpresaResumoDTO> response = empresaService.listarTodas(pageable);
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
    public ResponseEntity<Page<EmpresaResumoDTO>> listarPorStatus(
            @PathVariable StatusEmpresa statusEmpresa,
            Pageable pageable) {
        Page<EmpresaResumoDTO> response = empresaService.listarPorStatus(statusEmpresa, pageable);
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
    public ResponseEntity<Page<EmpresaResumoDTO>> listarPorTipo(
            @PathVariable TipoEmpresa tipoEmpresa,
            Pageable pageable) {
        Page<EmpresaResumoDTO> response = empresaService.listarPorTipo(tipoEmpresa, pageable);
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
    public ResponseEntity<Page<EmpresaResumoDTO>> listarPorMatriz(
            @PathVariable Long matrizId,
            Pageable pageable) {
        Page<EmpresaResumoDTO> response = empresaService.listarPorMatriz(matrizId, pageable);
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
    public ResponseEntity<Page<EmpresaResumoDTO>> listarPorMatrizETipo(
            @PathVariable Long matrizId,
            @PathVariable TipoEmpresa tipoEmpresa,
            Pageable pageable) {
        Page<EmpresaResumoDTO> response = empresaService.listarPorMatrizETipo(matrizId, tipoEmpresa, pageable);
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
    public ResponseEntity<Page<EmpresaResumoDTO>> listarPorMatrizEStatus(
            @PathVariable Long matrizId,
            @PathVariable StatusEmpresa statusEmpresa,
            Pageable pageable) {
        Page<EmpresaResumoDTO> response = empresaService.listarPorMatrizEStatus(matrizId, statusEmpresa, pageable);
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
    public ResponseEntity<EmpresaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaUpdateRequestDTO request) {
        EmpresaResponseDTO response = empresaService.atualizar(id, request);
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