package es.uex.challengeapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Punto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.PuntoRepository;

@Service
public class PuntoServiceImpl implements PuntoService {

	@Autowired
	private PuntoRepository puntoRepository;

	@Override
	public Punto registrarPuntos(Punto punto) {
		return puntoRepository.save(punto);
	}

	@Override
	public List<Punto> obtenerPuntosUsuario(Usuario usuario) {
		return puntoRepository.findByUsuario(usuario);
	}

	@Override
	public int totalPuntosUsuario(Usuario usuario) {
		int totalPuntos = 0;
		List<Punto> puntos = obtenerPuntosUsuario(usuario);

		for (Punto punto : puntos) {
			totalPuntos += punto.getCantidad();
		}

		return totalPuntos;
	}

}
