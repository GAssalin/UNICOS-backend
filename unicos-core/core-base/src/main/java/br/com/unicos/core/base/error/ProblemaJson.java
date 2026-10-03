package br.com.unicos.core.base.error;

import java.time.OffsetDateTime;

/**
 * Serializa respostas de erro no formato <a href="https://www.rfc-editor.org/rfc/rfc9457">Problem Details</a>
 * (o mesmo produzido pelos {@code @RestControllerAdvice} com {@code ProblemDetail}).
 *
 * <p>
 * Utilizado em pontos onde o Jackson/Spring MVC não participa da resposta, como filtros
 * servlet e o gateway reativo, garantindo o mesmo contrato de erro em toda a plataforma.
 * </p>
 */
public final class ProblemaJson {

    public static final String CONTENT_TYPE = "application/problem+json";

    private ProblemaJson() {
    }

    public static String serializar(int status, String titulo, String detalhe, String path) {
        return "{"
                + "\"type\":\"about:blank\","
                + "\"title\":" + texto(titulo) + ","
                + "\"status\":" + status + ","
                + "\"detail\":" + texto(detalhe) + ","
                + "\"instance\":" + texto(path) + ","
                + "\"timestamp\":" + texto(OffsetDateTime.now().toString()) + ","
                + "\"path\":" + texto(path)
                + "}";
    }

    private static String texto(String valor) {
        if (valor == null)
            return "null";

        StringBuilder sb = new StringBuilder(valor.length() + 2).append('"');
        for (int i = 0; i < valor.length(); i++) {
            char c = valor.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                default -> {
                    if (c < 0x20)
                        sb.append(String.format("\\u%04x", (int) c));
                    else
                        sb.append(c);
                }
            }
        }
        return sb.append('"').toString();
    }
}
