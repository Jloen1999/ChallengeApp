package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

public interface ParticipantesRetoService {

	ParticipantesReto unirseAReto(ParticipantesReto participantesReto);
	
	List<Reto> obtenerRetosDeUsuario(Long usuarioId);
	
	List<Usuario> obteneParticipantesDeReto(Long retoId);
	
	boolean unidoAlReto(Long usuarioId, Long retoId);

	List<Usuario> obtenerTodosLosUsuarios();

	ParticipantesReto obtenerParticipacionReto(Usuario usuario, Reto reto);

	List<Reto> obtenerRetosMasParticipantes();

	void desunirseReto(Usuario usuario, Reto reto);
}
