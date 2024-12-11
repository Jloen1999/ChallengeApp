package es.uex.challengeapp.model;

public class UsuarioDTO {
	private Integer id;
	private String nombre;
	private String correo;

	public UsuarioDTO(Integer id, String nombre, String correo) {
		this.id = id;
		this.nombre = nombre;
		this.correo = correo;
	}

	// Getters y Setters
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
}
