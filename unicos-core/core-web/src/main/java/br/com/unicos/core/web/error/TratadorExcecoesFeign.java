package br.com.unicos.core.web.error;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratamento de falhas em chamadas Feign para outros microserviços.
 *
 * <p>
 * Erros de negócio do serviço chamado (400, 404, 409, 422) são repassados com o mesmo status;
 * demais falhas viram {@code 502 Bad Gateway}, sem expor detalhes internos ao cliente.
 * </p>
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class TratadorExcecoesFeign {

    @ExceptionHandler(FeignException.class)
    public ProblemDetail tratarFalhaIntegracao(FeignException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.status());

        if (status != null && isErroDeNegocio(status))
            return Problemas.criar(status, null, "Requisição rejeitada por um serviço dependente.", request);

        log.error("Falha na integração com outro microserviço ao processar {} {} (status {}).",
                request.getMethod(), request.getRequestURI(), ex.status(), ex);

        return Problemas.criar(
                HttpStatus.BAD_GATEWAY,
                "Falha de integração",
                "Não foi possível concluir a operação em um serviço dependente.",
                request
        );
    }

    private static boolean isErroDeNegocio(HttpStatus status) {
        return status == HttpStatus.BAD_REQUEST
                || status == HttpStatus.NOT_FOUND
                || status == HttpStatus.CONFLICT
                || status == HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
