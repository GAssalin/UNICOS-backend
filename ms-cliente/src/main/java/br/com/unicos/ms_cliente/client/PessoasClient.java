package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.pessoas.dto.pessoa.PessoaResponseClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "ms-pessoas",
        contextId = "PessoasClient"
)
public interface PessoasClient {

    @GetMapping("/{id}")
    PessoaResponseClient buscarPorId(@PathVariable Long id);

}