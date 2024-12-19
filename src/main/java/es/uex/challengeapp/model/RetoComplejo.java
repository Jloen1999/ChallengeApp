package es.uex.challengeapp.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class RetoComplejo extends Reto {
    private int subtareaActual;

    @OneToMany(mappedBy = "retoComplejo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subtarea> subtareas;

    public int getSubtareaActual() {
        return subtareaActual;
    }

    public void setSubtareaActual(int subtareaActual) {
        this.subtareaActual = subtareaActual;
    }

    public List<Subtarea> getSubtareas() {
        return subtareas;
    }

    public void setSubtareas(List<Subtarea> subtareas) {
        this.subtareas = subtareas;
    }
}
