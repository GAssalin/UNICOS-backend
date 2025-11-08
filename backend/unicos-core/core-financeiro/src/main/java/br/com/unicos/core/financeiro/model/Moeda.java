package br.com.unicos.core.financeiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma moeda utilizada nas operações financeiras.
 * <p>
 * Armazena informações básicas como código ISO, símbolo e nome,
 * sendo utilizada para identificar a moeda em lançamentos e contas.
 */
@Entity
@Table(name = "moeda")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moeda {

    /**
     * Identificador único da moeda.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código ISO da moeda (ex: BRL, USD, EUR).
     */
    @NotBlank
    @Column(nullable = false, length = 3, unique = true)
    private String codigoIso;

    /**
     * Símbolo representativo da moeda (ex: R$, $, €).
     */
    @NotBlank
    @Column(nullable = false, length = 5)
    private String simbolo;

    /**
     * Nome completo da moeda (ex: Real Brasileiro, Dólar Americano).
     */
    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;
}
