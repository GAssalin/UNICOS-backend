package br.com.unicos.ms_ativos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que armazena o histórico de eventos relacionados ao ativo,
 * como alterações de status, transferências, manutenções e baixas.
 */
@Entity
@Table(name = "historico_ativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoAtivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ativo ao qual o histórico está vinculado.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    /**
     * Data e hora do evento registrado.
     */
    @PastOrPresent
    @Column(name = "data_evento", nullable = false)
    private LocalDateTime dataEvento;

    /**
     * Descrição do evento ocorrido (ex: “transferido para Filial B”).
     */
    @NotBlank
    @Column(name = "descricao_evento", nullable = false, length = 255)
    private String descricaoEvento;

    /**
     * Identificador do usuário responsável pelo evento (referência ao ms-pessoas).
     */
    @Column(name = "usuario_responsavel_id")
    private Long usuarioResponsavelId;
}
