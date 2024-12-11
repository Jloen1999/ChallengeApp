package es.uex.challengeapp.model;

import jakarta.persistence.Entity;

@Entity
public class RetoSimple extends Reto {
    private int progreso;
    private int cantidad;

    public int getProgresoArray() {
        return progreso;
    }

    public void setProgresoArray(int progreso) {
        this.progreso = progreso;
    }

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
