package br.com.unicos.core.produto.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de armazenamento necessários para um produto.
 *
 * <p>
 * Define as condições físicas ou ambientais necessárias para estocagem adequada,
 * garantindo a integridade, segurança e conformidade legal do item.
 * </p>
 */
@Getter
public enum TipoArmazenamentoProduto {

    /**
     * Produto que não exige condições especiais de armazenamento.
     */
    NORMAL("Normal"),

    /**
     * Produto que requer refrigeração acima de 0°C e abaixo de 10°C.
     */
    REFRIGERADO("Refrigerado"),

    /**
     * Produto que deve ser armazenado em temperaturas negativas.
     */
    CONGELADO("Congelado"),

    /**
     * Produto sujeito a controle rígido por normas sanitárias ou legais.
     */
    CONTROLADO("Controlado"),

    /**
     * Produto inflamável ou que apresenta riscos químicos.
     */
    INFLAMAVEL("Inflamável"),

    /**
     * Produto classificado como químico e que necessita condições específicas.
     */
    QUIMICO("Químico");

    private final String descricao;

    TipoArmazenamentoProduto(String descricao) {
        this.descricao = descricao;
    }
}
