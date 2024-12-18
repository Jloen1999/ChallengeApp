package es.uex.challengeapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Amistad;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.AmistadRepository;
import jakarta.transaction.Transactional;

@Service
public class AmistadServiceImpl implements AmistadService {

	@Autowired
	private AmistadRepository amistadRepository;

	@Override
	public Amistad anadirAmigo(Amistad amistad) {
		return amistadRepository.save(amistad);
	}

	@Override
	public List<Usuario> obtenerAmigos(Long id) {
		return amistadRepository.findAmigosByUsuarioId(id);
	}

	@Transactional
	@Override
	public void eliminarAmistad(Long id, Long id2) {
		amistadRepository.eliminarAmistad(id, id2);
		amistadRepository.eliminarAmistad(id2, id);
	}

	@Override
	public boolean sonAmigos(Usuario usuario1, Usuario usuario2) {
        return amistadRepository.existsByUsuario1AndUsuario2(usuario1, usuario2) ||
               amistadRepository.existsByUsuario1AndUsuario2(usuario2, usuario1);
    }
}
