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
	public List<Usuario> obtenerAmigos(Integer id) {
		return amistadRepository.findAmigosByUsuarioId(id);
	}

	@Transactional
	@Override
	public void eliminarAmistad(Integer id, Integer id2) {
		amistadRepository.eliminarAmistad(id, id2);
	}

}
