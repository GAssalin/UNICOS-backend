package br.com.unicos.ms_estoque.model;

import br.com.unicos.ms_estoque.enums.TipoTransacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa uma transação de movimentação no estoque.
 * Pode ser de entrada, saída, transferência ou ajuste.
 */
@Entity
@Table(name = "transacao_estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private TipoTransacao tipo; // ENTRADA, SAIDA, TRANSFERENCIA, AJUSTE

    @Column(length = 255)
    private String observacao;

    @Column(nullable = false)
    private LocalDateTime data = LocalDateTime.now();

    @Column(name = "usuario_responsavel")
    private String usuarioResponsavel;
}
