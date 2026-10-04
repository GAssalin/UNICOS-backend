package br.com.unicos.ms_funcionario.client;

import br.com.unicos.core.pessoas.dto.pessoa.PessoaResponseClient;
import br.com.unicos.core.usuario.dto.UsuarioResumoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Consultas ao ms-pessoas. Pessoas e usuários são buscados na empresa do JWT repassado,
 * portanto registros de outra empresa respondem {@code 404}.
 */
@FeignClient(
        name = "ms-pessoas",
        contextId = "PessoaClient"
)
public interface PessoaClient {

    @GetMapping("/internal/pessoas/{id}")
    PessoaResponseClient buscarPessoa(@PathVariable("id") Long id);

    @GetMapping("/internal/usuarios/{id}/resumo")
    UsuarioResumoResponse buscarUsuario(@PathVariable("id") Long id);
}
