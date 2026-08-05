package br.com.unicos.ms_pessoas.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de endereço associados a uma pessoa.
 * <p>
 * Permite classificação entre diferentes finalidades e usos,
 * trazendo clareza ao relacionamento da pessoa com seus endereços cadastrados.
 */
@Getter
public enum TipoEndereco {

    /**
     * Endereço residencial utilizado para correspondência pessoal.
     */
    RESIDENCIAL("Residencial"),

    /**
     * Endereço comercial vinculado a atividades profissionais.
     */
    COMERCIAL("Comercial"),

    /**
     * Endereço alternativo utilizado para correspondências ou contatos eventuais.
     */
    ENTREGA("Endereço para entrega");

    private final String descricao;

    TipoEndereco(String descricao) {
        this.descricao = descricao;
    }
}
