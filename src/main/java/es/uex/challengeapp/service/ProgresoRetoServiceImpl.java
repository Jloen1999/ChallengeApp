package es.uex.challengeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.ProgresoRetoRepository;

@Service
public class ProgresoRetoServiceImpl implements ProgresoRetoService {

	@Autowired
	private ProgresoRetoRepository progresoRetoRepository;

	@Override
	public ProgresoReto actualizarProgreso(ProgresoReto progresoReto) {
		return progresoRetoRepository.save(progresoReto);
	}

	@Override
	public ProgresoReto buscarProgresoReto(Usuario usuario, Reto reto) {
		return progresoRetoRepository.findByUsuarioAndReto(usuario, reto).orElse(new ProgresoReto());
	}

}
