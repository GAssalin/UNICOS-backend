package br.com.unicos.ms_empresa.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de contato institucional da empresa.
 * <p>
 * Permite categorizar os canais oficiais de comunicação
 * utilizados para atendimento, notificações e suporte.
 */
@Getter
public enum TipoContatoEmpresa {

    /**
     * Telefone fixo ou comercial da empresa.
     */
    TELEFONE("Telefone"),

    /**
     * Endereço de e-mail institucional.
     */
    EMAIL("E-mail"),

    /**
     * Número de WhatsApp institucional.
     */
    WHATSAPP("WhatsApp");

    private final String descricao;

    TipoContatoEmpresa(String descricao) {
        this.descricao = descricao;
    }
}
