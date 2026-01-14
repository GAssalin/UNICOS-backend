package br.com.unicos.ms_filial.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Representa o horário de funcionamento de uma filial dentro do UniCoS.
 * <p>
 * No MVP do ms-filial, o horário é mantido como entidade separada para permitir
 * múltiplos registros por filial (um por dia da semana), sem complicar o agregado de Filial.
 */
@Entity
@Table(
        name = "horario_funcionamento_filial",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_horario_filial_dia",
                        columnNames = {"filial_id", "dia_semana"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HorarioFuncionamentoFilial extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da filial proprietária do horário.
     */
    @NotNull
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Dia da semana ao qual o horário se aplica.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false, length = 10)
    private DayOfWeek diaSemana;

    /**
     * Horário de abertura (null quando a filial não abre no dia).
     */
    @Column(name = "hora_abertura")
    private LocalTime horaAbertura;

    /**
     * Horário de fechamento (null quando a filial não abre no dia).
     */
    @Column(name = "hora_fechamento")
    private LocalTime horaFechamento;

    /**
     * Indica se a filial funciona neste dia da semana.
     */
    @NotNull
    @Column(name = "aberto", nullable = false)
    private Boolean aberto;
}
