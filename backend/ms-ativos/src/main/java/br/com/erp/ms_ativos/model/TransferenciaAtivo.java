package br.com.erp.ms_ativos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transferencia_ativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaAtivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    @NotNull
    @Column(name = "origem_id", nullable = false)
    private Long origemId;

    @NotNull
    @Column(name = "destino_id", nullable = false)
    private Long destinoId;

    @PastOrPresent
    @Column(name = "data_transferencia", nullable = false)
    private LocalDate dataTransferencia;

    @Column(length = 255)
    private String motivo;
}