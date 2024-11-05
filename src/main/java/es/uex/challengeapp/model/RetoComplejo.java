package es.uex.challengeapp.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class RetoComplejo extends Reto {
    private String subtareaActual;

    @OneToMany(mappedBy = "retoComplejo")
    private List<Subtarea> subtareas;

    public String getSubtareaActual() {
        return subtareaActual;
    }

    public void setSubtareaActual(String subtareaActual) {
        this.subtareaActual = subtareaActual;
    }

    public List<Subtarea> getSubtareas() {
        return subtareas;
    }

    public void setSubtareas(List<Subtarea> subtareas) {
        this.subtareas = subtareas;
    }
}
