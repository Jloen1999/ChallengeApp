package es.uex.challengeapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.ParticipantesRetoRepository;
import es.uex.challengeapp.repository.RetoRepository;
import es.uex.challengeapp.repository.UsuarioRepository;

@Service
public class ParticipantesRetoServiceImpl implements ParticipantesRetoService {
	
	@Autowired
	private ParticipantesRetoRepository participantesRetoRepository;
	
	@Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RetoRepository retoRepository;
    
    
    @Override
    public ParticipantesReto unirseAReto(Long usuarioId, Long retoId) {
    	Usuario usuario=usuarioRepository.findById(usuarioId).orElse(null);
    	Reto reto=retoRepository.findById(retoId).orElse(null);
    	
    	ParticipantesReto participantesReto=new ParticipantesReto();
    	
    	participantesReto.setUsuario(usuario);
    	participantesReto.setReto(reto);
    	
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

}
