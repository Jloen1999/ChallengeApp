package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Amistad;
import es.uex.challengeapp.model.Usuario;

public interface AmistadService {
	Amistad anadirAmigo(Amistad amistad);

	List<Usuario> obtenerAmigos(Long id);

	void eliminarAmistad(Long id, Long id2);
	
	boolean sonAmigos(Usuario usuario1, Usuario usuario2);
}
