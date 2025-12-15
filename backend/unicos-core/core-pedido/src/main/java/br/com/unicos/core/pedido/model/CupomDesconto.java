package br.com.unicos.core.pedido.model;

import br.com.unicos.core.pedido.enums.TipoDesconto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa um cupom de desconto aplicável a pedidos.
 *
 * <p>
 * Define o tipo de desconto (percentual ou fixo), o valor e o período de validade.
 * </p>
 */
@Entity
@Table(name = "cupom_desconto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CupomDesconto {

    /**
     * Identificador único do cupom.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código único do cupom utilizado para validação.
     */
    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    /**
     * Tipo de desconto (percentual ou fixo).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoDesconto tipo;

    /**
     * Valor do desconto aplicado.
     */
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    /**
     * Data de início da validade do cupom.
     */
    private LocalDateTime inicioVigencia;

    /**
     * Data de término da validade do cupom.
     */
    private LocalDateTime fimVigencia;

    /**
     * Indica se o cupom ainda está ativo.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
