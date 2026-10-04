package br.com.unicos.ms_funcionario.client;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Valida no ms-pessoas as pessoas e os usuários referenciados pelos funcionários.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaClient pessoaClient;

    /**
     * @throws EntityNotFoundException quando a pessoa não existe na empresa do contexto atual
     */
    @CircuitBreaker(name = "ms-pessoas", fallbackMethod = "fallbackValidarPessoa")
    public void validarPessoa(Long idPessoa) {
        pessoaClient.buscarPessoa(idPessoa);
    }

    /**
     * @throws EntityNotFoundException quando o usuário não existe na empresa do contexto atual
     */
    @CircuitBreaker(name = "ms-pessoas", fallbackMethod = "fallbackValidarUsuario")
    public void validarUsuario(Long idUsuario) {
        pessoaClient.buscarUsuario(idUsuario);
    }

    private void fallbackValidarPessoa(Long idPessoa, Throwable ex) {
        if (ex instanceof FeignException.NotFound)
            throw new EntityNotFoundException("Pessoa não encontrada: " + idPessoa);

        tratarFalha("a pessoa", idPessoa, ex);
    }

    private void fallbackValidarUsuario(Long idUsuario, Throwable ex) {
        if (ex instanceof FeignException.NotFound)
            throw new EntityNotFoundException("Usuário não encontrado: " + idUsuario);

        tratarFalha("o usuário", idUsuario, ex);
    }

    private void tratarFalha(String recurso, Long id, Throwable ex) {
        if (ex instanceof FeignException.FeignClientException clientException)
            throw clientException;

        log.error("Falha ao validar {} [{}] no ms-pessoas. Causa: {}", recurso, id, ex.getMessage(), ex);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de pessoas temporariamente indisponível");
    }
}
