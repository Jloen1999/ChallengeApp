package es.uex.challengeapp.model;

import jakarta.persistence.*;

@Entity
public class Recompensa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoMedalla tipo;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public enum TipoMedalla {
		BRONCE, PLATA, ORO, DIAMANTE
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
