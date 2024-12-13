package es.uex.challengeapp.model;

import jakarta.persistence.*;

@Entity
public class Recompensa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String descripcion;
	private TipoMedalla tipo;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public enum TipoMedalla {
		BRONCE, PLATA, ORO, DIAMANTE
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoMedalla getTipo() {
		return tipo;
	}

	public void setTipo(TipoMedalla tipo) {
		this.tipo = tipo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
