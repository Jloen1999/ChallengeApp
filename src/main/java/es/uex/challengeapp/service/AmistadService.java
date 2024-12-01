package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Amistad;
import es.uex.challengeapp.model.Usuario;

public interface AmistadService {
	Amistad anadirAmigo(Amistad amistad);

	List<Usuario> obtenerAmigos(Integer id);

	void eliminarAmistad(Integer id, Integer id2);

}
