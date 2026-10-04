package br.com.unicos.core.web.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.OffsetDateTime;

/**
 * Fábrica de {@link ProblemDetail} com os campos padrão da plataforma ({@code timestamp} e {@code path}).
 */
public final class Problemas {

    private static final String PACOTE_APLICACAO = "br.com.unicos.";

    private Problemas() {
    }

    /**
     * Mensagem da exceção quando ela foi lançada pelo código da plataforma (regras de negócio).
     * Exceções de bibliotecas e frameworks podem expor detalhes internos (classes, consultas,
     * valores), e nesse caso é devolvido o texto padrão informado.
     */
    public static String detalheSeguro(Throwable ex, String detalhePadrao) {
        return lancadaPelaAplicacao(ex) && ex.getMessage() != null ? ex.getMessage() : detalhePadrao;
    }

    /**
     * Indica se a exceção foi criada no código da plataforma, a partir do primeiro quadro da pilha.
     */
    public static boolean lancadaPelaAplicacao(Throwable ex) {
        StackTraceElement[] pilha = ex.getStackTrace();
        return pilha.length > 0 && pilha[0].getClassName().startsWith(PACOTE_APLICACAO);
    }

    public static ProblemDetail criar(HttpStatusCode status, String titulo, String detalhe, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detalhe);
        problem.setTitle(titulo != null ? titulo : tituloPadrao(status));
        problem.setType(URI.create("about:blank"));
        return completar(problem, request.getRequestURI());
    }

    public static ProblemDetail completar(ProblemDetail problem, String path) {
        problem.setProperty("timestamp", OffsetDateTime.now().toString());
        problem.setProperty("path", path);
        return problem;
    }

    static String tituloPadrao(HttpStatusCode status) {
        HttpStatus httpStatus = HttpStatus.resolve(status.value());
        return httpStatus != null ? httpStatus.getReasonPhrase() : "Erro";
    }
}
