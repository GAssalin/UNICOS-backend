package br.com.unicos.ms_funcionario.model;

import br.com.unicos.core.funcionario.enums.EscopoCarteira;
import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_funcionario.enums.StatusFuncionario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Funcionário da empresa.
 *
 * <p>
 * {@code pessoaId} e {@code usuarioId} referenciam logicamente o ms-pessoas e {@code filialId}
 * o ms-empresa, sem FK entre serviços. O usuário é opcional: apenas funcionários que acessam o
 * sistema (como os vendedores) precisam dele.
 * </p>
 */
@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Funcionario extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pessoa_id", nullable = false)
    private Long pessoaId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "matricula", length = 30)
    private String matricula;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    /**
     * Superior imediato na hierarquia (outro funcionário da mesma empresa).
     */
    @Column(name = "superior_id")
    private Long superiorId;

    @Column(name = "filial_id")
    private Long filialId;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "data_desligamento")
    private LocalDate dataDesligamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusFuncionario status;

    public boolean isDesligado() {
        return status == StatusFuncionario.DESLIGADO;
    }

    /**
     * Clientes que o funcionário pode acessar, conforme o papel do cargo. Um funcionário desligado
     * perde a visão gerencial e fica restrito, no máximo, à própria carteira.
     */
    public EscopoCarteira getEscopoCarteira() {
        return isDesligado() ? EscopoCarteira.PROPRIA : cargo.getPapel().getEscopoCarteira();
    }
}
