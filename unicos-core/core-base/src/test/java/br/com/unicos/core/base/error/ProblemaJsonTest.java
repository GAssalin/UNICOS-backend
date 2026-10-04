package br.com.unicos.core.base.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemaJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveGerarJsonValidoNoFormatoProblemDetail() throws Exception {
        JsonNode json = objectMapper.readTree(
                ProblemaJson.serializar(403, "Forbidden", "Sem permissão", "/v1/pessoas")
        );

        assertThat(json.get("type").asText()).isEqualTo("about:blank");
        assertThat(json.get("status").asInt()).isEqualTo(403);
        assertThat(json.get("title").asText()).isEqualTo("Forbidden");
        assertThat(json.get("detail").asText()).isEqualTo("Sem permissão");
        assertThat(json.get("path").asText()).isEqualTo("/v1/pessoas");
        assertThat(json.get("timestamp").asText()).isNotBlank();
    }

    @Test
    void deveEscaparCaracteresEspeciais() throws Exception {
        String pathMalicioso = "/v1/x\",\"status\":200,\"a\":\"\n";

        JsonNode json = objectMapper.readTree(
                ProblemaJson.serializar(401, "Unauthorized", null, pathMalicioso)
        );

        assertThat(json.get("status").asInt()).isEqualTo(401);
        assertThat(json.get("path").asText()).isEqualTo(pathMalicioso);
        assertThat(json.get("detail").isNull()).isTrue();
    }
}
