package es.uex.challengeapp.model;

import java.io.Serializable;
import java.util.Objects;


//ESTA CLASE ES NECESARIA PORQUE EN AMISTAD HAY DOS CAMPOS CON LA ETIQUETA ID
public class AmistadId implements Serializable {
    private int usuario1;
    private int usuario2;

    // Getters, setters, equals y hashCode
    public int getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(int usuario1) {
        this.usuario1 = usuario1;
    }

    public int getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(int usuario2) {
        this.usuario2 = usuario2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmistadId amistadId = (AmistadId) o;
        return Objects.equals(usuario1, amistadId.usuario1) &&
               Objects.equals(usuario2, amistadId.usuario2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario1, usuario2);
    }
}

