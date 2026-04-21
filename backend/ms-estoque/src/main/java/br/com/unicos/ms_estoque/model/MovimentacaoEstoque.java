package br.com.unicos.ms_estoque.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_estoque.enums.StatusMovimentacaoEstoque;
import br.com.unicos.ms_estoque.enums.TipoMovimentacaoEstoque;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MovimentacaoEstoque extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false, length = 30)
    private TipoMovimentacaoEstoque tipoMovimentacao;

    @Column(name = "estoque_origem_id")
    private Long estoqueOrigemId;

    @Column(name = "estoque_destino_id")
    private Long estoqueDestinoId;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @Column(name = "documento_referencia", length = 100)
    private String documentoReferencia;

    @Column(name = "usuario_responsavel_id")
    private Long usuarioResponsavelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_movimentacao", nullable = false, length = 20)
    private StatusMovimentacaoEstoque statusMovimentacao;
}