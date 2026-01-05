package br.com.unicos.gateway.docs;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentationController {

    private final DiscoveryClient discoveryClient;

    @GetMapping("/docs")
    public String documentationPortal() {

        String authUrl = resolveServiceUrl("ms-auth");
        String produtosUrl = resolveServiceUrl("ms-produtos");
        String pessoasUrl = resolveServiceUrl("ms-pessoas");
        String empresaUrl = resolveServiceUrl("ms-empresa");
        String usuarioUrl = resolveServiceUrl("ms-usuario");

        return """
                <html>
                  <head>
                    <title>UniCoS - Portal de Documentação</title>
                    <style>
                        body { font-family: Arial, sans-serif; background: #111; color: #eee; padding: 40px; }
                        h1 { color: #4caf50; }
                        .card {
                            background: #1d1d1d;
                            padding: 20px;
                            border-radius: 8px;
                            margin: 15px 0;
                            border: 1px solid #333;
                        }
                        a {
                            color: #76baff;
                            font-size: 18px;
                            text-decoration: none;
                        }
                        a:hover { color: #a5cdff; }
                    </style>
                  </head>
                  <body>
                    <h1>UniCoS - Portal de Documentação</h1>
                    <p>Acesse abaixo a documentação dos microserviços:</p>

                    <div class="card">
                      <strong>MS-AUTH</strong><br/>
                      <a href="%s/swagger-ui/index.html" target="_blank">Swagger UI</a><br/>
                      <a href="%s/v3/api-docs" target="_blank">OpenAPI JSON</a>
                    </div>
                    
                    <div class="card">
                      <strong>MS-USUARIO</strong><br/>
                      <a href="%s/swagger-ui/index.html" target="_blank">Swagger UI</a><br/>
                      <a href="%s/v3/api-docs" target="_blank">OpenAPI JSON</a>
                    </div>
                    
                    <div class="card">
                      <strong>MS-EMPRESA</strong><br/>
                      <a href="%s/swagger-ui/index.html" target="_blank">Swagger UI</a><br/>
                      <a href="%s/v3/api-docs" target="_blank">OpenAPI JSON</a>
                    </div>

                    <div class="card">
                      <strong>MS-PRODUTOS</strong><br/>
                      <a href="%s/swagger-ui/index.html" target="_blank">Swagger UI</a><br/>
                      <a href="%s/v3/api-docs" target="_blank">OpenAPI JSON</a>
                    </div>

                    <div class="card">
                      <strong>MS-PESSOAS</strong><br/>
                      <a href="%s/swagger-ui/index.html" target="_blank">Swagger UI</a><br/>
                      <a href="%s/v3/api-docs" target="_blank">OpenAPI JSON</a>
                    </div>

                  </body>
                </html>
                """.formatted(authUrl, authUrl, usuarioUrl, usuarioUrl, empresaUrl, empresaUrl, produtosUrl, produtosUrl, pessoasUrl, pessoasUrl);
    }

    private String resolveServiceUrl(String serviceId) {
        var instances = discoveryClient.getInstances(serviceId);
        if (instances == null || instances.isEmpty())
            return "http://service-not-found";
        return instances.get(0).getUri().toString();
    }
}
