package br.com.unicos.core.web.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratamento das exceções do Spring Security lançadas a partir dos controllers/services.
 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class TratadorExcecoesSeguranca {

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail tratarAcessoNegado(AccessDeniedException ex, HttpServletRequest request) {
        return Problemas.criar(HttpStatus.FORBIDDEN, "Acesso negado", ex.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail tratarFalhaAutenticacao(AuthenticationException ex, HttpServletRequest request) {
        return Problemas.criar(HttpStatus.UNAUTHORIZED, "Falha de autenticação", ex.getMessage(), request);
    }
}
