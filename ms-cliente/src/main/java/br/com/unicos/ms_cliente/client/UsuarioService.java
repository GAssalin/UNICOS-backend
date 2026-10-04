package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.usuario.dto.UsuarioResumoResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioClient usuarioClient;

    /**
     * Nome de exibição do usuário. Informação acessória: falhas na consulta não impedem a
     * resposta principal e resultam em {@code null}.
     */
    @CircuitBreaker(name = "ms-pessoas", fallbackMethod = "fallbackBuscarNome")
    public String buscarNome(Long idUsuario) {
        UsuarioResumoResponse resumo = usuarioClient.buscarResumo(idUsuario);
        return resumo != null ? resumo.nome() : null;
    }

    private String fallbackBuscarNome(Long idUsuario, Throwable ex) {
        log.warn("Não foi possível obter o nome do usuário [{}] no ms-pessoas: {}", idUsuario, ex.getMessage());
        return null;
    }
}
