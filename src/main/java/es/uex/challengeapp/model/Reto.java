package es.uex.challengeapp.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Reto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String descripcion;
    private int duracion;
    private String url;
    private Boolean novedad;
    private String visibilidad;
    private Date fechaCreacion;
    private Date fechaFinalizacion;
    private Float porcentajeProgreso;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "creador_id")
    private Usuario creador;

    @OneToMany(mappedBy = "reto")
    private List<Comentario> comentarios;

    @OneToMany(mappedBy = "reto")
    private List<Recompensa> recompensas;

    @OneToMany(mappedBy = "reto")
    private List<ProgresoReto> progresoRetos;

    @OneToMany(mappedBy = "reto")
    private List<ParticipantesReto> participantes;

    // Getters and setters

    public enum Estado {
        PENDIENTE, EN_PROGRESO, COMPLETADO, FALLIDO
    }

    public enum Tipo {
        SIMPLE, COMPLEJO
    }
}
