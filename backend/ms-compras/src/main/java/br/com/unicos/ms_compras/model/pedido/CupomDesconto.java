package br.com.unicos.ms_compras.model.pedido;

import br.com.unicos.ms_pedido.enums.TipoDesconto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa cupons de desconto aplicáveis aos pedidos.
 */
@Entity
@Table(name = "cupom_desconto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CupomDesconto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, unique = true, nullable = false)
    private String codigo;

    @Enumerated(EnumType.STRING)
    private TipoDesconto tipo;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorDesconto;

    private LocalDate dataValidade;

    private Boolean ativo;
}
