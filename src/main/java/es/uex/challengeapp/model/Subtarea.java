package es.uex.challengeapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Subtarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaFinalizada;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "reto_complejo_id")
    private RetoComplejo retoComplejo;

    // Getters and setters

    public enum Estado {
        PENDIENTE, EN_PROGRESO, COMPLETADA
    }
}
