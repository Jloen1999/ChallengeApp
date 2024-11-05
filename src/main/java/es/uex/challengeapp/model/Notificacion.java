package es.uex.challengeapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String mensaje;
    private Boolean leido;
    private Date fechaEnvio;

    @ManyToOne // Muchas notificaciones pueden ser enviadas por un usuario
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne // El usuario puede recibir muchas notificaciones de un reto
    @JoinColumn(name = "reto_id")
    private Reto reto;

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
}

