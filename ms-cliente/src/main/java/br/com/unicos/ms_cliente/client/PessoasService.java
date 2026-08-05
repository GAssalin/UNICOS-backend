package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.pessoas.dto.pessoa.PessoaResponseClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PessoasService {
    private final PessoasClient pessoasClient;

    @CircuitBreaker(name = "ms-permissao", fallbackMethod = "fallbackBuscarPorId")
    public PessoaResponseClient buscarPorId(Long idPessoa) {
        return pessoasClient.buscarPorId(idPessoa);
    }
    private PessoaResponseClient fallbackBuscarPorId(Long idPessoa, Throwable ex) {
        log.error(
                "Fallback do CircuitBreaker acionado ao buscar pessoa [{}]. Causa: {}",
                idPessoa,
                ex.getMessage(),
                ex
        );

        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de busca da pessoa temporariamente indisponível");
    }
}
