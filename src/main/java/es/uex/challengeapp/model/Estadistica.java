package es.uex.challengeapp.model;

import jakarta.persistence.*;

@Entity
public class Estadistica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer totalRetos;
    private Integer retosCompletados;
    private Integer retosFallidos;
    private Float progresoPromedio;
    private Float tiempoPromedio;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalRetos() {
        return totalRetos;
    }

    public void setTotalRetos(Integer totalRetos) {
        this.totalRetos = totalRetos;
    }

    public Integer getRetosCompletados() {
        return retosCompletados;
    }

    public void setRetosCompletados(Integer retosCompletados) {
        this.retosCompletados = retosCompletados;
    }

    public Integer getRetosFallidos() {
        return retosFallidos;
    }

    public void setRetosFallidos(Integer retosFallidos) {
        this.retosFallidos = retosFallidos;
    }

    public Float getProgresoPromedio() {
        return progresoPromedio;
    }

    public void setProgresoPromedio(Float progresoPromedio) {
        this.progresoPromedio = progresoPromedio;
    }

    public Float getTiempoPromedio() {
        return tiempoPromedio;
    }

    public void setTiempoPromedio(Float tiempoPromedio) {
        this.tiempoPromedio = tiempoPromedio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

