package br.com.erp.ms_pagamento.model;

import br.com.erp.ms_pagamento.enums.TipoFormaPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma forma de pagamento aceita pela empresa.
 */
@Entity
@Table(name = "forma_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição da forma de pagamento é obrigatória.")
    @Column(nullable = false, length = 100)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de forma de pagamento é obrigatório.")
    @Column(nullable = false, length = 30)
    private TipoFormaPagamento tipo;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;
}