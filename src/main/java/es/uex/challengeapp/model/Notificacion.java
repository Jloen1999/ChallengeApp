package es.uex.challengeapp.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Notificacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String mensaje;
	private Boolean leido;
	private Date fechaEnvio;
	
	@Enumerated(EnumType.STRING)
	private TipoNotificacion tipoNotificacion;

	@ManyToOne // Muchas notificaciones pueden ser enviadas por un usuario
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@ManyToOne // El usuario puede recibir muchas notificaciones de un reto
	@JoinColumn(name = "reto_id")
	private Reto reto;

	public enum TipoNotificacion {
		SOLICITUD_AMISTAD, ACEPTACION_AMISTAD, CREACION_RETO, UNION_RETO, ELIMINACION_AMISTAD
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Boolean getLeido() {
		return leido;
	}

	public void setLeido(Boolean leido) {
		this.leido = leido;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Reto getReto() {
		return reto;
	}

	public void setReto(Reto reto) {
		this.reto = reto;
	}

	public TipoNotificacion getTipoNotificacion() {
		return tipoNotificacion;
	}

	public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
		this.tipoNotificacion = tipoNotificacion;
	}
}
