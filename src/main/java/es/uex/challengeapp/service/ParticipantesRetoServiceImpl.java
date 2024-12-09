package es.uex.challengeapp.service;

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

}
