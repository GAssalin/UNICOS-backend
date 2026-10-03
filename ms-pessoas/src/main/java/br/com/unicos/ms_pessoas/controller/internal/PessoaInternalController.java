package br.com.unicos.ms_pessoas.controller.internal;

import br.com.unicos.core.pessoas.dto.pessoa.PessoaResponseClient;
import br.com.unicos.ms_pessoas.service.PessoaService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints consumidos apenas por outros microserviços (exigem o token interno).
 */
@Hidden
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class PessoaInternalController {

    private final PessoaService pessoaService;

    /**
     * Dados básicos da pessoa na empresa do contexto atual (JWT repassado pelo serviço chamador).
     */
    @GetMapping("/pessoas/{id}")
    public PessoaResponseClient buscarPorId(@PathVariable("id") Long id) {
        return pessoaService.buscarPorId(id)
                .map(pessoa -> new PessoaResponseClient(pessoa.id(), pessoa.nome()))
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
    }
}
