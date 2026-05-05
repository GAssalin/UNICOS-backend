package br.com.unicos.ms_cliente.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_cliente.enums.StatusCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Cliente extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pessoa_id", nullable = false)
    private Long pessoaId;

    @Column(name = "filial_id")
    private Long filialId;

    @Column(name = "codigo_interno", length = 50)
    private String codigoInterno;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusCliente status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private ClienteCategoria categoria;

    @Column(name = "observacao_geral", length = 1000)
    private String observacaoGeral;

    @Column(name = "permite_venda_a_prazo", nullable = false)
    private Boolean permiteVendaAPrazo;

    @Column(name = "limite_credito", precision = 15, scale = 2)
    private BigDecimal limiteCredito;
}