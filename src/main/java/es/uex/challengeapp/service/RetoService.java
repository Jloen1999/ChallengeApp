package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

public interface RetoService {

	List<Reto> getNovedososRetos();

	Reto crearReto(Reto reto);

	List<Reto> obtenerRetosCreadosPorUsuario(Long usuarioId);

	Reto obtenerReto(Long id);
	
	List<Reto> obtenerTodosLosRetos();
	
	float tiempoEnCompletado(Usuario usuario, Reto reto);
}
