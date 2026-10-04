package br.com.unicos.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ContextoGatewayTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void deveBloquearEndpointsInternosDosServicos() {
        webTestClient.get().uri("/ms-pessoas/internal/auth/by-email?email=admin@unicos.com")
                .header("Authorization", "Bearer qualquer")
                .exchange()
                .expectStatus().isNotFound();

        webTestClient.get().uri("/ms-produto/actuator/health")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deveExigirTokenNasRotasProtegidas() {
        webTestClient.get().uri("/ms-pessoas/v1/pessoas")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON);
    }

    @Test
    void deveResponderHealthDoProprioGateway() {
        webTestClient.get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk();
    }
}
