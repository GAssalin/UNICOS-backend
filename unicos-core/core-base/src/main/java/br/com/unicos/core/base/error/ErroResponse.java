package br.com.unicos.core.base.error;

public record ErroResponse(
        int status,
        String erro,
        String mensagem,
        String path
) { }