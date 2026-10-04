package br.com.unicos.core.web.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratamento das exceções de persistência (JPA / Spring Data).
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class TratadorExcecoesJpa {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail tratarEntidadeNaoEncontrada(EntityNotFoundException ex, HttpServletRequest request) {
        return Problemas.criar(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                Problemas.detalheSeguro(ex, "O recurso solicitado não foi encontrado."),
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarViolacaoIntegridade(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Violação de integridade em {} {}: {}", request.getMethod(), request.getRequestURI(),
                ex.getMostSpecificCause().getMessage());

        return Problemas.criar(
                HttpStatus.CONFLICT,
                "Conflito de dados",
                "A operação viola uma restrição de integridade (registro duplicado ou referenciado por outros dados).",
                request
        );
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail tratarConcorrencia(OptimisticLockingFailureException ex, HttpServletRequest request) {
        return Problemas.criar(
                HttpStatus.CONFLICT,
                "Conflito de concorrência",
                "O registro foi alterado por outra operação. Recarregue os dados e tente novamente.",
                request
        );
    }
}
