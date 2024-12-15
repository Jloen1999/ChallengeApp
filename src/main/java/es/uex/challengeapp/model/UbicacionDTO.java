package es.uex.challengeapp.model;

public class UbicacionDTO {
	private String id;
	private String nombre;

	public UbicacionDTO(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	// Getters y setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
