package br.com.unicos.core.pessoas.exception;

public class PessoaNotFoundException extends RuntimeException {
    public PessoaNotFoundException() {
        super("Pessoa não encontrada");
    }
}
