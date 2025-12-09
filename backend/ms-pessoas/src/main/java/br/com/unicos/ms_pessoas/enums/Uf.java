package br.com.unicos.ms_pessoas.enums;

import lombok.Getter;

/**
 * Enum que representa todas as Unidades Federativas (UFs) do Brasil.
 * <p>
 * Utilizado para padronizar o armazenamento de endereços e garantir
 * integridade em operações que envolvem localização geográfica.
 */
@Getter
public enum Uf {

    AC("Acre"),
    AL("Alagoas"),
    AP("Amapá"),
    AM("Amazonas"),
    BA("Bahia"),
    CE("Ceará"),
    DF("Distrito Federal"),
    ES("Espírito Santo"),
    GO("Goiás"),
    MA("Maranhão"),
    MT("Mato Grosso"),
    MS("Mato Grosso do Sul"),
    MG("Minas Gerais"),
    PA("Pará"),
    PB("Paraíba"),
    PR("Paraná"),
    PE("Pernambuco"),
    PI("Piauí"),
    RJ("Rio de Janeiro"),
    RN("Rio Grande do Norte"),
    RS("Rio Grande do Sul"),
    RO("Rondônia"),
    RR("Roraima"),
    SC("Santa Catarina"),
    SP("São Paulo"),
    SE("Sergipe"),
    TO("Tocantins");

    private final String descricao;

    Uf(String descricao) {
        this.descricao = descricao;
    }
}
