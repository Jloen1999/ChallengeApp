package es.uex.challengeapp.model;

import java.util.Date;

import jakarta.persistence.*;

@Entity
public class ParticipantesReto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date fechaUnion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "reto_id")
    private Reto reto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaUnion() {
		return fechaUnion;
	}

	public void setFechaUnion(Date fechaUnion) {
		this.fechaUnion = fechaUnion;
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
