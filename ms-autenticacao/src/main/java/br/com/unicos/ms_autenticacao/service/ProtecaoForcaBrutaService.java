package br.com.unicos.ms_autenticacao.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limita tentativas de login com senha incorreta por e-mail.
 *
 * <p>
 * Após {@code maxTentativas} falhas dentro da janela, novas tentativas para o mesmo e-mail são
 * recusadas com {@code 429} até o fim do bloqueio, mesmo com a senha correta. O controle vale
 * também para e-mails inexistentes, para não revelar quais estão cadastrados.
 * </p>
 *
 * <p>
 * O estado fica em memória e é por instância: com várias réplicas do ms-autenticacao, prefira um
 * limitador compartilhado (por exemplo, no gateway com Redis).
 * </p>
 */
@Slf4j
@Service
public class ProtecaoForcaBrutaService {

    /**
     * Limite de e-mails monitorados, evitando crescimento indefinido da memória.
     */
    static final int MAX_EMAILS_MONITORADOS = 100_000;

    private final int maxTentativas;
    private final Duration bloqueio;
    private final Clock clock;
    private final Map<String, Tentativas> tentativas = new ConcurrentHashMap<>();

    @Autowired
    public ProtecaoForcaBrutaService(
            @Value("${unicos.login.max-tentativas:5}") int maxTentativas,
            @Value("${unicos.login.bloqueio:15m}") Duration bloqueio
    ) {
        this(maxTentativas, bloqueio, Clock.systemUTC());
    }

    ProtecaoForcaBrutaService(int maxTentativas, Duration bloqueio, Clock clock) {
        if (maxTentativas <= 0 || bloqueio.isNegative() || bloqueio.isZero())
            throw new IllegalStateException("Configuração inválida da proteção de login.");

        this.maxTentativas = maxTentativas;
        this.bloqueio = bloqueio;
        this.clock = clock;
    }

    /**
     * @throws ResponseStatusException {@code 429} quando o e-mail está temporariamente bloqueado
     */
    public void verificarBloqueio(String email) {
        Tentativas atual = tentativas.get(chave(email));

        if (atual != null && atual.bloqueadoAte() != null && atual.bloqueadoAte().isAfter(clock.instant()))
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Muitas tentativas de login. Tente novamente mais tarde.");
    }

    public void registrarFalha(String email) {
        Instant agora = clock.instant();

        if (tentativas.size() >= MAX_EMAILS_MONITORADOS)
            removerExpiradas(agora);

        Tentativas resultado = tentativas.compute(chave(email), (chave, atual) -> {
            int falhas = atual == null || atual.expiraEm().isBefore(agora) ? 1 : atual.falhas() + 1;
            Instant bloqueadoAte = falhas >= maxTentativas ? agora.plus(bloqueio) : null;
            return new Tentativas(falhas, agora.plus(bloqueio), bloqueadoAte);
        });

        if (resultado.falhas() == maxTentativas)
            log.warn("Login bloqueado temporariamente após {} tentativas inválidas.", maxTentativas);
    }

    public void registrarSucesso(String email) {
        tentativas.remove(chave(email));
    }

    private void removerExpiradas(Instant agora) {
        tentativas.entrySet().removeIf(entry -> entry.getValue().expiraEm().isBefore(agora));
    }

    private static String chave(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * @param falhas       falhas consecutivas dentro da janela
     * @param expiraEm     fim da janela (renovada a cada falha)
     * @param bloqueadoAte fim do bloqueio, quando o limite foi atingido
     */
    private record Tentativas(int falhas, Instant expiraEm, Instant bloqueadoAte) {
    }
}
