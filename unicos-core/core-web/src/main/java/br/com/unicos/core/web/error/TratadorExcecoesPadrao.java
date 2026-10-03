package br.com.unicos.core.web.error;

import br.com.unicos.core.auth.exception.TokenNotDefinedException;
import br.com.unicos.core.tenant.exception.TenantNotDefinedException;
import br.com.unicos.core.usuario.exception.UserNotDefinedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tratamento global de exceções dos microserviços, no formato Problem Details (RFC 9457).
 *
 * <p>
 * Possui a menor precedência entre os {@code @RestControllerAdvice}: tratadores específicos
 * (JPA, Spring Security, Feign) e eventuais tratadores do próprio microserviço são consultados antes.
 * </p>
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class TratadorExcecoesPadrao extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());

        ProblemDetail problem = ex.getBody();
        problem.setTitle("Dados de entrada inválidos");
        problem.setDetail("Um ou mais campos estão inválidos.");
        problem.setProperty("errors", errors);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {
        ResponseEntity<Object> response = super.handleExceptionInternal(ex, body, headers, statusCode, request);

        if (response != null && response.getBody() instanceof ProblemDetail problem)
            Problemas.completar(problem, caminho(request));

        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail tratarConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ProblemDetail problem = Problemas.criar(
                HttpStatus.BAD_REQUEST,
                "Dados de entrada inválidos",
                "Um ou mais parâmetros estão inválidos.",
                request
        );

        Map<String, String> violations = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                violations.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage())
        );

        problem.setProperty("errors", violations);
        return problem;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail tratarTipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return Problemas.criar(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                "Valor inválido para o parâmetro '" + ex.getName() + "'.",
                request
        );
    }

    @ExceptionHandler({TenantNotDefinedException.class, UserNotDefinedException.class, TokenNotDefinedException.class})
    public ProblemDetail tratarContextoAusente(IllegalStateException ex, HttpServletRequest request) {
        return Problemas.criar(HttpStatus.UNAUTHORIZED, "Falha de autenticação", ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail tratarIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return Problemas.criar(HttpStatus.BAD_REQUEST, "Requisição inválida", ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail tratarIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return Problemas.criar(
                HttpStatus.CONFLICT,
                "Operação não permitida no estado atual",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarErroInesperado(Exception ex, HttpServletRequest request) {
        log.error("Erro interno não tratado ao processar {} {}.", request.getMethod(), request.getRequestURI(), ex);

        return Problemas.criar(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                "Ocorreu um erro inesperado ao processar a requisição.",
                request
        );
    }

    private static String caminho(WebRequest request) {
        if (request instanceof NativeWebRequest nativeRequest) {
            HttpServletRequest servletRequest = nativeRequest.getNativeRequest(HttpServletRequest.class);
            if (servletRequest != null)
                return servletRequest.getRequestURI();
        }
        return null;
    }
}
