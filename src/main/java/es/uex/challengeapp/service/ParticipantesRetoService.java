package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

public interface ParticipantesRetoService {

	ParticipantesReto unirseAReto(Long usuarioId, Long retoId);
	
	List<Reto> obtenerRetosDeUsuario(Long usuarioId);
	
	List<Usuario> obteneParticipantesDeReto(Long retoId);
}
