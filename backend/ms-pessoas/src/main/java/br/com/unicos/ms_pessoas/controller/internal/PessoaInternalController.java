package br.com.unicos.ms_pessoas.controller.internal;

import br.com.unicos.core.pessoas.dto.pessoa.PessoaResponseClient;
import br.com.unicos.core.pessoas.exception.PessoaNotFoundException;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class PessoaInternalController {

    private final PessoaService pessoaService;

    @GetMapping("/pessoas/{id}")
    public PessoaResponseClient buscarPorId(@PathVariable Long id) {
        PessoaResponse pessoaResponse = pessoaService.buscarPorId(id).orElseThrow(PessoaNotFoundException::new);
        return new PessoaResponseClient(
                pessoaResponse.id(),
                pessoaResponse.nome()
        );
    }
}
