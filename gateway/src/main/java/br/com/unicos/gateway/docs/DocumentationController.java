package br.com.unicos.gateway.docs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Portal com links para a documentação OpenAPI de cada microserviço registrado no Eureka.
 *
 * <p>
 * Os links apontam para os endereços internos das instâncias, portanto o portal só é útil em
 * ambientes onde essa rede é acessível. Desabilite com {@code unicos.docs.enabled=false}
 * (padrão no profile {@code prod}).
 * </p>
 */
@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "unicos.docs", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DocumentationController {

    private static final List<String> SERVICOS = List.of(
            "ms-autenticacao",
            "ms-permissao",
            "ms-pessoas",
            "ms-empresa",
            "ms-estoque",
            "ms-produto",
            "ms-cliente"
    );

    private final DiscoveryClient discoveryClient;

    @GetMapping(value = "/docs", produces = MediaType.TEXT_HTML_VALUE)
    public String documentationPortal() {
        StringBuilder cards = new StringBuilder();

        for (String servico : SERVICOS) {
            String url = resolveServiceUrl(servico);
            String nome = HtmlUtils.htmlEscape(servico.toUpperCase());

            if (url == null) {
                cards.append("""
                        <div class="card">
                          <strong>%s</strong><br/>
                          <span class="off">Serviço não registrado no Eureka</span>
                        </div>
                        """.formatted(nome));
                continue;
            }

            String href = HtmlUtils.htmlEscape(url);
            cards.append("""
                    <div class="card">
                      <strong>%s</strong><br/>
                      <a href="%s/swagger-ui/index.html" target="_blank" rel="noopener">Swagger UI</a><br/>
                      <a href="%s/v3/api-docs" target="_blank" rel="noopener">OpenAPI JSON</a>
                    </div>
                    """.formatted(nome, href, href));
        }

        return """
                <!doctype html>
                <html lang="pt-BR">
                  <head>
                    <meta charset="utf-8"/>
                    <title>UniCoS - Portal de Documentação</title>
                    <style>
                        body { font-family: Arial, sans-serif; background: #111; color: #eee; padding: 40px; }
                        h1 { color: #4caf50; }
                        .card { background: #1d1d1d; padding: 20px; border-radius: 8px; margin: 15px 0; border: 1px solid #333; }
                        a { color: #76baff; font-size: 18px; text-decoration: none; }
                        a:hover { color: #a5cdff; }
                        .off { color: #999; }
                    </style>
                  </head>
                  <body>
                    <h1>UniCoS - Portal de Documentação</h1>
                    <p>Acesse abaixo a documentação dos microserviços:</p>
                    %s
                  </body>
                </html>
                """.formatted(cards);
    }

    private String resolveServiceUrl(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (instances == null || instances.isEmpty())
            return null;
        return instances.getFirst().getUri().toString();
    }
}
