package es.uex.challengeapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.ParticipantesRetoRepository;

@Service
public class ParticipantesRetoServiceImpl implements ParticipantesRetoService {

	@Autowired
	private ParticipantesRetoRepository participantesRetoRepository;

	@Override
	public ParticipantesReto unirseAReto(ParticipantesReto participantesReto) {
		return participantesRetoRepository.save(participantesReto);
	}

	@Override
	public List<Reto> obtenerRetosDeUsuario(Long usuarioId) {
		return participantesRetoRepository.findRetosByUsuarioId(usuarioId);
	}

	@Override
	public List<Usuario> obteneParticipantesDeReto(Long retoId) {
		return participantesRetoRepository.findUsuariosByRetoId(retoId);
	}

	@Override
	public boolean unidoAlReto(Long usuarioId, Long retoId) {
		return participantesRetoRepository.existsByUsuarioIdAndRetoId(usuarioId, retoId);
	}

	@Override
	public List<Usuario> obtenerTodosLosUsuarios() {
		return participantesRetoRepository.findAllDistinctUsuarios();
	}

	@Override
	public ParticipantesReto obtenerParticipacionReto(Usuario usuario, Reto reto) {
		return participantesRetoRepository.findByUsuarioAndReto(usuario, reto).orElse(new ParticipantesReto());
	}

	@Override
	public List<Reto> obtenerRetosMasParticipantes() {
		List<Object[]> resultados = participantesRetoRepository.obtenerRetosMasParticipantes();
	    List<Reto> retos = new ArrayList<>();
	    
	    for (Object[] resultado : resultados) {
	        retos.add((Reto) resultado[0]);
	    }
	    
	    return retos.subList(0, Math.min(retos.size(), 5));
	}

	@Override
	public void desunirseReto(Usuario usuario, Reto reto) {
		ParticipantesReto participantesReto=obtenerParticipacionReto(usuario, reto);
		participantesRetoRepository.delete(participantesReto);
	}

	

}
