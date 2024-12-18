package es.uex.challengeapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Subtarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaFinalizada;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "reto_complejo_id")
    private RetoComplejo retoComplejo;

    public enum Estado {
        PENDIENTE, EN_PROGRESO, COMPLETADA
    }

    // Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaFinalizada() {
		return fechaFinalizada;
	}

	public void setFechaFinalizada(Date fechaFinalizada) {
		this.fechaFinalizada = fechaFinalizada;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public RetoComplejo getRetoComplejo() {
		return retoComplejo;
	}

	public void setRetoComplejo(RetoComplejo retoComplejo) {
		this.retoComplejo = retoComplejo;
	}
}
