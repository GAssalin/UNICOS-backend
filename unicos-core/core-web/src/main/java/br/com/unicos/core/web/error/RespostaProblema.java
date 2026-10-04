package br.com.unicos.core.web.error;

import br.com.unicos.core.base.error.ProblemaJson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Escreve respostas de erro no formato Problem Details diretamente no {@link HttpServletResponse}.
 *
 * <p>
 * Usado por filtros e handlers do Spring Security, que executam fora do
 * {@code DispatcherServlet} e por isso não passam pelos {@code @RestControllerAdvice}.
 * </p>
 */
public final class RespostaProblema {

    private RespostaProblema() {
    }

    public static void escrever(
            HttpServletRequest request,
            HttpServletResponse response,
            int status,
            String detalhe
    ) throws IOException {
        if (response.isCommitted())
            return;

        HttpStatus httpStatus = HttpStatus.resolve(status);
        String titulo = httpStatus != null ? httpStatus.getReasonPhrase() : "Erro";

        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(ProblemaJson.CONTENT_TYPE);
        response.getWriter().write(ProblemaJson.serializar(status, titulo, detalhe, request.getRequestURI()));
        response.getWriter().flush();
    }
}
