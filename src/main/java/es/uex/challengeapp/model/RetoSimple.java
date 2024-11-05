package es.uex.challengeapp.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
public class RetoSimple extends Reto {
    @Lob
    private JsonNode progresoArray;

    public JsonNode getProgresoArray() {
        return progresoArray;
    }

    public void setProgresoArray(JsonNode progresoArray) {
        this.progresoArray = progresoArray;
    }
}
