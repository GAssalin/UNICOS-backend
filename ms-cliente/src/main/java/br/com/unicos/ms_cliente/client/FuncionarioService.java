package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.funcionario.dto.AcessoCarteiraResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioClient funcionarioClient;

    /**
     * Escopo de acesso do usuário à carteira de clientes. Sem essa informação a requisição é
     * recusada (503), nunca liberada sem restrição.
     */
    @CircuitBreaker(name = "ms-funcionario", fallbackMethod = "fallbackBuscarAcessoCarteira")
    public AcessoCarteiraResponse buscarAcessoCarteira(Long idUsuario) {
        return funcionarioClient.buscarAcessoCarteira(idUsuario);
    }

    private AcessoCarteiraResponse fallbackBuscarAcessoCarteira(Long idUsuario, Throwable ex) {
        if (ex instanceof FeignException.FeignClientException clientException)
            throw clientException;

        log.error("Falha ao buscar o acesso à carteira do usuário [{}] no ms-funcionario. Causa: {}", idUsuario, ex.getMessage(), ex);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de funcionários temporariamente indisponível");
    }
}
