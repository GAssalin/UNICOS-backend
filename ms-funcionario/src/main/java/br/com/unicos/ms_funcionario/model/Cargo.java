package br.com.unicos.ms_funcionario.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_funcionario.enums.PapelFuncionario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Cargo da empresa (ex.: "Vendedor Externo", "Gerente Comercial"). O nome é livre; o
 * {@link PapelFuncionario papel} define as regras aplicadas aos funcionários do cargo.
 */
@Entity
@Table(name = "cargos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Cargo extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false, length = 30)
    private PapelFuncionario papel;
}
