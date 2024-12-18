package es.uex.challengeapp.model;

public class RetoDTO {
    private Long id;
    private String nombre;
    private String descripcion;

    // Constructor
    public RetoDTO(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

