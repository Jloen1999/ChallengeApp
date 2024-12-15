package es.uex.challengeapp.model;

public class RetoDTO {
    private Integer id;
    private String nombre;
    private String descripcion;

    // Constructor
    public RetoDTO(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

