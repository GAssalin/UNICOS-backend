package br.com.unicos.ms_ativos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidade que representa a localização física de um ativo dentro de uma unidade (filial).
 * <p>
 * Exemplo: Bloco A, 2º Andar, Sala 204.
 * Cada localização pode conter diversos ativos associados.
 */
@Entity
@Table(name = "localizacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descrição da localização (ex: "Sala de Servidores", "Depósito Central").
     */
    @NotBlank
    @Column(nullable = false, length = 100)
    private String descricao;

    /**
     * Identificação do andar (ex: "Térreo", "1º", "2º").
     */
    @Column(length = 10)
    private String andar;

    /**
     * Identificação do bloco ou prédio (ex: "A", "B", "Administrativo").
     */
    @Column(length = 20)
    private String bloco;

    /**
     * Identificador da filial onde a localização pertence (referência ao ms-empresa).
     */
    @NotNull
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    // ===========================================================
    // 🔗 RELACIONAMENTOS
    // ===========================================================

    /**
     * Lista de ativos que estão fisicamente nesta localização.
     */
    @OneToMany(mappedBy = "localizacao", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Ativo> ativos;
}
