package br.com.unicos.ms_cliente.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_cliente.enums.TipoObservacaoCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cliente_observacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClienteObservacao extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "titulo", length = 100)
    private String titulo;

    @Column(name = "descricao", nullable = false, length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoObservacaoCliente tipo;

}