package es.uex.challengeapp.model;

import jakarta.persistence.Entity;

@Entity
public class RetoSimple extends Reto {
    private int progreso;

    public int getProgresoArray() {
        return progreso;
    }

    public void setProgresoArray(int progreso) {
        this.progreso = progreso;
    }
}
