package br.com.erp.ms_empresa.controller;

import br.com.erp.ms_empresa.dto.EnderecoEmpresaListDTO;
import br.com.erp.ms_empresa.dto.EnderecoEmpresaRequest;
import br.com.erp.ms_empresa.dto.EnderecoEmpresaResponse;
import br.com.erp.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.erp.ms_empresa.service.EnderecoEmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento dos endereços empresariais.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de endereços.
 */
@RestController
@RequestMapping("/v1/enderecos-empresa")
@RequiredArgsConstructor
public class EnderecoEmpresaController {

    private final EnderecoEmpresaService enderecoEmpresaService;

    /**
     * Cria um novo endereço empresarial.
     *
     * @param request dados do endereço a ser criado
     * @return endereço criado
     */
    @PostMapping
    public ResponseEntity<EnderecoEmpresaResponse> criar(@Valid @RequestBody EnderecoEmpresaRequest request) {
        EnderecoEmpresaResponse response = enderecoEmpresaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um endereço empresarial existente.
     *
     * @param id identificador do endereço
     * @param request dados atualizados
     * @return endereço atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoEmpresaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EnderecoEmpresaRequest request) {
        EnderecoEmpresaResponse response = enderecoEmpresaService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um endereço empresarial pelo seu ID.
     *
     * @param id identificador do endereço
     * @return endereço encontrado, se existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoEmpresaResponse> buscarPorId(@PathVariable Long id) {
        EnderecoEmpresaResponse response = enderecoEmpresaService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os endereços vinculados a uma empresa.
     *
     * @param empresaId identificador da empresa
     * @return lista de endereços
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<EnderecoEmpresaListDTO>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<EnderecoEmpresaListDTO> enderecos = enderecoEmpresaService.findByEmpresa(empresaId);
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enderecos);
    }

    /**
     * Busca um endereço pelo CEP informado.
     *
     * @param cep código postal
     * @return endereço encontrado, se existir
     */
    @GetMapping("/buscar/cep")
    public ResponseEntity<EnderecoEmpresaResponse> buscarPorCep(@RequestParam String cep) {
        return enderecoEmpresaService.findByCep(cep)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista endereços de uma cidade específica.
     *
     * @param cidade nome da cidade
     * @return lista de endereços
     */
    @GetMapping("/buscar/cidade")
    public ResponseEntity<List<EnderecoEmpresaListDTO>> buscarPorCidade(@RequestParam String cidade) {
        List<EnderecoEmpresaListDTO> enderecos = enderecoEmpresaService.findByCidade(cidade);
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enderecos);
    }

    /**
     * Lista endereços de uma determinada UF.
     *
     * @param uf sigla da unidade federativa
     * @return lista de endereços
     */
    @GetMapping("/buscar/uf")
    public ResponseEntity<List<EnderecoEmpresaListDTO>> buscarPorUf(@RequestParam String uf) {
        List<EnderecoEmpresaListDTO> enderecos = enderecoEmpresaService.findByUf(uf);
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enderecos);
    }

    /**
     * Lista endereços de um tipo específico (ex: MATRIZ, ENTREGA, FATURAMENTO).
     *
     * @param tipo tipo de endereço
     * @return lista de endereços
     */
    @GetMapping("/buscar/tipo")
    public ResponseEntity<List<EnderecoEmpresaListDTO>> buscarPorTipo(@RequestParam TipoEnderecoEmpresa tipo) {
        List<EnderecoEmpresaListDTO> enderecos = enderecoEmpresaService.findByTipo(tipo);
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enderecos);
    }

    /**
     * Exclui um endereço empresarial.
     *
     * @param id identificador do endereço
     * @return resposta sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoEmpresaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}