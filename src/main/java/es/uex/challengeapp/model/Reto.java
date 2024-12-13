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

	private Boolean visibilidad;

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

	@OneToMany(mappedBy = "reto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comentario> comentarios;

	@OneToMany(mappedBy = "reto")
	private List<ProgresoReto> progresoRetos;

	@OneToMany(mappedBy = "reto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParticipantesReto> participantes;

	public enum Estado {
		PENDIENTE, EN_PROGRESO, COMPLETADO, FALLIDO
	}

	public enum Tipo {
		SIMPLE, COMPLEJO
	}

	// GETTERS Y SETTERS
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getNovedad() {
		return novedad;
	}

	public void setNovedad(Boolean novedad) {
		this.novedad = novedad;
	}

	public Boolean getVisibilidad() {
		return visibilidad;
	}

	public void setVisibilidad(Boolean visibilidad) {
		this.visibilidad = visibilidad;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public Float getPorcentajeProgreso() {
		return porcentajeProgreso;
	}

	public void setPorcentajeProgreso(Float porcentajeProgreso) {
		this.porcentajeProgreso = porcentajeProgreso;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Usuario getCreador() {
		return creador;
	}

	public void setCreador(Usuario creador) {
		this.creador = creador;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public List<ProgresoReto> getProgresoRetos() {
		return progresoRetos;
	}

	public void setProgresoRetos(List<ProgresoReto> progresoRetos) {
		this.progresoRetos = progresoRetos;
	}

	public List<ParticipantesReto> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<ParticipantesReto> participantes) {
		this.participantes = participantes;
	}
}
