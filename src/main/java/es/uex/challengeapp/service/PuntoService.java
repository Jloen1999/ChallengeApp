package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Punto;
import es.uex.challengeapp.model.Usuario;

public interface PuntoService {

	Punto registrarPuntos(Punto punto);

	int totalPuntosUsuario(Usuario usuario);
	
	List<Punto> obtenerPuntosUsuario(Usuario usuario);

}
